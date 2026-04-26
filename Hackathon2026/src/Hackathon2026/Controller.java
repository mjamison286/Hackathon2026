package Hackathon2026;

import java.io.*;
import java.net.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
/*
    @FXML
    private Label label;
*/
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
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize()
    {
        try {
            Socket socket = new Socket("localhost", 5000);

            in =new BufferedReader( new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> listenForMessage()).start();

            chatArea.appendText("Connected to the server \n");

            
        } catch (IOException e) {
            chatArea.appendText("Could not connect to the sever");
            e.printStackTrace();
        }
    }
}
