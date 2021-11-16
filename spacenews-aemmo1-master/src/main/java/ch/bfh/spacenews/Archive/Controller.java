/*
 * Project and Training 2: Space News Controller - Computer Science, Berner Fachhochschule
 */

package ch.bfh.spacenews;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Controller {
	@FXML public Label version;

	public Controller() {
	}

	@FXML
	public void initialize() throws IOException {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		version.setText("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
	}

}
