package com.asapp.backend.challenge.resources;

import com.asapp.backend.challenge.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class GetMessageResource {
    @JsonView(Views.getMessageDeserialization.class)
    int recipient;
    @JsonView(Views.getMessageDeserialization.class)
    int start;
    @JsonView(Views.getMessageDeserialization.class)
    int limit = 100;
    @JsonView(Views.getMessageSerialization.class)
    List<MessageResource> messages;

    public GetMessageResource(int recipient, int start, int limit, List<MessageResource> messages) {
        this.recipient = recipient;
        this.start = start;
        this.limit = limit;
        this.messages = messages;
    }

    public GetMessageResource() {

    }

    public List<MessageResource> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResource> messages) {
        this.messages = messages;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRecipient() {
        return recipient;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }
}
