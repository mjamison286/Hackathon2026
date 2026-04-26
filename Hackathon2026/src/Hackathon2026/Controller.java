package Hackathon2026;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.stage.*;


import org.json.*;

public class Controller {

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView imageViewer;

    @FXML
    private TextField promptField;

    @FXML
    private Button inputButton;

    @FXML
    private Text responseTextOutput;
        
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    //Created with heavy help from tutorial by mintype
    @FXML
    void AskAI()
    {
        String modelName = "llama3.1";
        String promptInput = promptField.getText().trim();

        try
        {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            System.out.println("PromptInput: " + promptInput);

            if(promptInput != null)
            {
                String jsonInputString = String.format("{\"model\": \"%s\", \"prompt\":\"%s\", \"stream\": false}", modelName, promptInput);

                try (OutputStream os = conn.getOutputStream())
                {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8); 
                    os.write(input, 0, input.length);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                int code = conn.getResponseCode();

                System.out.println("Response Code is " + code);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();

                String line;
                while((line = in.readLine()) != null)
                {
                    response.append(line);
                }
                
                in.close();

                System.out.println("Response Body: " + response.toString());

                JSONObject jsonResponse = new JSONObject(response.toString());
                String responseText = jsonResponse.getString("response");

                responseTextOutput.setText(responseText);

                System.out.println(responseText);
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTopic.fxml"));
            switchScenes(loader);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void switchScenes(FXMLLoader ld)
    {
        try
        {
            ld.setController(new Controller());

            Parent root = ld.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            
            stage.setTitle("hello hackathon2026");
            stage.setScene(new Scene(root, 400, 300));

            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initialize()
    {
        /*
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        label.setText("JavaFX Version: " + javafxVersion + " and Java Version: " + javaVersion);
        
        Image imageToSet = new Image(new File("assets\\image\\media-wallpaper-full3.jpg").toURI().toString());
        imageViewer.setImage(imageToSet);
        */
    }
}
