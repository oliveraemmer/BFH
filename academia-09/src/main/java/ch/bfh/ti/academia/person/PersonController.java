/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import ch.bfh.ti.academia.util.ConnectionManager;
import ch.bfh.ti.academia.util.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * The class PersonController provides REST endpoints to get infos of persons.
 */
@WebServlet("/api/persons/*")
public class PersonController extends HttpServlet {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String JSON_MEDIA_TYPE = "application/json";

	private static final Logger logger = Logger.getLogger(PersonController.class.getName());

	private final ConnectionManager connectionManager = ConnectionManager.getInstance();
	private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		PersonRepository repository = new PersonRepository(connection);

		// get role and pid
		String role = request.getAttribute("role").toString();
		int pid = Integer.parseInt(request.getAttribute("pid").toString());

		try {
			if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			int pathPid = Integer.parseInt(request.getPathInfo().substring(1));

			// check if user is student
			if (role.equals("student") && pathPid != pid) {
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			logger.info("Getting person " + pathPid);
			Person person = repository.findPid(pathPid);
			if (person == null) {
				connectionManager.rollback(connection);
				response.sendError(SC_NOT_FOUND);
				return;
			}
			response.setStatus(SC_OK);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), person);
		} catch (NumberFormatException ex) {
			connectionManager.rollback(connection);
			logger.info("Bad Request");
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_BAD_REQUEST);
		}
		catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}
}
