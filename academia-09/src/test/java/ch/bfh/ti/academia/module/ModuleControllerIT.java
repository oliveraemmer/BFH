/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import ch.bfh.ti.academia.DBUtil;
import ch.bfh.ti.academia.LogUtil;
import ch.bfh.ti.academia.ServerUtil;
import ch.bfh.ti.academia.authentication.AuthLogin;
import ch.bfh.ti.academia.person.PersonDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleControllerIT {

	private static final String USERNAME = "thomas";
	private static final String PASSWORD = "thomas";

	@BeforeAll
	public static void setup() throws Exception {
		RestAssured.port = 8080;
		LogUtil.setLevel(Level.INFO);
		DBUtil.runScript("db-create.sql");
		DBUtil.runScript("db-init.sql");
		ServerUtil.start();
	}

	@AfterAll
	public static void cleanup() throws Exception {
		ServerUtil.stop();
	}

	@Test
	public void getModule() {
		AuthLogin login = new AuthLogin(USERNAME, PASSWORD);

		String token = RestAssured
			.given().contentType(ContentType.JSON)
			.body(login)
			.when().post("/api/auth")
			.then().statusCode(200).extract().response().asString();

		Module foundModule = RestAssured
			.given().auth().oauth2(token).accept(ContentType.JSON)
				.when().get("/api/modules/BTI1222")
				.then().statusCode(200).extract().as(Module.class);
		assertEquals("BTI1222", foundModule.getMid());

		RestAssured
				.given().auth().oauth2(token).accept(ContentType.JSON)
				.when().get("/api/modules/Mx")
				.then().statusCode(404);
	}

	@Test
	public void getModules() {
		AuthLogin login = new AuthLogin(USERNAME, PASSWORD);
		String token = RestAssured
			.given().contentType(ContentType.JSON)
			.body(login)
			.when().post("/api/auth")
			.then().statusCode(200).extract().response().asString();
		PersonDTO coordinator = new PersonDTO(2,"Thomas","teacher");
		Module module = new Module("BTI1222", "TestModule", "Module for unit Tests", coordinator);
		Module[] modules = RestAssured
				.given().auth().oauth2(token).accept(ContentType.JSON)
				.when().get("/api/modules")
				.then().statusCode(200).extract().as(Module[].class);
		assertTrue(Arrays.asList(modules).contains(module));
	}

	@Test
	public void updateModule() {
		AuthLogin login = new AuthLogin(USERNAME, PASSWORD);
		String token = RestAssured
			.given().contentType(ContentType.JSON)
			.body(login)
			.when().post("/api/auth")
			.then().statusCode(200).extract().response().asString();

		Module module = RestAssured
				.given().auth().oauth2(token).accept(ContentType.JSON)
				.when().get("/api/modules/BTI1222")
				.then().statusCode(200).extract().as(Module.class);
		module.setDescription("New Description for BTI1222");
		ModuleDTO bodyModule = new ModuleDTO(module.getMid(), module.getDescription());
		RestAssured
				.given().auth().oauth2(token)
				.contentType(ContentType.JSON).accept(ContentType.JSON).body(bodyModule)
				.when().put("/api/modules/BTI1222")
				.then().statusCode(200);
		Module updatedModule = RestAssured
				.given().auth().oauth2(token).accept(ContentType.JSON)
				.when().get("/api/modules/BTI1222")
				.then().statusCode(200).extract().as(Module.class);
		assertEquals(module.getDescription(), updatedModule.getDescription());
	}


}
