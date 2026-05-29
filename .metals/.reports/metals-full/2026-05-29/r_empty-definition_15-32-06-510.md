error id: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java:java/net/Socket#
file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
empty definition using pc, found symbol in pc: java/net/Socket#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1001
uri: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
text:
```scala
package ua.knure.icq.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;


public class ServerApp {
    private static final int PORT = 12345;

    public static void main(String[] args){
        System.out.println("[Launching ICQ Server]");
        try (ServerSocket serverSocket = bindServerSocket(PORT)) {
            System.out.println("ICQ server started on port " + PORT + ".");
            System.out.println("Waiting for clients...");
            serverSocket.accept();
        } catch (IOException exception) {
            System.out.println("Failed to start ICQ server on port " + PORT + ".");
            exception.printStackTrace();
        }
    }

    private static ServerSocket bindServerSocket(int port) throws IOException {
        System.out.println("Binding server socket to port " + port + "...");
        ServerSocket serverSocket = new ServerSocket(port);
        return serverSocket;
    }

    private static @@Socket acceptSocket(ServerSocket serverSocket) throws IOException {
        

    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/net/Socket#