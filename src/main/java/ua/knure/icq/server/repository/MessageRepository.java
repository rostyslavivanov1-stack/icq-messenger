package ua.knure.icq.server.repository;

import ua.knure.icq.common.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    private static final String DATABASE_URL = "jdbc:sqlite:icq_messenger.db";

    public MessageRepository() {
        createMessagesTable();
    }

    private void createMessagesTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS messages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT NOT NULL,
                    receiver TEXT NOT NULL,
                    text TEXT NOT NULL,
                    time TEXT NOT NULL
                );
                """;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to create messages table.");
            exception.printStackTrace();
        }
    }

    public synchronized void saveMessage(Message message) {
        String sql = """
                INSERT INTO messages (sender, receiver, text, time)
                VALUES (?, ?, ?, ?);
                """;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, message.getSender());
            statement.setString(2, message.getReceiver());
            statement.setString(3, message.getText());
            statement.setString(4, LocalDateTime.now().toString());

            statement.executeUpdate();

        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to save message.");
            exception.printStackTrace();
        }
    }

    public synchronized List<String> getConversationNames() {
        List<String> conversationNames = new ArrayList<>();

        String sql = """
                SELECT DISTINCT sender, receiver
                FROM messages;
                """;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String sender = resultSet.getString("sender");
                String receiver = resultSet.getString("receiver");

                String conversationName = createConversationName(sender, receiver);

                if (!conversationNames.contains(conversationName)) {
                    conversationNames.add(conversationName);
                }
            }

        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to load conversation names.");
            exception.printStackTrace();
        }

        return conversationNames;
    }

    public synchronized List<Message> getConversationMessages(String firstUser, String secondUser) {
        List<Message> messages = new ArrayList<>();

        String sql = """
                SELECT sender, receiver, text
                FROM messages
                WHERE (sender = ? AND receiver = ?)
                   OR (sender = ? AND receiver = ?)
                ORDER BY time ASC;
                """;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, firstUser);
            statement.setString(2, secondUser);
            statement.setString(3, secondUser);
            statement.setString(4, firstUser);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message(
                        resultSet.getString("sender"),
                        resultSet.getString("receiver"),
                        resultSet.getString("text")
                );

                messages.add(message);
            }

        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to load conversation messages.");
            exception.printStackTrace();
        }

        return messages;
    }

    public String createConversationName(String firstUser, String secondUser) {
        if (firstUser.compareToIgnoreCase(secondUser) <= 0) {
            return firstUser + " <--> " + secondUser;
        }

        return secondUser + " <--> " + firstUser;
    }
}