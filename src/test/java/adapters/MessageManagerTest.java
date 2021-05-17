package adapters;

import com.asapp.backend.challenge.adapters.MessageManager;
import com.asapp.backend.challenge.adapters.storage.db.MessageRepository;
import com.asapp.backend.challenge.adapters.storage.db.UserRepository;
import com.asapp.backend.challenge.domain.entities.Message;
import com.asapp.backend.challenge.domain.entities.MessageContent;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.MessageContentInvalidException;
import com.asapp.backend.challenge.domain.useCases.IMessageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MessageManagerTest {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private MessageManager messageManager;

    @BeforeEach
    public void setup() {
        this.messageRepository = mock(MessageRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.messageManager = new MessageManager(messageRepository, userRepository);
    }

    @Test
    public void InvalidMessageContentTShouldRiseMessageContentInvalidException() {
        //Given
        int senderID = 0;
        int receiverID = 1;
        MessageContent messageContent = new MessageContent("other_type", "text");
        IMessageManager.SendMessageRequest messageRequest = new IMessageManager.SendMessageRequest(senderID, receiverID, messageContent);

        //When - Then
        Assertions.assertThrows(MessageContentInvalidException.class, () -> messageManager.sendMessage(messageRequest) );
    }

    @Test
    public void MessageRequestWithInexistentUserIDShouldRiseInexistentUserException() throws ConnectException {
        //Given
        int senderID = 0;
        int receiverID = 1;
        MessageContent messageContent = new MessageContent("text", "textContent");
        IMessageManager.SendMessageRequest messageRequest = new IMessageManager.SendMessageRequest(senderID, receiverID, messageContent);
        when(this.userRepository.findUserById(senderID)).thenReturn(null);

        //When - Then
        Assertions.assertThrows(InexistentUserException.class, () -> messageManager.sendMessage(messageRequest) );
    }

    @Test
    public void validMessageIsAddedCorrectly() throws MessageContentInvalidException, InexistentUserException, ConnectException {
        //Given
        int senderID = 0;
        int receiverID = 1;
        MessageContent messageContent = new MessageContent("text", "textContent");
        IMessageManager.SendMessageRequest messageRequest = new IMessageManager.SendMessageRequest(senderID, receiverID, messageContent);
        when(this.userRepository.findUserById(anyInt())).thenReturn(new User("user", "aHash", 0));
        when(this.messageRepository.sendMessage(messageRequest)).thenReturn(new Message(10, senderID, receiverID, messageContent, new Timestamp(new Date().getTime())));

        //When
        Message newMessage = this.messageManager.sendMessage(messageRequest);

        //Then
        Assertions.assertNotNull(newMessage);
        Assertions.assertEquals(senderID, newMessage.getSender());
        Assertions.assertEquals(receiverID, newMessage.getReceiver());
        Assertions.assertEquals(messageContent.getText(), newMessage.getContent().getText());

    }
}
