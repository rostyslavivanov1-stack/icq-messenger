package ua.knure.icq.client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    public boolean connect(String host, int port) {
        try {
            System.out.println("Connecting to ICQ server " + host + ":" + port + "...");

            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to ICQ server.");
            return true;
        } catch (IOException exception) {
            System.out.println("[ERROR] Failed to connect to ICQ server.");
            exception.printStackTrace();
            return false;
        }
    }

    public void sendLine(String line) {
        if (output == null) {
            System.out.println("[ERROR] Cannot send message: connection is not established.");
            return;
        }
        output.println(line);
    }

    public String receiveLine() {
        if (input == null) {
            System.out.println("[ERROR] Cannot receive message: connection is not established.");
            return null;
        }
        try {
            return input.readLine();
        } catch (IOException exception) {
            System.out.println("[ERROR] Failed to receive message from server.");
            exception.printStackTrace();
            return null;
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void disconnect() {
        closeInput();
        closeOutput();
        closeSocket();
    }

    private void closeInput() {
        if (input != null) {
            try {
                input.close();
            } catch (IOException exception) {
                System.out.println("[ERROR] Failed to close input stream.");
                exception.printStackTrace();
            }
        }
    }

    private void closeOutput() {
        if (output != null) {
            output.close();
        }
    }

    private void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException exception) {
                System.out.println("[ERROR] Failed to close socket.");
                exception.printStackTrace();
            }
        }
    }
}