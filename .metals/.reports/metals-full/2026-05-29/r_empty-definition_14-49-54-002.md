error id: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java:java/io/IOException#
file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
empty definition using pc, found symbol in pc: java/io/IOException#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 356
uri: file:///D:/Java%20ICQ%20Project/icq-messenger/src/main/java/ua/knure/icq/server/ServerApp.java
text:
```scala
package ua.knure.icq.server;

import java.net.ServerSocket;
import java.io.IOException;


public class ServerApp {
    private static final int PORT = 12345;

    public static void main(String[] args){
        System.out.println("Launching ICQ Server...");
    }

    private static ServerSocket createServerSocket(int port) throws IOExceptio@@n {
        return new ServerSocket(port);
    }


    }

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/io/IOException#