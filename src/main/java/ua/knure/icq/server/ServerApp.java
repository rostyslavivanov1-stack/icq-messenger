package ua.knure.icq.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerApp {
    private static final int PORT = 12345;

    public static void main(String[] args){
        System.out.println("[Launching ICQ Server]");
        ServerSocket serverSocket = bindServerSocket(PORT);
        if (serverSocket == null) {
            System.out.println("[ERROR] Server could not be started.");
            return;
        }
        System.out.println("ICQ server started on port " + PORT + ".");
        while (true) {
            Socket clientSocket = acceptSocket(serverSocket);
            if (clientSocket != null) {
                startClientThread(clientSocket);
            }
        }
    }

    private static ServerSocket bindServerSocket(int port) {
        try {
            System.out.println("Binding server socket to the port " + port + "...");
            return new ServerSocket(port);
        } catch (IOException exception) {
            System.out.println("[ERROR] Failed to start ICQ server on port " + port + ".");
            exception.printStackTrace();
            return null;
        }
    }

    private static Socket acceptSocket(ServerSocket serverSocket) {
        try {
            System.out.println("Waiting for client connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            return clientSocket;
        } catch (IOException exception) {
            System.out.println("[ERROR] Error while accepting client.");
            exception.printStackTrace();
            return null;
        }
    }

    private static void startClientThread(Socket clientSocket) {
        Thread clientHandlerThread = new Thread(() -> handleClient(clientSocket));
        clientHandlerThread.start();
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Server accepted client connection.");
            String clientMessage = input.readLine();
            while (clientMessage != null) {
                System.out.println("Client: " + clientMessage);
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

    private static void closeClientSocket(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException exception) {
            System.out.println("[ERROR] Error while closing client socket.");
            exception.printStackTrace();
        }
    }
}