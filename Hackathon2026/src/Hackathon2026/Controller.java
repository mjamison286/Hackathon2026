package Hackathon2026;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.*;;

public class Controller {

    @FXML
    private Label label;

    @FXML
    private ImageView imageViewer;

    @FXML
    void test(ActionEvent actionEvent)
    {
        //whatever with actionevent here
    }

    public void initialize()
    {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        label.setText("JavaFX Version: " + javafxVersion + " and Java Version: " + javaVersion);

        Image imageToSet = new Image(new File("assets\\image\\media-wallpaper-full3.jpg").toURI().toString());
        imageViewer.setImage(imageToSet);
    }
}
