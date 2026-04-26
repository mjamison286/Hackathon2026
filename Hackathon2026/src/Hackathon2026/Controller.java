package Hackathon2026;

import java.io.*;
import java.net.*;
import javafx.fxml.FXML;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

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

    private PrintWriter out;
    private  BufferedReader in;
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private void handleSend() {
        String message = messageField.getText().trim();

        if (!message.isEmpty()) {
            out.println(message);
            messageField.clear();
        }
    }

    private void listenForMessage() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                String finalMsg = msg;

                javafx.application.Platform.runLater(() -> {
                    chatArea.appendText(finalMsg +"\n");
                });
            }
            
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

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

                if (!responseTextOutput.isVisible())
                {
                    responseTextOutput.isVisible();
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void swapToAddTopic()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTopic.fxml"));
        switchScenes(loader);
    }

    @FXML
    void swapToProfile()
    {
        FXMLLoader loader;

        if(!Database.signedIn)
        {
            loader = new FXMLLoader(getClass().getResource("login.fxml"));
        }
        else
        {
            loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        }

        switchScenes(loader);
    }

    @FXML
    void swapToSettings()
    {
        FXMLLoader loader;

        if(!Database.signedIn)
        {
            loader = new FXMLLoader(getClass().getResource("login.fxml"));
        }
        else
        {
            loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        }

        switchScenes(loader);
    }

    @FXML
    void handleLogin() 
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        Database.loginUser(username, password);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hackathon2026.fxml"));
            switchScenes(loader);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSignUp()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        Database.registerUser(username, password);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hackathon2026.fxml"));
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

            double width = rootPane.getScene().getWidth();
            double height = rootPane.getScene().getHeight();
            
            stage.setScene(new Scene(root, width, height));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initialize()
    {
        try {

            if (!Database.initialized)
            {
                Database.initialize();
            }

            if (!ChatServer.initialized)
            {
                CompletableFuture.runAsync(() -> ChatServer.initialize());
            }

            Socket socket = new Socket("localhost", 5000);

            in =new BufferedReader( new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> listenForMessage()).start();
            if (chatArea != null)
            {
                chatArea.appendText("Connected to the server \n");
                System.out.println("Connected to the server");
            }
        } 
        catch (IOException e) 
        {
            if (chatArea != null)
            {
                chatArea.appendText("Could not connect to the server");
                System.out.println("Could not connect to the server");
            }

            e.printStackTrace();
        }
    }
}