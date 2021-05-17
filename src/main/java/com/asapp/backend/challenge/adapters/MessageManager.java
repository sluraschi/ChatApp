package com.asapp.backend.challenge.adapters;

import com.asapp.backend.challenge.adapters.storage.IMessageRepository;
import com.asapp.backend.challenge.adapters.storage.IUserRepository;
import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.entities.MessageContent;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.InexistentMessageException;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.MessageContentInvalidException;
import com.asapp.backend.challenge.domain.useCases.IMessageManager;

import java.net.ConnectException;
import java.util.List;

public class MessageManager implements IMessageManager {

    IMessageRepository messageRepository;
    IUserRepository userRepository;

    public MessageManager(IMessageRepository messageRepository, IUserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message sendMessage(SendMessageRequest sendMessageRequest) throws MessageContentInvalidException, InexistentUserException, ConnectException {
        Message message;
        User sender;
        User receiver;

        if(!MessageContent.isValid(sendMessageRequest.getMessageContent()))
            throw new MessageContentInvalidException();

        sender = userRepository.findUserById(sendMessageRequest.getSender());
        receiver = userRepository.findUserById(sendMessageRequest.getRecipient());

        if(sender == null || receiver == null)
            throw new InexistentUserException();

        try{
            message = messageRepository.sendMessage(sendMessageRequest);
        } catch (NullPointerException e) {
            throw new ConnectException("Communication to Db failed");
        }
        return message;
    }

    @Override
    public List<Message> getMessages(GetMessagesRequest getMessagesRequest) throws InexistentMessageException, InexistentUserException, ConnectException {
        User receiver;
        Message startMessage;
        List<Message> messages;

        startMessage = messageRepository.findMessageById(getMessagesRequest.getStartMessageID());
        receiver = userRepository.findUserById(getMessagesRequest.getReceiver());

        if(receiver == null)
            throw new InexistentUserException();

        if(startMessage == null)
            throw new InexistentMessageException();

        try{
            messages = messageRepository.getMessagesFromRecipientInARange(getMessagesRequest);
        } catch (NullPointerException e) {
            throw new ConnectException("Communication to Db failed");
        }
        return messages;
    }
}
