/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.authentication;

import ch.bfh.ti.academia.person.Person;
import ch.bfh.ti.academia.person.PersonRepository;
import ch.bfh.ti.academia.util.ConnectionManager;
import ch.bfh.ti.academia.util.ObjectMapperFactory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * The class AuthenticationServlet provides REST endpoints for authorization
 */
@WebServlet("/api/auth/*")
public class AuthenticationController extends HttpServlet {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String JSON_MEDIA_TYPE = "application/json";

	private final MessageDigest hashAlgorithm = MessageDigest.getInstance("SHA-512");
	private final Algorithm algorithm = Algorithm.HMAC256("HjSPDSAw7M8!hxs9euo9n7Knso2NQ%");

	private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());
	private final ConnectionManager connectionManager = ConnectionManager.getInstance();
	private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

	/**
	 * Constructor for class AuthenticationController
	 */
	public AuthenticationController() throws NoSuchAlgorithmException {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		PersonRepository repository = new PersonRepository(connection);

		try {
			if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			// get attribute "pid"
			int pid = Integer.parseInt(request.getAttribute("pid").toString());

			Person person = repository.findPid(pid);
			if (person == null) {
				logger.info("Authenticated person not found!");
				response.setStatus(SC_UNAUTHORIZED);
				return;
			}

			// create AuthResponse
			AuthResponse authResponse = new AuthResponse(person.getPid(), person.getRole());
			response.setStatus(SC_OK);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), authResponse);
		} catch (JsonParseException ex) {
			response.sendError(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		PersonRepository repository = new PersonRepository(connection);

		try {
			if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}
			AuthLogin login = objectMapper.readValue(request.getInputStream(), AuthLogin.class);

			Person person = repository.findUsername(login.getUsername());

			if (person == null) {
				logger.info("Person not found!");
				connectionManager.rollback(connection);
				response.setStatus(SC_UNAUTHORIZED);
				return;
			}

			// create SHA512sum of password
			final byte[] hashbytes = hashAlgorithm.digest(
					login.getPassword().getBytes(StandardCharsets.UTF_8));
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, hashbytes);
			// Convert message digest into hex value
			String hashedPassword = no.toString(16);
			// Add preceding 0s to make it 32 bit
			while (hashedPassword.length() < 32) {
				hashedPassword = "0" + hashedPassword;
			}

			// check if password is valid
			if (!person.getPassword().equals(hashedPassword)) {
				logger.info("Wrong password!");
				connectionManager.rollback(connection);
				response.setStatus(SC_UNAUTHORIZED);
				return;
			}

			// create jwt -> expiration date is 36 hour in future
			Date expDate = new Date(System.currentTimeMillis() + 1000 * 3600 * 36);
			String token = JWT.create()
					.withIssuer("academia")
					.withExpiresAt(expDate)
					.withClaim("pid", person.getPid())
					.withClaim("role", person.getRole())
					.sign(algorithm);

			response.setStatus(SC_OK);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			response.getWriter().print(token);
		} catch (JWTCreationException ex) {
			logger.info("Failed to create JWT");
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} catch (JsonParseException ex) {
			response.sendError(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		}
		finally {
			connectionManager.close(connection);
		}
	}
}