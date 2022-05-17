/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * The class ModulerunController provides REST endpoints for the administration of moduleruns.
 */
@WebServlet("/api/moduleruns/*")
public class ModulerunController extends HttpServlet {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String JSON_MEDIA_TYPE = "application/json";

	private static final Logger logger = Logger.getLogger(ModulerunController.class.getName());

	private final ConnectionManager connectionManager = ConnectionManager.getInstance();
	private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		ModulerunRepository repository = new ModulerunRepository(connection);

		// get role and pid
		String role = request.getAttribute("role").toString();
		int pid = Integer.parseInt(request.getAttribute("pid").toString());

		try {
			if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
				logger.info("Getting all moduleruns");
				List<Modulerun> moduleruns;
				if (role.equals("teacher")){
					// only get moduleruns the teacher teaches
					moduleruns = repository.findAllForTeacher(pid);
				} else {
					// get all moduleruns
					moduleruns = repository.findAllForStudents(pid);
				}
				response.setStatus(SC_OK);
				response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
				objectMapper.writeValue(response.getOutputStream(), moduleruns);
			} else {
				int mrid = Integer.parseInt(request.getPathInfo().substring(1));
				logger.info("Getting modulerun " + mrid);

				// check if modulerun exists
				if (!repository.checkModulerun(mrid)) {
					connectionManager.rollback(connection);
					response.setStatus(SC_NOT_FOUND);
					return;
				}

				Modulerun modulerun;
				if (role.equals("teacher")){
					// only get modulerun if the teacher teaches it
					modulerun = repository.findForTeacher(pid, mrid);

					// if teacher doesn't teach modulerun, return 403
					if (modulerun == null) {
						connectionManager.rollback(connection);
						response.sendError(SC_FORBIDDEN);
						return;
					}
				} else {
					// get modulerun
					modulerun = repository.findForStudent(pid, mrid);
				}

				response.setStatus(SC_OK);
				response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
				objectMapper.writeValue(response.getOutputStream(), modulerun);
			}
		} catch (NumberFormatException ex) {
			connectionManager.rollback(connection);
			logger.info("Bad Request");
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}
}
