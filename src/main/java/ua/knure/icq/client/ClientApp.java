package ua.knure.icq.client;

import ua.knure.icq.client.model.ClientConnection;

import java.util.Scanner;

public class ClientApp {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("[Launching ICQ Client]");

        ClientConnection connection = new ClientConnection();

        if (!connection.connect(SERVER_HOST, SERVER_PORT)) {
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Type message or /exit to quit.");

            while (true) {
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("/exit")) {
                    System.out.println("Closing client...");
                    break;
                }

                connection.sendLine(message);
            }
        } finally {
            connection.disconnect();
        }
    }
}