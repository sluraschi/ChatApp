package com.asapp.backend.challenge.domain.entities;

public class MessageContent {

    enum ValidTypes {
        text,
        image,
        video
    }

    String type;
    String text;

    public MessageContent(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public static boolean isValid(MessageContent messageContent) {
        for(ValidTypes valid : ValidTypes.values()){
            if (messageContent.type.toLowerCase().equals(valid.name()))
                return true;
        }
        return false;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
}
