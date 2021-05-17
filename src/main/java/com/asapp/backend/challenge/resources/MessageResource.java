package com.asapp.backend.challenge.resources;

import com.asapp.backend.challenge.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.sql.Timestamp;

public class MessageResource {
    @JsonView(Views.sendMessageSerialization.class)
    int id;
    @JsonView(Views.sendMessageSerialization.class)
    Timestamp timestamp;
    @JsonView({Views.getMessageSerialization.class,
               Views.sendMessageDeserialization.class})
    int sender;
    @JsonView({Views.getMessageSerialization.class,
               Views.sendMessageDeserialization.class})
    int recipient;
    @JsonView({Views.getMessageSerialization.class,
               Views.sendMessageDeserialization.class})
    MessageContentResource content;

    public MessageResource(int id, int sender, int recipient, MessageContentResource content, Timestamp timestamp) {
        this.id = id;
        this.timestamp = timestamp;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public MessageResource() {
    }

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public MessageContentResource getContentResource() {
        return content;
    }

    public void setContentResource(MessageContentResource content) {
        this.content = content;
    }
}
