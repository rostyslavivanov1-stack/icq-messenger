package ua.knure.icq.common;

import java.time.Instant;

public class Message {
    private final String sender;
    private final String receiver;
    private final String text;
    private final Instant time;

    public Message(String sender, String receiver, String text, Instant time) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.time = time;
    }

    public Message(String sender, String receiver, String text) {
        this(sender, receiver, text, Instant.now());
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public Instant getTime() {
        return time;
    }
}