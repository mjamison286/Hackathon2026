package Hackathon2026;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.stage.*;

import org.json.*;

public class Controller {

    public static String currentTopic = "";
    public static int weeklyStreakCount = 0;

    @FXML private Pane rootPane;
    @FXML private ImageView imageViewer;
    @FXML private TextField promptField;
    @FXML private Button inputButton;
    @FXML private Text responseTextOutput;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private CheckBox day1;
    @FXML private CheckBox day2;
    @FXML private CheckBox day3;
    @FXML private CheckBox day4;
    @FXML private CheckBox day5;
    @FXML private CheckBox day6;
    @FXML private CheckBox day7;
    @FXML private ProgressBar progressBar;
    @FXML private ProgressBar weeklyProgressBar;
    @FXML private ProgressBar loadingBar;
    @FXML private Label progressLabel;
    @FXML private Label weeklyLabel;
    @FXML private Label trackerTitle;

    @FXML
    void AskAI() {
        String modelName = "llama3.1";
        String promptInput = promptField.getText().trim();

        if (loadingBar != null) loadingBar.setVisible(true);
        if (responseTextOutput != null) responseTextOutput.setText("Thinking...");

        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                if (promptInput != null) {
                    String jsonInputString = String.format("{\"model\": \"%s\", \"prompt\":\"%s\", \"stream\": false}", modelName, promptInput);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String responseText = jsonResponse.getString("response");

                    Platform.runLater(() -> {
                        responseTextOutput.setText(responseText);
                        if (loadingBar != null) loadingBar.setVisible(false);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (responseTextOutput != null) responseTextOutput.setText("Something went wrong.");
                    if (loadingBar != null) loadingBar.setVisible(false);
                });
            }
        }).start();
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (Database.loginUser(username, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTopic.fxml"));
            switchScenes(loader, usernameField);
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (Database.registerUser(username, password)) {
            errorLabel.setText("Account created! Please log in.");
        } else {
            errorLabel.setText("Username already taken.");
        }
    }

    @FXML
    void handleStartTracking(ActionEvent event) {
        if (promptField != null && !promptField.getText().isEmpty()) {
            currentTopic = promptField.getText();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tracker.fxml"));
        switchScenes(loader, (Node) event.getSource());
    }

    @FXML
    void updateProgress(ActionEvent event) {
        int count = 0;
        if (day1 != null && day1.isSelected()) count++;
        if (day2 != null && day2.isSelected()) count++;
        if (day3 != null && day3.isSelected()) count++;
        if (day4 != null && day4.isSelected()) count++;
        if (day5 != null && day5.isSelected()) count++;
        if (day6 != null && day6.isSelected()) count++;
        if (day7 != null && day7.isSelected()) count++;

        if (progressLabel != null)
            progressLabel.setText("Daily Progress - " + count + "/7");
        if (progressBar != null)
            progressBar.setProgress((double) count / 7);

        if (count == 7) {
            weeklyStreakCount++;
            if (weeklyLabel != null)
                weeklyLabel.setText("Weeks learned " + weeklyStreakCount + "/5");
            if (weeklyProgressBar != null)
                weeklyProgressBar.setProgress((double) weeklyStreakCount / 5);

            day1.setSelected(false);
            day2.setSelected(false);
            day3.setSelected(false);
            day4.setSelected(false);
            day5.setSelected(false);
            day6.setSelected(false);
            day7.setSelected(false);

            if (progressLabel != null)
                progressLabel.setText("Daily Progress - 0/7");
            if (progressBar != null)
                progressBar.setProgress(0);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTopic.fxml"));
        switchScenes(loader, (Node) event.getSource());
    }

    void switchScenes(FXMLLoader ld, Node currentNode) {
        try {
            Parent root = ld.load();
            Stage stage = (Stage) currentNode.getScene().getWindow();
            stage.setTitle("hello hackathon2026");
            stage.setScene(new Scene(root, 640, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        if (trackerTitle != null) {
            String topic = currentTopic.isEmpty() ? "New Interest" : currentTopic;
            trackerTitle.setText("Daily Tracker - " + topic);
        }
        if (weeklyLabel != null) {
            weeklyLabel.setText("Weeks learned " + weeklyStreakCount + "/5");
        }
    }
}