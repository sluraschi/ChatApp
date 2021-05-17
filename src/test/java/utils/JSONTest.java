package utils;

import com.asapp.backend.challenge.domain.entities.MessageContent;
import com.asapp.backend.challenge.resources.MessageContentResource;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.JSONUtil;
import com.asapp.backend.challenge.utils.Views;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONTest {

    private JSONUtil jsonUtil;

    @BeforeEach
    public void setup(){
        ObjectMapper objectMapper = new ObjectMapper();
        this.jsonUtil = new JSONUtil(objectMapper);
    }

    @Test
    public void userResourcesParsesToJsonCorrectly(){
        //Given
        int id = 0;
        String username = "username";
        String password = "password";
        UserResource u = new UserResource(username, password, id);
        String expected = "{ \"id\" : " + id + " }";

        //When
        String result = jsonUtil.dataToJson(u, Views.addUserSerialization.class);

        //Then
        Assertions.assertEquals(expected.trim().replaceAll("\\s+", " "), result.trim().replaceAll("\\s+", " "));
    }

    @Test
    public void messageResourceToSendMessageParsesToJsonCorrectly(){
        //Given
        int messageID = 0;
        int senderID = 1;
        int recipientID = 2;
        MessageContentResource messageContentResource = new MessageContentResource("text", "text content");

        String date = "2020-05-20T08:25:22z";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'z'");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            Assertions.fail();
        }
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        MessageResource u = new MessageResource(messageID, senderID, recipientID, messageContentResource, timestamp);

        String expected = "{ \"id\" : " + messageID +
                ", \"timestamp\" : \"" + date +
                "\" }";

        //When
        String result = jsonUtil.dataToJson(u, Views.sendMessageSerialization.class);

        //Then
        Assertions.assertEquals(expected.trim().replaceAll("\\s+", " "), result.trim().replaceAll("\\s+", " "));    }
}
