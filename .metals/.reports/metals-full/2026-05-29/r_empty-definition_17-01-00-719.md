error id: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java:_empty_/message#
file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
empty definition using pc, found symbol in pc: _empty_/message#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1929
uri: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
text:
```scala
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
        try (ServerSocket serverSocket = bindServerSocket(PORT)) {
            System.out.println("ICQ server started on port " + PORT + ".");
            try {
                Socket clientSocket = acceptSocket(serverSocket);
                System.out.println("Server accepted client connection.");
                handleClient(clientSocket);
            } catch (IOException exception) {
                System.out.println("Error while working with client.");
                exception.printStackTrace();
            }
        } catch (IOException exception) {
            System.out.println("Failed to start ICQ server on port " + PORT + ".");
            exception.printStackTrace();
        }
    }

    private static ServerSocket bindServerSocket(int port) throws IOException {
        System.out.println("Binding server socket to the port " + port + "...");
        ServerSocket serverSocket = new ServerSocket(port);
        return serverSocket;
    }

    private static Socket acceptSocket(ServerSocket serverSocket) throws IOException {
        System.out.println("Waiting for clients connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.getInetAddress());
        return clientSocket;
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String clientMessage = input.readLine();
        while (messa@@ge != null) {
            System.out.println("Client: " + message);
            message = input.readLine();
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/message#