package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.exceptions.InexistentMessageException;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.MessageContentInvalidException;
import com.asapp.backend.challenge.domain.useCases.IMessageManager;
import com.asapp.backend.challenge.resources.GetMessageResource;
import com.asapp.backend.challenge.resources.MessageContentResource;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.utils.Views;
import com.asapp.backend.challenge.utils.JSONUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.util.LinkedList;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Spark;

public class MessagesController {

    IMessageManager messageManager;
    JSONUtil jsonUtil;

    public MessagesController(IMessageManager messageManager, JSONUtil jsonUtil) {
        this.messageManager = messageManager;
        this.jsonUtil = jsonUtil;
    }

    public String sendMessage(Request req, Response rep){
        MessageResource messageResource = null;
        Message message = null;

        try{
            messageResource = (MessageResource) this.jsonUtil.jsonToData(req.body(), Views.sendMessageDeserialization.class, MessageResource.class);
        } catch (IOException e){
            Spark.halt(400, "Invalid body request");
        }

        try {
            message = this.messageManager.sendMessage(  new IMessageManager.SendMessageRequest(messageResource.getSender(),
                                                        messageResource.getRecipient(),
                                                        messageResource.getContentResource().getType(),
                                                        messageResource.getContentResource().getText()));
        }catch(MessageContentInvalidException e) {
            Spark.halt(400, "Message content is not of a valid type.");
        }catch(InexistentUserException e) {
            Spark.halt(400, "Nonexistent user in the request.");
        } catch (ConnectException e) {
            Spark.halt(500, "Storage is offline, please try again in a couple of minutes.");
        }

        messageResource.setId(message.getId());
        messageResource.setTimestamp(message.getTimestamp());

        return jsonUtil.dataToJson(messageResource, Views.sendMessageSerialization.class);
    }

    public String getMessages(Request req, Response rep){
        List<Message> messages = new LinkedList<>();
        List<MessageResource> messageResources = new LinkedList<>();
        GetMessageResource getMessageResource = null;

        try{
            getMessageResource = (GetMessageResource) this.jsonUtil.jsonToData(req.body(), Views.getMessageDeserialization.class, GetMessageResource.class);
        } catch (IOException e){
            Spark.halt(400, "Invalid body request");
        }

        try {
            messages = this.messageManager.getMessages( new IMessageManager.GetMessagesRequest(getMessageResource.getRecipient(),
                                                        getMessageResource.getStart(),
                                                        getMessageResource.getLimit()));
        }catch(InexistentMessageException e) {
            Spark.halt(400, "Nonexistent message in the request.");
        }catch(InexistentUserException e) {
            Spark.halt(400, "Nonexistent user in the request.");
        } catch (ConnectException e) {
            Spark.halt(500, "Storage is offline, please try again in a couple of minutes.");
        }

        for (Message m : messages){
            messageResources.add(new MessageResource(m.getId(), m.getSender(), m.getReceiver(),
                                 new MessageContentResource(m.getContent().getType(), m.getContent().getText()), m.getTimestamp()));
        }

        getMessageResource.setMessages(messageResources);
        return jsonUtil.dataToJson(getMessageResource, Views.getMessageSerialization.class);
    }
}
