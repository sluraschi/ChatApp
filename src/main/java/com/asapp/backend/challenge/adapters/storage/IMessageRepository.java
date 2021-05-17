package com.asapp.backend.challenge.adapters.storage;

import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.useCases.IMessageManager;

import java.net.ConnectException;
import java.util.List;

public interface IMessageRepository {
    Message sendMessage(IMessageManager.SendMessageRequest sendMessageRequest) throws ConnectException;

    Message findMessageById(int startMessageID);

    List<Message> getMessagesFromRecipientInARange(IMessageManager.GetMessagesRequest getMessagesRequest);
}
