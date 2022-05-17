package ch.bfh.ti.academia;

import ch.bfh.ti.academia.util.ConnectionManager;
import org.h2.tools.RunScript;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBUtil {

	private static final Logger logger = Logger.getLogger(DBUtil.class.getName());

	public static ResultSet runScript(String name) throws SQLException {
		logger.info("Executing SQL script " + name);
		Connection connection = ConnectionManager.getInstance().getConnection();
		try {
			InputStream inputStream = DBUtil.class.getResourceAsStream("/" + name);
			InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			ResultSet results = RunScript.execute(connection, reader);
			ConnectionManager.getInstance().commit(connection);
			return results;
		} finally {
			ConnectionManager.getInstance().close(connection);
		}
	}
}
