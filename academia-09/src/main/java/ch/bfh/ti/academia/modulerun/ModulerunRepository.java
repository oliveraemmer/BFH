/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.enrollment.Enrollment;
import ch.bfh.ti.academia.module.ModuleRepository;
import ch.bfh.ti.academia.person.PersonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class ModulerunRepository provides persistence methods for moduleruns.
 */
public class ModulerunRepository {

	// statements for students
	private static final String FIND_QUERY = "SELECT r.mrid, r.mid, m.name, m.description, r.year, r.semester, e.pid, e.grade FROM run AS r LEFT JOIN module AS m ON r.mid = m.mid LEFT JOIN enroll AS e ON r.mrid = e.mrid AND e.pid = ? ORDER BY r.mrid ASC";
	private static final String FIND_QUERY_WITH_MRID = "SELECT r.mrid, r.mid, m.name, m.description, r.year, r.semester, e.pid, e.grade FROM run AS r LEFT JOIN module AS m ON r.mid = m.mid LEFT JOIN enroll AS e ON r.mrid = e.mrid AND e.pid = ? WHERE r.mrid = ? ORDER BY r.mrid ASC";
	// statements for teacher
	private static final String FIND_QUERY_TEACHER = "SELECT r.mrid, r.mid, m.name, m.description, r.year, r.semester FROM run AS r LEFT JOIN module AS m ON r.mid = m.mid LEFT JOIN teacher_run AS t ON r.mrid = t.mrid WHERE t.pid = ? ORDER BY r.mrid ASC";
	private static final String FIND_QUERY_TEACHER_WITH_MRID = "SELECT r.mrid, r.mid, m.name, m.description, r.year, r.semester FROM run AS r LEFT JOIN module AS m ON r.mid = m.mid LEFT JOIN teacher_run AS t ON r.mrid = t.mrid WHERE t.pid = ? AND r.mrid = ? ORDER BY r.mrid ASC";
	// utils
	private static final String FIND_TEACHERS = "SELECT p.pid, p.firstname, p.lastname FROM teacher_run AS t LEFT JOIN person AS p ON t.pid = p.pid WHERE t.mrid = ? ORDER BY p.pid ASC";
	private static final String FIND_ENROLLMENTS = "SELECT p.pid, p.firstname, p.lastname, e.grade FROM enroll AS e LEFT JOIN person AS p ON e.pid = p.pid WHERE e.mrid = ? ORDER BY p.pid ASC";
	private static final String FIND_COORDINATOR = "SELECT p.pid, p.firstname, p.lastname FROM module AS m LEFT JOIN person AS p ON m.pid = p.pid WHERE mid = ?";
	private static final String FIND_MODULERUN_QUERY = "SELECT * FROM run WHERE mrid = ?";
	private static final Logger logger = Logger.getLogger(ModuleRepository.class.getName());

	private final Connection connection;

	public ModulerunRepository(Connection connection) {
		this.connection = connection;
	}

	public List<Modulerun> findAllForTeacher(int pid) throws SQLException {
		List<Modulerun> moduleruns = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY_TEACHER)) {
			statement.setInt(1, pid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int mrid = results.getInt("mrid");
				List<PersonDTO> teachers = getTeachers(mrid);
				List<Enrollment> enrollments = getEnrollments(mrid);
				moduleruns.add(getModulerunTeacher(results, teachers, enrollments));
			}
			return moduleruns;
		}
	}

	public Modulerun findForTeacher(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY_TEACHER_WITH_MRID)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				List<PersonDTO> teachers = getTeachers(mrid);
				List<Enrollment> enrollments = getEnrollments(mrid);
				return getModulerunTeacher(results, teachers, enrollments);
			} else return null;
		}
	}

	public List<Modulerun> findAllForStudents(int pid) throws SQLException {
		List<Modulerun> moduleruns = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY)) {
			statement.setInt(1, pid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int mrid = results.getInt("mrid");
				List<PersonDTO> teachers = getTeachers(mrid);
				moduleruns.add(getModulerunStudent(results, teachers));
			}
			return moduleruns;
		}
	}

	public Modulerun findForStudent(int pid, int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY_WITH_MRID)) {
			statement.setInt(1, pid);
			statement.setInt(2, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				List<PersonDTO> teachers = getTeachers(mrid);
				return getModulerunStudent(results, teachers);
			} else return null;
		}
	}

	private List<PersonDTO> getTeachers(int mrid) throws SQLException {
		List<PersonDTO> teachers = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(FIND_TEACHERS)) {
			statement.setInt(1, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				PersonDTO teacher = new PersonDTO();
				teacher.setPid(results.getInt("pid"));
				teacher.setLastname(results.getString("lastname"));
				teacher.setFirstname(results.getString("firstname"));
				teachers.add(teacher);
			}
		}
		return teachers;
	}

	private List<Enrollment> getEnrollments(int mrid) throws SQLException {
		List<Enrollment> enrollments = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(FIND_ENROLLMENTS)) {
			statement.setInt(1, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				Enrollment enrollment = new Enrollment();
				enrollment.setPid(results.getInt("pid"));
				enrollment.setGrade(results.getString("grade"));
				enrollment.setFirstname(results.getString("firstname"));
				enrollment.setLastname(results.getString("lastname"));
				enrollments.add(enrollment);
			}
		}
		return enrollments;
	}

	private Modulerun getModulerunStudent(ResultSet results, List<PersonDTO> teachers) throws SQLException {
		// create modulerun
		Modulerun modulerun = new Modulerun();
		modulerun.setMrid(results.getInt("mrid"));
		modulerun.setYear(results.getInt("year"));
		modulerun.setSemester(results.getString("semester"));
		modulerun.setName(results.getString("name"));
		modulerun.setDescription(results.getString("description"));

		// set mid and coordinator
		String mid = results.getString("mid");
		modulerun.setMid(mid);
		PersonDTO coordinator = findCoordinator(mid);
		modulerun.setCoordinator(coordinator);

		// only set enrolled, if column "pid" isn't empty
		if (results.getString("pid") != null) {
			modulerun.setEnrolled("true");
			// only set grade if student is enrolled and grade is set
			if (results.getString("grade") != null) {
				modulerun.setGrade(results.getString("grade"));
			}
		}
		modulerun.setTeachers(teachers);
		return modulerun;
	}

	private Modulerun getModulerunTeacher(ResultSet results, List<PersonDTO> teachers, List<Enrollment> enrollments) throws SQLException {
		// create modulerun
		Modulerun modulerun = new Modulerun();
		modulerun.setMrid(results.getInt("mrid"));
		modulerun.setYear(results.getInt("year"));
		modulerun.setSemester(results.getString("semester"));
		modulerun.setName(results.getString("name"));
		modulerun.setDescription(results.getString("description"));

		// set mid and coordinator
		String mid = results.getString("mid");
		modulerun.setMid(mid);
		PersonDTO coordinator = findCoordinator(mid);
		modulerun.setCoordinator(coordinator);

		modulerun.setTeachers(teachers);
		modulerun.setEnrollments(enrollments);
		return modulerun;
	}

	public PersonDTO findCoordinator(String mid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_COORDINATOR)) {
			statement.setString(1, mid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return new PersonDTO(results.getInt("pid"), results.getString("lastname"), results.getString("firstname"));
			} else return null;
		}
	}

	public boolean checkModulerun(int mrid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_MODULERUN_QUERY)) {
			statement.setInt(1, mrid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			return results.next();
		}
	}
}
