/*
 * Project and Training 2: Space News - Computer Science, Berner Fachhochschule
 */
package ch.bfh.spacenews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Dummy application class demonstrating a JavaFX application.
 */
//fx:controller="ch.bfh.spacenews.Controller"
public class HelloFX extends Application {

	/**
	 * Start method called by the JavaFX framework upon calling launch().
	 *
	 * @param stage a (default) stage provided by the framework
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
		stage.setTitle("Space News App");
		stage.setScene(new Scene(root, 640, 480));
		stage.show();


		/*final int width = 640;
		final int height = 480;
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
		Scene scene = new Scene(new StackPane(l), width, height);
		stage.setScene(scene);
		stage.show();*/
	}

	/**
	 * Main entry point of the application.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch();
	}

}
