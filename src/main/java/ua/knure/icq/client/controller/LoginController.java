package ua.knure.icq.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ua.knure.icq.client.model.ChatModel;
import ua.knure.icq.client.model.LoginModel;

public class LoginController {
    private static final int SERVER_PORT = 12345;

    @FXML
    private TextField serverIpField;

    @FXML
    private TextField usernameField;

    @FXML
    private Button connectButton;

    @FXML
    private Button cancelButton;

    private LoginModel loginModel;

    @FXML
    private void initialize() {
        loginModel = new LoginModel();

        connectButton.setOnAction(event -> handleConnect());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleConnect() {
        loginModel.setServerIp(serverIpField.getText());
        loginModel.setUsername(usernameField.getText());

        if (!loginModel.isValid()) {
            System.out.println("[ERROR] Please enter server IP and user name.");
            return;
        }

        ChatModel chatModel = new ChatModel(loginModel.getUsername(), loginModel.getServerIp(), SERVER_PORT);

        if (!chatModel.connect()) {
            System.out.println("[ERROR] Failed to connect to server.");
            return;
        }
        openChatWindow(chatModel);
    }

    private void openChatWindow(ChatModel chatModel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/knure/icq/client/view/chat-view.fxml"));
            Scene scene = new Scene(loader.load());
            ChatController chatController = loader.getController();
            chatController.setChatModel(chatModel);
            Stage chatStage = new Stage();
            chatStage.setTitle("ICQ Chat - " + chatModel.getUsername());
            chatStage.setScene(scene);
            chatStage.setWidth(1000);
            chatStage.setHeight(720);
            chatStage.show();
            connectButton.getScene().getWindow().hide();

        } catch (Exception exception) {
            System.out.println("[ERROR] Failed to open chat window.");
            exception.printStackTrace();
            chatModel.disconnect();
        }
    }

    private void handleCancel() {
        cancelButton.getScene().getWindow().hide();
    }
}