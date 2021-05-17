package com.asapp.backend.challenge.adapters.storage.db;

import com.asapp.backend.challenge.adapters.storage.IMessageRepository;
import com.asapp.backend.challenge.adapters.storage.entity.DbMessage;
import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.entities.MessageContent;
import com.asapp.backend.challenge.domain.useCases.IMessageManager;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;


import java.net.ConnectException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MessageRepository implements IMessageRepository {

    private Sql2o sql2o;

    public MessageRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Message sendMessage(IMessageManager.SendMessageRequest sendMessageRequest) throws ConnectException {
        Timestamp t = new Timestamp(new Date().getTime());
        DbMessage toInsert = new DbMessage(sendMessageRequest.getSender(), sendMessageRequest.getRecipient(), sendMessageRequest.getMessageContent().getType(), sendMessageRequest.getMessageContent().getText(), t);
        int insertedId;
        try (Connection conn = sql2o.open()) {
            insertedId = (int) conn.createQuery("insert into Messages(sender, receiver, contentType, content, timestamp) " +
                    "VALUES (:sender, :receiver, :contentType, :content, :timestamp)", true)
                    .bind(toInsert)
                    .executeUpdate()
                    .getKey();
        }catch (Exception e) {
            throw new ConnectException();
        }

        return new Message(insertedId, toInsert.getSender(), toInsert.getReceiver(), new MessageContent(toInsert.getContentType(), toInsert.getContent()), toInsert.getTimestamp());
    }

    @Override
    public Message findMessageById(int startMessageID) {
        DbMessage messageFound;
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery("SELECT * FROM Messages WHERE id == :id")
                    .addParameter("id", startMessageID);

            messageFound = query.executeAndFetchFirst(DbMessage.class);
        }

        if (messageFound == null)
            return null;
        return new Message(messageFound.getId(), messageFound.getSender(), messageFound.getReceiver(), new MessageContent(messageFound.getContentType(), messageFound.getContent()), messageFound.getTimestamp());
    }

    @Override
    public List<Message> getMessagesFromRecipientInARange(IMessageManager.GetMessagesRequest getMessagesRequest) {
        List<DbMessage> dbMessages;
        LinkedList<Message> messages = new LinkedList<>();
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery("SELECT * FROM Messages Where receiver == :id  AND id >= :start ORDER BY id ASC LIMIT :limit")
                    .addParameter("id", getMessagesRequest.getReceiver())
                    .addParameter("start", getMessagesRequest.getStartMessageID())
                    .addParameter("limit", getMessagesRequest.getLimit());

            dbMessages = query.executeAndFetch(DbMessage.class);
        }

        for (DbMessage ins : dbMessages)
            messages.add(new Message(ins.getId(), ins.getSender(), ins.getReceiver(), new MessageContent(ins.getContentType(), ins.getContent()), ins.getTimestamp()));
        return messages;
    }
}
