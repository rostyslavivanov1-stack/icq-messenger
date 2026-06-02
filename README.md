# ICQ Messenger

JavaFX client-server application for instant messaging.
The project implements an ICQ-like messenger with its own server, graphical client interface, graphical server interface, XML-based message protocol, online user list, message forwarding, and server-side message history stored in SQLite.

## Features

* JavaFX client application
* JavaFX server application
* Login window with server IP and username
* Chat window with online users list
* Real-time message exchange between clients
* All messages are transmitted through the custom server
* Server displays all user conversations
* Custom XML-based message protocol
* SQLite database for storing server-side message history
* Conversation history remains available on the server after restart
* Maven project structure
* Separate MVC-style structure for client and server

## Technologies

* Java
* JavaFX
* Maven
* Sockets
* XML
* SQLite
* FXML
* JavaFX CSS

## Project Structure

```text
src/main/java/ua/knure/icq/
├── client/
│   ├── ClientApp.java
│   ├── controller/
│   │   ├── LoginController.java
│   │   └── ChatController.java
│   └── model/
│       ├── LoginModel.java
│       ├── ChatModel.java
│       └── ClientConnection.java
│
├── server/
│   ├── ServerApp.java
│   ├── controller/
│   │   └── ServerController.java
│   ├── model/
│   │   └── ServerModel.java
│   └── repository/
│       └── MessageRepository.java
│
└── common/
    ├── Message.java
    └── XmlProtocol.java
```

```text
src/main/resources/ua/knure/icq/
├── client/view/
│   ├── login-view.fxml
│   └── chat-view.fxml
└── server/view/
    └── server-view.fxml
```

```text
icq-messenger/
├── pom.xml
├── README.md
├── .gitignore
└── src/
```

## How to Run

### 1. Run the server

Open a terminal in the project root folder and run:

```bash
mvn javafx:run -Pserver
```

The server window will open and start listening on port `12345`.

### 2. Run the first client

Open another terminal and run:

```bash
mvn javafx:run -Pclient
```

Enter:

```text
Server IP: localhost
User name: Nick
```

Click `Connect`.

### 3. Run the second client

Open one more terminal and run:

```bash
mvn javafx:run -Pclient
```

Enter:

```text
Server IP: localhost
User name: Ben
```

Click `Connect`.

### 4. Test messaging

* Nick should see Ben in the online users list.
* Ben should see Nick in the online users list.
* Select a user from the list.
* Type a message.
* Click `Send`.

The message will be sent through the server and delivered to the selected user.

## Server Behavior

The server accepts client connections through sockets.
After connection, each client sends a login message with its username.
The server stores active clients in memory and sends the online users list to all connected clients.

When a message is received, the server:

1. Parses the XML message.
2. Saves it to SQLite.
3. Updates the server conversation view.
4. Finds the receiver among active clients.
5. Forwards the message to the receiver.

## Database

The project uses SQLite for server-side message history.

The database file is created automatically:

```text
icq_messenger.db
```

This file is ignored by Git because it contains runtime data.

## Message Protocol

Messages between client and server are transmitted as XML strings.
The `Message` object is converted to XML before sending and parsed back after receiving.

Example message structure:

```xml
<message>
    <sender>Nick</sender>
    <receiver>Ben</receiver>
    <text>Hello!</text>
</message>
```

Service messages are also used for internal communication, for example:

```text
LOGIN:Nick
USER_LIST:Nick,Ben
```

## Notes

The client stores local conversation history only while the client application is running.
The server stores all received messages in SQLite, so server-side conversation history is preserved after restart.

## Author

Rostislav Ivanov
