package ua.knure.icq.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    public static void main(String[] args) {
        System.out.println("[Launching ICQ Client]");
        try (Socket socket = connectToServer(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Connected to ICQ server.");
            handleUserInput(socket);
        } catch (IOException exception) {
            System.out.println("Client error.");
            exception.printStackTrace();
        }
    }

    private static Socket connectToServer(String host, int port) throws IOException {
        System.out.println("Connecting to ICQ server " + host + ":" + port + "...");
        return new Socket(host, port);
    }

    private static void handleUserInput(Socket socket) throws IOException {
            try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)) {
                System.out.println("Type message or /exit to quit.");
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("/exit")) {
                        System.out.println("Closing client...");
                    break;
                }
                output.println(message);
            }
        }
    }
}