/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * The class AuthenticationFilter is used to authenticate all HTTP requests.
 */
@WebFilter(urlPatterns = "/api/*")
public class AuthenticationFilter extends HttpFilter {

	private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
	private static final String AUTH_HEADER = "Authorization";
	private static final String AUTH_SCHEME = "Bearer";
	private final Algorithm algorithm = Algorithm.HMAC256("HjSPDSAw7M8!hxs9euo9n7Knso2NQ%");

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// add some HEADERs to the response to avoid problems with Cross-Origin Resource Sharing (CORS)
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(SC_OK);
			return;

		// no authentication only on /api/auth with POST
		} else if (!(request.getMethod().equals("POST") && request.getRequestURI().equals("/api/auth"))) {

			try {
				// validate jwt token and get user infos
				String[] userInfos = validateToken(request);

				// set request attributes "pid" & "role"
				request.setAttribute("pid", userInfos[0]);
				request.setAttribute("role", userInfos[1]);
			} catch (JWTDecodeException ex) {
				logger.info("Failed to decode JWT");
				response.setStatus(SC_UNAUTHORIZED);
				return;
			} catch (JWTVerificationException ex) {
				logger.info("JWT expired");
				response.setStatus(SC_UNAUTHORIZED);
				return;
			} catch (Exception ex) {
				logger.info("Authentication failed");
				response.setStatus(SC_UNAUTHORIZED);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private String[] validateToken(HttpServletRequest request) throws Exception {
		String authHeader = request.getHeader(AUTH_HEADER);
		String[] headerTokens = authHeader.split(" ");

		// check if authentication scheme is bearer
		if (!headerTokens[0].equals(AUTH_SCHEME)) throw new Exception();

		// verify jwt token
		String token = headerTokens[1];
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer("academia")
				.build();

		// decode jwt token
		DecodedJWT jwt = verifier.verify(token);

		// return pid and role from jwt token
		String pid = jwt.getClaim("pid").asInt().toString();
		String role = jwt.getClaim("role").asString();
		return new String[]{pid, role};
	}
}
