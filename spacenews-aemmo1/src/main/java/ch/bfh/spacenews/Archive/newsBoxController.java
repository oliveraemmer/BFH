package ch.bfh.spacenews;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class newsBoxController {

    @FXML public Label viewTitle;
    @FXML public VBox vBox;

    DataAdmin admin = DataAdmin.getInstance();
    ObjectMapper mapper = new ObjectMapper();
    List<Map<String, Object>> jsonMap;

    public newsBoxController() {
    }

    public void initialize() throws IOException {
        admin.getEntries().clear();

        URL url = new URL("https://api.spaceflightnewsapi.net/v3/" + admin.getType());
        jsonMap = mapper.readValue(url, new TypeReference<List<Map<String,Object>>>(){});
        for(int i = 0; i < 9; i++) {
            //admin.addEntry((int) jsonMap.get(i).get("id"), (String) jsonMap.get(i).get("title"), (String) jsonMap.get(i).get("summary"));
        }

        viewTitle.setText(admin.getType());
        for (int i = 0; i < admin.getEntries().size(); i++) {
            Text title = new Text((String) admin.getEntries().get(i).getTitle());
            title.setFont(Font.font ("Verdana", 15));
            Text summary = new Text((String) admin.getEntries().get(i).getSummary() + "\n");
            summary.setFont(Font.font ("Verdana", 11));
            TextFlow tfSummary = new TextFlow(summary);
            vBox.getChildren().add(title);
            vBox.getChildren().add(tfSummary);
        }
    }

}
