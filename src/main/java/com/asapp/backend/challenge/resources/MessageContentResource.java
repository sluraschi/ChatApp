package com.asapp.backend.challenge.resources;

import com.asapp.backend.challenge.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class MessageContentResource {
    @JsonView({Views.sendMessageDeserialization.class, Views.getMessageSerialization.class})
    String type;
    @JsonView({Views.sendMessageDeserialization.class, Views.getMessageSerialization.class})
    String text;

    public MessageContentResource() {
    }

    public MessageContentResource(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
