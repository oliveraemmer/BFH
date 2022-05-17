/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import ch.bfh.ti.academia.util.ConnectionManager;
import ch.bfh.ti.academia.util.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * The class ModuleController provides REST endpoints for the administration of modules.
 */
@WebServlet("/api/modules/*")
public class ModuleController extends HttpServlet {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String JSON_MEDIA_TYPE = "application/json";

	private static final Logger logger = Logger.getLogger(ModuleController.class.getName());

	private final ConnectionManager connectionManager = ConnectionManager.getInstance();
	private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		ModuleRepository repository = new ModuleRepository(connection);

		try {
			if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
				logger.info("Getting all modules");
				List<Module> modules = repository.findAll();
				response.setStatus(SC_OK);
				response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
				objectMapper.writeValue(response.getOutputStream(), modules);
			} else {
				String mid = request.getPathInfo().substring(1);
				logger.info("Getting module " + mid);
				Module module = repository.find(mid);
				if (module == null) {
					connectionManager.rollback(connection);
					response.sendError(SC_NOT_FOUND);
					return;
				}
				response.setStatus(SC_OK);
				response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
				objectMapper.writeValue(response.getOutputStream(), module);
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		ModuleRepository repository = new ModuleRepository(connection);

		// get role and pid
		String role = request.getAttribute("role").toString();
		int pid = Integer.parseInt(request.getAttribute("pid").toString());

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				return;
			}

			String mid = pathInfo.substring(1);
			ModuleDTO module = objectMapper.readValue(request.getInputStream(), ModuleDTO.class);

			// check path mid with json body mid
			if (module.getMid() == null || !module.getMid().equals(mid)) {
				connectionManager.rollback(connection);
				logger.info("mid body & context mismatch");
				response.sendError(SC_BAD_REQUEST);
				return;
			}

			// check if module exists
			Module moduleDB = repository.find(mid);
			if(moduleDB == null ){
				connectionManager.rollback(connection);
				response.sendError(SC_NOT_FOUND);
				return;
			}

			// check if user is a teacher and the coordinator of the module
			if (!(role.equals("teacher") && moduleDB.getCoordinator().getPid() == pid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// create new module with only the new description added
			Module moduleNew = moduleDB;
			moduleNew.setDescription(module.getDescription());

			logger.info("Updating module " + mid);
			boolean success = repository.update(moduleNew);
			response.setStatus(success ? SC_OK : SC_CONFLICT);
			connectionManager.commit(connection);

			// get updated module for response
			Module responseModule = repository.find(mid);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), responseModule);
		} catch (JsonParseException ex) {
			connectionManager.rollback(connection);
			response.sendError(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}
}
