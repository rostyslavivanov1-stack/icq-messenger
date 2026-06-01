package ua.knure.icq.client.model;

import ua.knure.icq.common.Message;
import ua.knure.icq.common.XmlProtocol;

import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private String username;
    private String serverIp;
    private int serverPort;

    private String selectedReceiver;

    private ClientConnection connection;
    private List<Message> messages;

    public ChatModel(String username, String serverIp, int serverPort) {
        this.username = username;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.selectedReceiver = "";
        this.connection = new ClientConnection();
        this.messages = new ArrayList<>();
    }

    public boolean connect() {
        boolean connected = connection.connect(serverIp, serverPort);
        if (!connected) {
            return false;
        }
        connection.sendLine("LOGIN:" + username);
        return true;
    }

    public void sendMessage(String receiver, String text) {
        if (receiver == null || receiver.isBlank()) {
            System.out.println("[ERROR] Receiver is empty.");
            return;
        }

        if (text == null || text.isBlank()) {
            System.out.println("[ERROR] Message text is empty.");
            return;
        }

        Message message = new Message(username, receiver, text);
        String xml = XmlProtocol.toXml(message);

        if (xml.isBlank()) {
            System.out.println("[ERROR] Failed to create XML message.");
            return;
        }

        connection.sendLine(xml);
        messages.add(message);
    }

    public void sendMessage(String text) {
        sendMessage(selectedReceiver, text);
    }

    public Message receiveMessage() {
        String xml = connection.receiveLine();

        if (xml == null) {
            return null;
        }
        Message message = XmlProtocol.fromXml(xml);

        if (message != null) {
            messages.add(message);
        }
        return message;
    }

    public String receiveLine() {
        return connection.receiveLine();
    }

    public void disconnect() {
        connection.disconnect();
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public String getUsername() {
        return username;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getSelectedReceiver() {
        return selectedReceiver;
    }

    public void setSelectedReceiver(String selectedReceiver) {
        this.selectedReceiver = selectedReceiver;
    }

    public List<Message> getMessages() {
        return messages;
    }
}