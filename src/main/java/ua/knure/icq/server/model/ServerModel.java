package ua.knure.icq.server.model;

import ua.knure.icq.common.Message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServerModel {
    private Map<String, List<String>> conversations;

    public ServerModel() {
        this.conversations = new LinkedHashMap<>();
    }

    public synchronized void addMessage(Message message) {
        String conversationName = createConversationName(message.getSender(), message.getReceiver());
        conversations.putIfAbsent(conversationName, new ArrayList<>());
        String messageLine = message.getSender() + ": " + message.getText();
        conversations.get(conversationName).add(messageLine);
    }

    public synchronized List<String> getConversationNames() {
        return new ArrayList<>(conversations.keySet());
    }

    public synchronized List<String> getMessagesForConversation(String conversationName) {
        if (!conversations.containsKey(conversationName)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(conversations.get(conversationName));
    }

    public String createConversationName(String firstUser, String secondUser) {
        if (firstUser.compareToIgnoreCase(secondUser) <= 0) {
            return firstUser + " <--> " + secondUser;
        }
        return secondUser + " <--> " + firstUser;
    }
}