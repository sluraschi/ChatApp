package com.asapp.backend.challenge.domain.entities;

import java.sql.Timestamp;
import java.util.Date;

public class Message {

    int id;
    int sender;
    int receiver;
    MessageContent content;
    Timestamp timestamp;

    public Message(int id, int sender, int receiver, MessageContent content, Timestamp timestamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public MessageContent getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }
}
