package ua.knure.icq.server.model;

import ua.knure.icq.common.Message;
import ua.knure.icq.server.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;

public class ServerModel {
    private MessageRepository messageRepository;

    public ServerModel() {
        this.messageRepository = new MessageRepository();
    }

    public synchronized void addMessage(Message message) {
        messageRepository.saveMessage(message);
    }

    public synchronized List<String> getConversationNames() {
        return messageRepository.getConversationNames();
    }

    public synchronized List<String> getMessagesForConversation(String conversationName) {
        String[] users = parseConversationName(conversationName);

        if (users == null) {
            return new ArrayList<>();
        }
        List<Message> messages = messageRepository.getConversationMessages(users[0], users[1]);
        List<String> messageLines = new ArrayList<>();

        for (Message message : messages) {
            String messageLine = message.getSender() + ": " + message.getText();
            messageLines.add(messageLine);
        }

        return messageLines;
    }

    private String[] parseConversationName(String conversationName) {
        if (conversationName == null || conversationName.isBlank()) {
            return null;
        }

        String[] parts = conversationName.split(" <--> ");

        if (parts.length != 2) {
            return null;
        }

        return new String[]{parts[0], parts[1]};
    }

    public String createConversationName(String firstUser, String secondUser) {
        return messageRepository.createConversationName(firstUser, secondUser);
    }
}