/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The class ConnectionManager is used to manage database connections.
 */
public class ConnectionManager {

	private static final String PROPERTY_FILE_PATH = "/jdbc.properties";

	private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

	private final String url;
	private final String user;
	private final String password;

	private static ConnectionManager instance;

	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	private ConnectionManager() {
		try {
			Properties props = new Properties();
			props.load(ConnectionManager.class.getResourceAsStream(PROPERTY_FILE_PATH));
			Class.forName(props.getProperty("database.driver"));
			url = props.getProperty("database.url");
			user = props.getProperty("database.user");
			password = props.getProperty("database.password");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Connection getConnection() {
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			logger.fine("Connection opened: " + connection);
			return connection;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void commit(Connection connection) {
		try {
			connection.commit();
			logger.fine("Connection committed: " + connection);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void rollback(Connection connection) {
		try {
			connection.rollback();
			logger.fine("Connection rolled back: " + connection);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void close(Connection connection) {
		try {
			connection.close();
			logger.fine("Connection closed: " + connection);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
