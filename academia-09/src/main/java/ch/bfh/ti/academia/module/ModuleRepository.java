/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import ch.bfh.ti.academia.person.PersonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class ModuleRepository provides persistence methods for modules.
 */
public class ModuleRepository {

	private static final String FIND_ALL_QUERY = "SELECT m.mid, m.name, m.description, p.pid, p.firstname, p.lastname FROM module AS m LEFT JOIN person AS p ON m.pid = p.pid ORDER BY m.mid ASC";
	private static final String FIND_QUERY = "SELECT m.mid, m.name, m.description, p.pid, p.firstname, p.lastname FROM module AS m LEFT JOIN person AS p ON m.pid = p.pid WHERE mid = ? ORDER BY m.mid ASC";
	private static final String UPDATE_QUERY = "UPDATE module SET name = ?, description = ?, version = ? WHERE mid =? AND version = ?";
	private static final String DELETE_QUERY = "DELETE FROM module WHERE mid = ?";
	private static final String SELECT_VERSION = "SELECT version FROM module WHERE mid = ?";

	private static final Logger logger = Logger.getLogger(ModuleRepository.class.getName());

	private final Connection connection;

	public ModuleRepository(Connection connection) {
		this.connection = connection;
	}

	public List<Module> findAll() throws SQLException {
		List<Module> modules = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				modules.add(getModule(results));
			}
			return modules;
		}
	}

	public Module find(String mid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY)) {
			statement.setString(1, mid);
			logger.info("Executing query: " + statement);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return getModule(results);
			} else return null;
		}
	}

	public boolean update(Module module) throws SQLException {
		int version;
		try (PreparedStatement versionStatement = connection.prepareStatement(SELECT_VERSION)) {
			versionStatement.setString(1,module.getMid());
			ResultSet results = versionStatement.executeQuery();
			results.next();
			version = results.getInt("version");

		}
		try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
			int index = 0;
			updateStatement.setString(++index, module.getName());
			updateStatement.setString(++index, module.getDescription());
			updateStatement.setInt(++index,version+1);
			updateStatement.setString(++index, module.getMid());
			updateStatement.setInt(++index, version);
			logger.info("Executing query: " + updateStatement);
			return updateStatement.executeUpdate() > 0;
		}
	}

	public boolean delete(String mid) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
			statement.setString(1, mid);
			logger.info("Executing query: " + statement);
			return statement.executeUpdate() > 0;

		}
	}

	private Module getModule(ResultSet results) throws SQLException {
		Module module = new Module();
		module.setMid(results.getString("mid"));
		module.setName(results.getString("name"));
		module.setDescription(results.getString("description"));
		PersonDTO coordinator = new PersonDTO(results.getInt("pid"), results.getString("firstname"), results.getString("lastname"));
		module.setCoordinator(coordinator);
		return module;
	}
}
