package com.asapp.backend.challenge.adapters.storage.entity;

import java.sql.Timestamp;

public class DbMessage {
    int id;
    int sender;
    int receiver;
    String contentType;
    String content;
    Timestamp timestamp;

    public DbMessage(int sender, int receiver, String contentType, String content, Timestamp timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.contentType = contentType;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
