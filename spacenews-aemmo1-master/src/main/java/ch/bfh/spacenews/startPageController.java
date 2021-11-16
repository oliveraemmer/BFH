package ch.bfh.spacenews;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class startPageController {
    @FXML public Button articles;
    @FXML public Button blogs;
    @FXML public Button reports;

    @FXML public AnchorPane anchorPane;
    @FXML public BorderPane borderPane;
    @FXML public ScrollPane scrollPane;
    @FXML public VBox vBox;


    DataAdmin admin = DataAdmin.getInstance();
    ObjectMapper mapper = new ObjectMapper();
    List<Map<String, Object>> jsonMap;
    URL url;
    BorderPane subBorderPane;

    public startPageController() {
    }

    public void initialize() throws IOException {

    }
/*
    @FXML
    public void showArticles (ActionEvent event) throws IOException {
        admin.setType("Articles");
        VBox newsBox = new FXMLLoader().load(getClass().getResource("newsBox.fxml"));
        scrollPane.setContent(newsBox);
    }

    @FXML
    public void showBlogs (ActionEvent event) throws IOException {
        admin.setType("Blogs");
        VBox newsBox = new FXMLLoader().load(getClass().getResource("newsBox.fxml"));
        scrollPane.setContent(newsBox);
    }

    @FXML
    public void showReports (ActionEvent event) throws IOException {
        admin.setType("Reports");
        VBox newsBox = new FXMLLoader().load(getClass().getResource("newsBox.fxml"));
        scrollPane.setContent(newsBox);
    }
*/
    @FXML
    public void showContent (ActionEvent event) throws IOException {
        // Reset View
        Button pressedButton = (Button) event.getSource();
        admin.setType(pressedButton.getText());
        getAPI();
        vBox.getChildren().clear();
        for (int i = 0; i < admin.getEntries().size(); i++) {
            // Load new BoarderPane
            try {
                subBorderPane = new FXMLLoader().load(getClass().getResource("borderPane.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Put entries into subBorderPane and add to VBox
            Text title = new Text((String) admin.getEntries().get(i).getTitle());
            title.setFont(Font.font ("Verdana", 15));
            Text summary = new Text((String) admin.getEntries().get(i).getSummary() + "\n");
            summary.setFont(Font.font ("Verdana", 11));
            TextFlow tfTitle = new TextFlow(title);
            TextFlow tfSummary = new TextFlow(summary);

            String imgPath = (String) admin.getEntries().get(i).getImageUrl();

            Image image = new Image(imgPath);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(70);

            subBorderPane.setTop(tfTitle);
            subBorderPane.setCenter(tfSummary);
            subBorderPane.setLeft(imageView);
            vBox.getChildren().add(subBorderPane);
        }
    }

    public void getAPI() throws IOException {
        // Clear entries and load current data into DataAdmin
        admin.getEntries().clear();
        try {
            url = new URL("https://api.spaceflightnewsapi.net/v3/" + admin.getType());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            jsonMap = mapper.readValue(url, new TypeReference<List<Map<String,Object>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 9; i++) {
            admin.addEntry((int) jsonMap.get(i).get("id"), (String) jsonMap.get(i).get("title"), (String) jsonMap.get(i).get("summary"), (String) jsonMap.get(i).get("imageUrl"));
        }
    }
}
