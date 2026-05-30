package ua.knure.icq.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ua.knure.icq.client.model.LoginModel;

public class LoginController {
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

        System.out.println("Server IP: " + loginModel.getServerIp());
        System.out.println("Username: " + loginModel.getUsername());

        // Пізніше тут буде підключення до сервера і відкриття chat-view.fxml
    }

    private void handleCancel() {
        cancelButton.getScene().getWindow().hide();
    }
}