/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * The class EnrollmentRepository provides persistence methods for enrollments.
 */
public class EnrollmentRepository {

	private static final String FIND_ENROLL_QUERY = "SELECT * FROM enroll WHERE pid = ? AND mrid = ? ORDER BY mrid ASC";
	private static final String FIND_MODULERUN_QUERY = "SELECT * FROM run WHERE mrid = ?";
	private static final String FIND_ENROLLMENTS = "SELECT p.pid, p.firstname, p.lastname, e.grade FROM enroll AS e LEFT JOIN person AS p ON e.pid = p.pid WHERE p.pid = ? AND e.mrid = ? ORDER BY e.mrid ASC";
	private static final String INSERT_ENROLL_QUERY = "INSERT INTO enroll(pid,mrid) VALUES(?,?)";
	private static final String DELETE_ENROLL_QUERY = "DELETE FROM enroll WHERE pid = ? AND mrid = ?";
	private static final String INSERT_GRADE_QUERY = "UPDATE enroll SET grade = ?, version = ? WHERE pid = ? AND mrid = ? AND version = ?";
	private static final String FIND_TEACHER = "SELECT r.mrid, r.mid, r.year, r.semester FROM run AS r LEFT JOIN teacher_run AS t ON r.mrid = t.mrid WHERE pid = ? AND r.mrid = ? ORDER BY r.mrid ASC";
	private static final String SELECT_VERSION = "SELECT version FROM enroll WHERE pid = ? AND mrid = ?";

	private static final Logger logger = Logger.getLogger(EnrollmentRepository.class.getName());

	private final Connection connection;

	public EnrollmentRepository(Connection connection) {
		this.connection = connection;
	}

	public boolean checkModulerun(int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_MODULERUN_QUERY)) {
			statement.setInt(1, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			return results.next();
		}
	}

	public boolean checkGrade(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_ENROLL_QUERY)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				String grade = results.getString("grade");
				return grade != null && !grade.equals("");
			} else return false;
		}
	}

	public boolean checkTeacher(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_TEACHER)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			return results.next();
		}
	}

	public boolean isStudentEnrolled(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_ENROLL_QUERY)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			return results.next();
		}
	}

	public boolean setGrade(int pid, int mrid, String grade) throws SQLException {
		int version;
		try (PreparedStatement versionStatement = connection.prepareStatement(SELECT_VERSION)) {
			versionStatement.setInt(1,pid);
			versionStatement.setInt(2,mrid);
			ResultSet results = versionStatement.executeQuery();
			results.next();
			version = results.getInt("version");
		}
		try (PreparedStatement updateStatement = connection.prepareStatement(INSERT_GRADE_QUERY)) {
			int index = 0;
			updateStatement.setString(++index, grade);
			updateStatement.setInt(++index, version+1);
			updateStatement.setInt(++index, pid);
			updateStatement.setInt(++index, mrid);
			updateStatement.setInt(++index,version);
			logger.info("Executing query: " + updateStatement);
			return updateStatement.executeUpdate() > 0;
		}
	}

	public boolean enroll(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(INSERT_ENROLL_QUERY)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			return statement.executeUpdate() > 0;
		}
	}

	public boolean deroll(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(DELETE_ENROLL_QUERY)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			return statement.executeUpdate() > 0;
		}
	}

	public Enrollment getEnrollment(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_ENROLLMENTS)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				Enrollment enrollment = new Enrollment();
				enrollment.setPid(results.getInt("pid"));
				enrollment.setGrade(results.getString("grade"));
				enrollment.setFirstname(results.getString("firstname"));
				enrollment.setLastname(results.getString("lastname"));
				return enrollment;
			} else return null;
		}
	}
}

