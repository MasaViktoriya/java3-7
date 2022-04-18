package com.geekbrains.client;

import com.geekbrains.server.ClientHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private TextFlow textFlow;
    @FXML
    private TextField messageField, loginField;
    @FXML
    private HBox messagePanel, authPanel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ListView<String> clientList;

    private final Network network;

    public ChatController() {
        this.network = new Network(this);
    }

    public void setAuthenticated(boolean authenticated){
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        messagePanel.setVisible(authenticated);
        messagePanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAuthenticated(false);
    }

    public void displayMessage(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text textNode = new Text(text);
                if (text.startsWith("PM ")) {
                    textNode.setFill(Color.DARKGREEN);
                    textFlow.getChildren().add(textNode);
                } else {
                    textFlow.getChildren().add(textNode);
                }
            }
            });
        }

    public void displayClient(String nickName){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().add(nickName);
            }
        });
    }

    public void removeClient(String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().remove(nickName);
            }
        });
    }

    public void sendAuth(ActionEvent actionEvent) {
        boolean authenticated = network.sendAuth(loginField.getText(), passwordField.getText());
        if (authenticated) {
            loginField.clear();
            passwordField.clear();
            setAuthenticated(true);
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        String selectedUser = clientList.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            network.sendMessage(messageField.getText());
            messageField.clear();
        }else {
            network.sendMessage("/w " + selectedUser +" "+ messageField.getText());
            messageField.clear();
            clientList.getSelectionModel().clearSelection();
        }
    }

    public void close(){
        network.closeConnection();
    }
}
