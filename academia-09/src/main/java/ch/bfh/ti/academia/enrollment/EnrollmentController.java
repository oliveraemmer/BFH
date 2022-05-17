/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enrollment;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * The class EnrollmentController provides REST endpoints for the administration of enrollments.
 */
@WebServlet("/api/enrollments/*")
public class EnrollmentController extends HttpServlet {

	private static final Logger logger = Logger.getLogger(EnrollmentController.class.getName());

	private final ConnectionManager connectionManager = ConnectionManager.getInstance();
	private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String JSON_MEDIA_TYPE = "application/json";

	/**
	 * Enrollment of a student to a modulerun
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		EnrollmentRepository repository = new EnrollmentRepository(connection);

		// get role and pid
		int pid = Integer.parseInt(request.getAttribute("pid").toString());
		String role = request.getAttribute("role").toString();

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			// check if user is student
			if (!role.equals("student")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// get mrid and pid from pathInfo
			String[] pathInfoArray = pathInfo.split("-");
			int pathMrid = Integer.parseInt(pathInfoArray[0].substring(1));
			int pathPid = Integer.parseInt(pathInfoArray[1]);

			// check if the authenticated student enrolls for him-/herself
			if (pid != pathPid) {
				connectionManager.rollback(connection);
				response.sendError(SC_BAD_REQUEST);
				return;
			}

			// check if modulerun exists
			if (!repository.checkModulerun(pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			// check if student is already enrolled
			if (repository.isStudentEnrolled(pathPid, pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_CONFLICT);
				return;
			}

			logger.info("Create enrollment for student  " + pathPid + " at modulerun " + pathMrid);
			boolean success = repository.enroll(pathPid, pathMrid);
			response.setStatus(success ? SC_CREATED : SC_CONFLICT);
			connectionManager.commit(connection);

			// get updated module for response
			Enrollment responseEnrollment = repository.getEnrollment(pathPid, pathMrid);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), responseEnrollment);
		} catch (NumberFormatException ex) {
			connectionManager.rollback(connection);
			logger.info("Bad Request");
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_BAD_REQUEST);
		} catch (JsonParseException ex) {
			connectionManager.rollback(connection);
			response.sendError(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			connectionManager.rollback(connection);
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}

	/**
	 * Delete enrollment of a student
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		EnrollmentRepository repository = new EnrollmentRepository(connection);

		// get role and pid
		int pid = Integer.parseInt(request.getAttribute("pid").toString());
		String role = request.getAttribute("role").toString();

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			// check if user is student
			if (!role.equals("student")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// get mrid and pid from pathInfo
			String[] pathInfoArray = pathInfo.split("-");
			int pathMrid = Integer.parseInt(pathInfoArray[0].substring(1));
			int pathPid = Integer.parseInt(pathInfoArray[1]);

			// check if the authenticated student enrolls for him-/herself
			if (pid != pathPid) {
				connectionManager.rollback(connection);
				response.sendError(SC_BAD_REQUEST);
				return;
			}

			// check if student isn't enrolled
			if (!repository.isStudentEnrolled(pathPid, pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			// check if grade is already set
			if (repository.checkGrade(pathPid, pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			logger.info("Delete enrollment for student  " + pathPid + " at modulerun " + pathMrid);
			boolean success = repository.deroll(pathPid, pathMrid);
			response.setStatus(success ? SC_NO_CONTENT : SC_CONFLICT);
			connectionManager.commit(connection);
		} catch (NumberFormatException ex) {
			connectionManager.rollback(connection);
			logger.info("Bad Request");
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_BAD_REQUEST);
		} catch (JsonParseException ex) {
			connectionManager.rollback(connection);
			response.sendError(SC_BAD_REQUEST);
		} catch (SQLException ex) {
			connectionManager.rollback(connection);
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
		} finally {
			connectionManager.close(connection);
		}
	}

	/**
	 * Set Grade to a student of a modulerun
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		EnrollmentRepository repository = new EnrollmentRepository(connection);

		// get role and pid
		int pid = Integer.parseInt(request.getAttribute("pid").toString());
		String role = request.getAttribute("role").toString();

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			// check if user is teacher
			if (!role.equals("teacher")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// get mrid and pid from pathInfo
			String[] pathInfoArray = pathInfo.split("-");
			int pathMrid = Integer.parseInt(pathInfoArray[0].substring(1));
			int pathPid = Integer.parseInt(pathInfoArray[1]);

			// check if modulerun exists
			if (!repository.checkModulerun(pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			// check if the authenticated teacher teaches modulerun
			if (!repository.checkTeacher(pid, pathMrid)) {
				logger.info("Teacher doesn't teach modulerun " + pathMrid);
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// check if student is enrolled
			if (!repository.isStudentEnrolled(pathPid, pathMrid)) {
				logger.info("Student " + pathPid + " isn't enrolled in modulerun " + pathMrid);
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			// get grade from request body
			Grade gradeObject = objectMapper.readValue(request.getInputStream(), Grade.class);
			String grade = gradeObject.getGrade();

			// check if grade syntax is correct
			if (!grade.matches("[a-f]") || grade.length() != 1) {
				connectionManager.rollback(connection);
				response.setStatus(SC_BAD_REQUEST);
				return;
			}

			// Execute setGrade in the repository
			logger.info("Set grade for student " + pathPid + " at modulerun " + pathMrid);
			boolean success = repository.setGrade(pathPid, pathMrid, grade);
			response.setStatus(success ? SC_OK : SC_CONFLICT);
			connectionManager.commit(connection);

			// get updated module for response
			Enrollment responseEnrollment = repository.getEnrollment(pathPid, pathMrid);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), responseEnrollment);
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

	/**
	 * Set Grade to a student of a modulerun
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection connection = connectionManager.getConnection();
		EnrollmentRepository repository = new EnrollmentRepository(connection);

		// get role and pid
		int pid = Integer.parseInt(request.getAttribute("pid").toString());
		String role = request.getAttribute("role").toString();

		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo == null || pathInfo.equals("/")) {
				connectionManager.rollback(connection);
				response.setStatus(SC_METHOD_NOT_ALLOWED);
				return;
			}

			// get mrid and pid from pathInfo
			String[] pathInfoArray = pathInfo.split("-");
			int pathMrid = Integer.parseInt(pathInfoArray[0].substring(1));
			int pathPid = Integer.parseInt(pathInfoArray[1]);

			// check if modulerun exists
			if (!repository.checkModulerun(pathMrid)) {
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			// check if a student enrolls for him-/herself
			if (role.equals("student") && (pid != pathPid)) {
				connectionManager.rollback(connection);
				response.sendError(SC_BAD_REQUEST);
				return;
			}

			// check if a teacher teaches modulerun
			if (role.equals("teacher") && !repository.checkTeacher(pid, pathMrid)) {
				logger.info("Teacher doesn't teach modulerun " + pathMrid);
				connectionManager.rollback(connection);
				response.setStatus(SC_FORBIDDEN);
				return;
			}

			// check if student is enrolled
			if (!repository.isStudentEnrolled(pathPid, pathMrid)) {
				logger.info("Student " + pathPid + " isn't enrolled in modulerun " + pathMrid);
				connectionManager.rollback(connection);
				response.setStatus(SC_NOT_FOUND);
				return;
			}

			logger.info("Get enrollment for student  " + pathPid + " at modulerun " + pathMrid);
			Enrollment enrollment = repository.getEnrollment(pathPid, pathMrid);
			if (enrollment == null) {
				connectionManager.rollback(connection);
				response.sendError(SC_NOT_FOUND);
				return;
			}
			response.setStatus(SC_OK);
			response.setHeader(CONTENT_TYPE_HEADER, JSON_MEDIA_TYPE);
			objectMapper.writeValue(response.getOutputStream(), enrollment);
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
