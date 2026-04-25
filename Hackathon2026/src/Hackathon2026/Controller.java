package Hackathon2026;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label label;

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
    }
}
