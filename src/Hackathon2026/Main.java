package Hackathon2026;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application{

public void start(Stage primaryStage) throws Exception
{
    Database.initialize();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("hello hackathon2026");
    primaryStage.setScene(new Scene(root, 640, 400));
    primaryStage.show();
}

    public static void main(String[] args)
    {
        launch(args);
    }
}