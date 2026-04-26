package Hackathon2026;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // SQLite check will go here later
        // For now just switch scenes if fields aren't empty
        try {
            Parent root = FXMLLoader.load(getClass().getResource("hackathon2026.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 300));
        } catch (Exception e) {
            errorLabel.setText("Something went wrong.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        // Sign up logic will go here later
    }
}