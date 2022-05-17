/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModuleTest {

	@Test
	public void createModule() {
		Module module = new Module("BTI9999", "Name0", "Description0", null);
		assertEquals("BTI9999", module.getMid());
		assertEquals("Name0", module.getName());
		assertEquals("Description0", module.getDescription());
	}
}
