package com.geekbrains.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        stage.setTitle("Чат");
        stage.setScene(new Scene(root, 400, 400));
        stage.show();
        ChatController chatController = loader.getController();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                chatController.close();
                Platform.exit();
                System.exit(0);
            }
        });
    }

/*    public static void main(String[] args) {
        MainClient mainClient = new MainClient();
        try {
            mainClient.start(new Stage());
        } catch (IOException e){
            e.printStackTrace();
       }
    }*/
}
