/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import ch.bfh.ti.academia.DBUtil;
import ch.bfh.ti.academia.LogUtil;
import ch.bfh.ti.academia.person.PersonDTO;
import ch.bfh.ti.academia.util.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleRepositoryIT {

	private static Connection connection;
	private static ModuleRepository repository;

	@BeforeAll
	public static void setup() throws SQLException {
		LogUtil.setLevel(Level.FINE);
		DBUtil.runScript("db-create.sql");
		DBUtil.runScript("db-init.sql");
		connection = ConnectionManager.getInstance().getConnection();
		repository = new ModuleRepository(connection);
	}

	@AfterAll
	public static void cleanup() {
		ConnectionManager.getInstance().close(connection);
	}

	@Test
	public void findModule() throws SQLException {
		Module foundModule = repository.find("BTI1222");
		assertEquals("BTI1222", foundModule.getMid());
		assertNull(repository.find("Mx"));
	}

	@Test
	public void findModules() throws SQLException {
		PersonDTO coordinator = new PersonDTO(2,"thomas","teacher");
		List<Module> modules = repository.findAll();
		Module module = new Module("BTI1222", "TestModule", "Module for unit Tests",coordinator);
		assertTrue(modules.contains(module));
	}

	@Test
	public void updateModule() throws SQLException {
		Module module = repository.find("BTI1222");
		module.setDescription("New Description for BTI1222");
		repository.update(module);
		Module updatedModule = repository.find("BTI1222");
		assertEquals(module.getDescription(), updatedModule.getDescription());
	}

	@Test
	public void deleteModule() throws SQLException {
		repository.delete("BTI9999");
		assertFalse(repository.delete("BTI9999"));
	}
}
