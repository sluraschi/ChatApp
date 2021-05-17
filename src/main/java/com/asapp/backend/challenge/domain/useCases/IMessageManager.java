package com.asapp.backend.challenge.domain.useCases;

import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.entities.MessageContent;
import com.asapp.backend.challenge.domain.exceptions.InexistentMessageException;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.MessageContentInvalidException;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;

public interface IMessageManager {

    Message sendMessage(SendMessageRequest sendMessageRequest) throws MessageContentInvalidException, InexistentUserException, ConnectException;
    List<Message> getMessages(GetMessagesRequest getMessagesRequest) throws InexistentMessageException, InexistentUserException, ConnectException;

    class SendMessageRequest {
        final int sender;
        final int recipient;
        final MessageContent messageContent;

        public SendMessageRequest(int sender, int recipient, MessageContent content) {
            this.sender = sender;
            this.recipient = recipient;
            this.messageContent = content;
        }

        public SendMessageRequest(int sender, int recipient, String type, String text) {
            this.sender = sender;
            this.recipient = recipient;
            this.messageContent = new MessageContent(type, text);
        }

        public int getSender() {
            return sender;
        }

        public int getRecipient() {
            return recipient;
        }

        public MessageContent getMessageContent() {
            return messageContent;
        }
    }

    class GetMessagesRequest {
        final int receiver;
        final int startMessageID;
        final int limit;

        public GetMessagesRequest(int receiver, int startMessageID, int limit) {
            this.receiver = receiver;
            this.startMessageID = startMessageID;
            this.limit = limit;
        }

        public int getReceiver() {
            return receiver;
        }

        public int getStartMessageID() {
            return startMessageID;
        }

        public int getLimit() {
            return limit;
        }
    }
}
