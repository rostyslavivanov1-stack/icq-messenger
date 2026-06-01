package ua.knure.icq.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ua.knure.icq.client.model.ChatModel;
import javafx.application.Platform;
import ua.knure.icq.common.Message;
import ua.knure.icq.common.XmlProtocol;
import java.util.HashMap;
import java.util.Map;
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
    private Map<String, List<String>> conversations;

    @FXML
    private void initialize() {
        allUsers = new ArrayList<>();
        conversations = new HashMap<>();

        sendButton.setOnAction(event -> handleSendMessage());
        usersListView.setOnMouseClicked(event -> handleUserSelection());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    public void setChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
        usernameLabel.setText(chatModel.getUsername());
        startReceiveThread();
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

        showConversation(selectedUser);
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
        addMessageToConversation(receiver, "Me: " + text);
        showConversation(receiver);
        messageField.clear();
    }

    private void startReceiveThread() {
        Thread receiveThread = new Thread(() -> {
            while (chatModel != null && chatModel.isConnected()) {
                String line = chatModel.receiveLine();

                if (line == null) {
                    break;
                }

                if (line.startsWith("USER_LIST:")) {
                    Platform.runLater(() -> updateUserList(line));
                } else {
                    Message message = XmlProtocol.fromXml(line);

                    if (message != null) {
                        Platform.runLater(() -> handleIncomingMessage(message));
                    }
                }
            }
        });

        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    private void updateUserList(String userListLine) {
        allUsers.clear();

        String usersText = userListLine.substring("USER_LIST:".length());

        if (!usersText.isBlank()) {
            String[] users = usersText.split(",");

            for (String user : users) {
                String trimmedUser = user.trim();

                if (!trimmedUser.isBlank() && !trimmedUser.equals(chatModel.getUsername())) {
                    allUsers.add(trimmedUser);
                }
            }
        }

        usersListView.getItems().setAll(allUsers);
    }

    private void handleIncomingMessage(Message message) {
        String sender = message.getSender();
        String messageLine = sender + ": " + message.getText();

        addMessageToConversation(sender, messageLine);
        String selectedReceiver = chatModel.getSelectedReceiver();

        if (selectedReceiver != null && selectedReceiver.equals(message.getSender())) {
            showConversation(sender);
        } else {
            System.out.println("New message from " + message.getSender() + ": " + message.getText());
        }
    }

    private void addMessageToConversation(String user, String messageLine) {
        conversations.putIfAbsent(user, new ArrayList<>());
        conversations.get(user).add(messageLine);
    }

    private void showConversation(String user) {
        messagesArea.clear();
        messagesArea.appendText("Chat with " + user + "\n\n");

        List<String> conversationMessages = conversations.get(user);

        if (conversationMessages == null) {
            return;
        }

        for (String messageLine : conversationMessages) {
            messagesArea.appendText(messageLine + "\n");
        }
    }
}