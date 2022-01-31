package ch.bfh.spacenews;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class startPageController {
    @FXML public Button articles;
    @FXML public Button blogs;
    @FXML public Button reports;
    @FXML public Button refresh;

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

    public void initialize() {

    }

    @FXML
    public void showContent (ActionEvent event) throws IOException {
        // Reset View
        Button pressedButton = (Button) event.getSource();
        if(!pressedButton.getText().equals("Refresh")){
            admin.setType(pressedButton.getText());
        } else {
            if(admin.getType() == null){
                admin.setType("Articles");
            }
        }
        getAPI();
        vBox.getChildren().clear();

        // one subborderpane per entry
        for (int i = 0; i < admin.getEntries().size(); i++) {
            // Load new BoarderPane
            try {
                subBorderPane = new FXMLLoader().load(getClass().getResource("borderPane.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Put data into subBorderPane and add to VBox
            Text title = new Text((String) admin.getEntries().get(i).getTitle());
            title.setFont(Font.font ("Verdana", 13));
            Text summary = new Text((String) admin.getEntries().get(i).getSummary() + "\n");
            summary.setFont(Font.font ("Verdana", 10));
            Text link = new Text((String) admin.getEntries().get(i).getLink() + "\n");
            link.setFont(Font.font ("Verdana", 8));
            TextFlow tfTitle = new TextFlow(title);
            TextFlow tfSummary = new TextFlow(summary);
            TextFlow tfLink = new TextFlow(link);

            String imgPath = (String) admin.getEntries().get(i).getImageUrl();

            Image image = new Image(imgPath);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(70);

            subBorderPane.setTop(tfTitle);
            subBorderPane.setCenter(tfSummary);
            subBorderPane.setLeft(imageView);
            subBorderPane.setBottom(tfLink);
            subBorderPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
            jsonMap = mapper.readValue(url, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 9; i++) {
            admin.addEntry((int) jsonMap.get(i).get("id"), (String) jsonMap.get(i).get("title"), (String) jsonMap.get(i).get("summary"), (String) jsonMap.get(i).get("imageUrl"), (String) jsonMap.get(i).get("url"));
        }
    }
}
