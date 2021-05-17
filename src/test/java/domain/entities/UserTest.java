package domain.entities;

import com.asapp.backend.challenge.domain.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void ShouldReturnFalseIfUsernameIsEmpty(){
        //Given
        String username = "";
        String password = "password";

        //When
        boolean actual = User.isValid(username, password);

        //Then
        Assertions.assertFalse(actual);
    }

    @Test
    public void ShouldReturnFalseIfPasswordIsEmpty(){
        //Given
        String username = "username";
        String password = "";

        //When
        boolean actual = User.isValid(username, password);

        //Then
        Assertions.assertFalse(actual);
    }

    @Test
    public void ShouldReturnTrueIfUsernameAndPasswordAreValid(){
        //Given
        String username = "username";
        String password = "password";

        //When
        boolean actual = User.isValid(username, password);

        //Then
        Assertions.assertTrue(actual);
    }
}
