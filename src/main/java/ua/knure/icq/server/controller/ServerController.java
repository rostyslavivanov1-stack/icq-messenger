package ua.knure.icq.server.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ua.knure.icq.common.Message;
import ua.knure.icq.common.XmlProtocol;
import ua.knure.icq.server.model.ServerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {
    private static final int PORT = 12345;

    @FXML
    private Label selectedConversationLabel;

    @FXML
    private ListView<String> conversationMessagesListView;

    @FXML
    private ListView<String> conversationsListView;

    private ServerModel serverModel;

    @FXML
    private void initialize() {
        serverModel = new ServerModel();
        conversationsListView.setOnMouseClicked(event -> handleConversationSelection());
        startServer();
    }

    private void startServer() {
        Thread serverThread = new Thread(this::runServer);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void runServer() {
        System.out.println("[Launching ICQ Server]");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("ICQ server started on port " + PORT + ".");
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                startClientThread(clientSocket);
            }
        } catch (IOException exception) {
            System.out.println("[ERROR] Failed to start ICQ server on port " + PORT + ".");
            exception.printStackTrace();
        }
    }

    private void startClientThread(Socket clientSocket) {
        Thread clientThread = new Thread(() -> handleClient(clientSocket));
        clientThread.setDaemon(true);
        clientThread.start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Server accepted client connection.");
            String clientMessage = input.readLine();
            while (clientMessage != null) {
                processClientMessage(clientMessage);
                clientMessage = input.readLine();
            }
            System.out.println("Client disconnected.");
        } catch (IOException exception) {
            System.out.println("[ERROR] Error while working with client.");
            exception.printStackTrace();
        } finally {
            closeClientSocket(clientSocket);
        }
    }

    private void processClientMessage(String clientMessage) {
        Message message = XmlProtocol.fromXml(clientMessage);
        if (message == null) {
            System.out.println("[ERROR] Server received invalid XML message.");
            return;
        }
        serverModel.addMessage(message);
        Platform.runLater(() -> {
            updateConversationsList();
            String selectedConversation = conversationsListView.getSelectionModel().getSelectedItem();
            String messageConversation = serverModel.createConversationName(
                    message.getSender(),
                    message.getReceiver()
            );
            if (selectedConversation != null && selectedConversation.equals(messageConversation)) {
                showConversationMessages(selectedConversation);
            }
        });
    }

    private void updateConversationsList() {
        String selectedConversation = conversationsListView.getSelectionModel().getSelectedItem();
        conversationsListView.getItems().setAll(serverModel.getConversationNames());
        if (selectedConversation != null) {
            conversationsListView.getSelectionModel().select(selectedConversation);
        }
    }

    private void handleConversationSelection() {
        String selectedConversation = conversationsListView.getSelectionModel().getSelectedItem();
        if (selectedConversation == null) {
            return;
        }
        selectedConversationLabel.setText(selectedConversation);
        showConversationMessages(selectedConversation);
    }

    private void showConversationMessages(String conversationName) {
        conversationMessagesListView.getItems().clear();
        for (String messageLine : serverModel.getMessagesForConversation(conversationName)) {
            conversationMessagesListView.getItems().add(messageLine);
        }
    }

    private void closeClientSocket(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException exception) {
            System.out.println("[ERROR] Error while closing client socket.");
            exception.printStackTrace();
        }
    }
}