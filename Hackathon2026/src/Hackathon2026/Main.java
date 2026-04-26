package Hackathon2026;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application{

    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTopic.fxml"));

        loader.setController(new Controller());

        Parent root = loader.load();

        primaryStage.setTitle("hello hackathon2026");
        primaryStage.setScene(new Scene(root, 400, 300));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
        try
        {
            ChatServer.initialize();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
