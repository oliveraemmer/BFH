/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * The class PersonRepository provides persistence methods for persons.
 */
public class PersonRepository {

	private static final String FIND_QUERY_PID = "SELECT * FROM person WHERE pid = ? ORDER BY pid ASC";
	private static final String FIND_QUERY_USERNAME = "SELECT * FROM person WHERE username = ?";

	private static final Logger logger = Logger.getLogger(PersonRepository.class.getName());

	private final Connection connection;

	public PersonRepository(Connection connection) {
		this.connection = connection;
	}

	public Person findPid(int pid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY_PID)) {
			statement.setInt(1, pid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return getPerson(results);
			} else return null;
		}
	}

	public Person findUsername(String username) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY_USERNAME)) {
			statement.setString(1, username);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return getPerson(results);
			} else return null;
		}
	}

	private Person getPerson(ResultSet results) throws SQLException {
		Person person = new Person();
		person.setPid(results.getInt("pid"));
		person.setFirstname(results.getString("firstname"));
		person.setLastname(results.getString("lastname"));
		person.setAddress(results.getString("address"));
		person.setSex(results.getString("sex"));
		person.setBirthdate(results.getDate("birthdate"));
		person.setRole(results.getString("role"));
		person.setUsername(results.getString("username"));
		person.setPassword(results.getString("password"));
		return person;
	}
}
