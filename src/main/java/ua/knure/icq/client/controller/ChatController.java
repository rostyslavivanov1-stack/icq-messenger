package ua.knure.icq.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ua.knure.icq.client.model.ChatModel;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label receiverLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> usersListView;

    @FXML
    private TextArea messagesArea;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    private ChatModel chatModel;
    private List<String> allUsers;

    @FXML
    private void initialize() {
        allUsers = new ArrayList<>();

        sendButton.setOnAction(event -> handleSendMessage());
        usersListView.setOnMouseClicked(event -> handleUserSelection());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));

        addTemporaryUsers();
    }

    public void setChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
        usernameLabel.setText(chatModel.getUsername());
    }

    private void addTemporaryUsers() {
        allUsers.add("Alex");
        allUsers.add("Maria");
        usersListView.getItems().setAll(allUsers);
    }

    private void filterUsers(String searchText) {
        usersListView.getItems().clear();

        if (searchText == null || searchText.isBlank()) {
            usersListView.getItems().setAll(allUsers);
            return;
        }

        String searchTextLower = searchText.toLowerCase();
        for (String user : allUsers) {
            if (user.toLowerCase().contains(searchTextLower)) {
                usersListView.getItems().add(user);
            }
        }
    }

    private void handleUserSelection() {
        String selectedUser = usersListView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            return;
        }

        receiverLabel.setText(selectedUser);

        if (chatModel != null) {
            chatModel.setSelectedReceiver(selectedUser);
        }

        messagesArea.clear();
        messagesArea.appendText("Chat with " + selectedUser + "\n\n");
    }

    private void handleSendMessage() {
        if (chatModel == null) {
            System.out.println("[ERROR] Chat model is not initialized.");
            return;
        }

        String receiver = chatModel.getSelectedReceiver();
        String text = messageField.getText();

        if (receiver == null || receiver.isBlank()) {
            System.out.println("[ERROR] Please select a user.");
            return;
        }

        if (text == null || text.isBlank()) {
            System.out.println("[ERROR] Message text is empty.");
            return;
        }

        chatModel.sendMessage(receiver, text);

        messagesArea.appendText("Me: " + text + "\n");
        messageField.clear();
    }
}