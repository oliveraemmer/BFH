/*
 * Project and Training 1: Oware Game - Computer Science, Berner Fachhochschule
 */
package ch.bfh.oware;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class JavaFXApp extends Application implements EventHandler<ActionEvent> {

	/**
	 * Start method called by the JavaFX framework upon calling launch().
	 *
	 * @param stage a (default) stage provided by the framework
	 */
	private Button onePlayer;
	private Button twoPlayer;
	private BorderPane root;
	private Scene scene;
	private Scene scene2;
	private Stage stage2;
	private VBox header;
	private HBox buttons;
	private Label titel;
	private Label chooseMode;
	private Label setName;
	private Label setNames;
	private TextField userName1;
	private TextField userName2;
	@Override
	public void start(Stage stage) {
		final int width = 300;
		final int height = 300;

		root = new BorderPane(); root.setPadding(new Insets(10));

		header = new VBox();
		root.setCenter(header);

		buttons = new HBox();
		root.setBottom(buttons);

		userName1 = new TextField("Player 1");
		userName2 = new TextField("Player 2");

		titel = new Label("OWARE\n");
		titel.setFont(Font.font("Default", FontWeight.BOLD, 20));
		chooseMode = new Label("Choose single or multiplayer");
		setName = new Label("Choose a playername");
		setNames = new Label("Choose playernames");

		onePlayer = new Button("1 Player");
		twoPlayer = new Button("2 Player");
		onePlayer.addEventHandler(ActionEvent.ACTION, this);
		twoPlayer.addEventHandler(ActionEvent.ACTION, this);

		header.getChildren().addAll(titel, chooseMode);
		buttons.getChildren().add(onePlayer);
		buttons.getChildren().add(twoPlayer);

		scene = new Scene(root, width, height);
		//scene2 = new Scene(setName, width, height);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource().equals(onePlayer)) {
			this.header.getChildren().clear();
			this.header.getChildren().addAll(titel, setName, userName1);
		}

		if (event.getSource().equals(twoPlayer)) {
			this.header.getChildren().clear();
			this.header.getChildren().addAll(titel, setName, userName1, userName2);
		}
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
