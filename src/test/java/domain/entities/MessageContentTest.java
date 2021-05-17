package domain.entities;

import com.asapp.backend.challenge.domain.entities.MessageContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageContentTest {

    @Test
    public void ContentTypeDifferentOfTheAllowedDetectedInvalid(){
        String type = "other_type";
        String text = "text";

        boolean actual = MessageContent.isValid(new MessageContent(type, text));

        Assertions.assertFalse(actual);
    }

    @Test
    public void ContentTypeTextIsValid(){
        String type = "text";
        String text = "text";

        boolean actual = MessageContent.isValid(new MessageContent(type, text));

        Assertions.assertTrue(actual);
    }

    @Test
    public void ContentTypeImageIsValid(){
        String type = "image";
        String text = "text";

        boolean actual = MessageContent.isValid(new MessageContent(type, text));

        Assertions.assertTrue(actual);
    }

    @Test
    public void ContentTypeVideoIsValid(){
        String type = "video";
        String text = "text";

        boolean actual = MessageContent.isValid(new MessageContent(type, text));

        Assertions.assertTrue(actual);
    }
}
