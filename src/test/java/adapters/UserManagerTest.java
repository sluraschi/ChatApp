package adapters;

import com.asapp.backend.challenge.adapters.UserManager;
import com.asapp.backend.challenge.adapters.storage.db.UserRepository;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.domain.exceptions.UserInvalidException;
import com.asapp.backend.challenge.domain.useCases.IUserManager;
import com.asapp.backend.challenge.utils.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserManagerTest {

    private UserManager userManager;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    @BeforeEach
    public void setup() {
        this.userRepository = mock(UserRepository.class);
        this.passwordHasher = mock(PasswordHasher.class);
        this.userManager = new UserManager(this.userRepository, this.passwordHasher);
    }

    @Test
    public void InvalidUsernameInRequestShouldRiseUserInvalidException() {
        //Given
        IUserManager.CreateUserRequest userRequest = new IUserManager.CreateUserRequest("", "pass");

        //When - Then
        Assertions.assertThrows(UserInvalidException.class, () -> userManager.addUser(userRequest));
    }

    @Test
    public void InvalidPasswordInRequestShouldRiseUserInvalidException() {
        //Given
        IUserManager.CreateUserRequest userRequest = new IUserManager.CreateUserRequest("user", "");

        //When - Then
        Assertions.assertThrows(UserInvalidException.class, () -> userManager.addUser(userRequest));
    }

    @Test
    public void DuplicateUsernameShouldRiseUserAlreadyExistsException() throws ConnectException {
        //Given
        String username = "CharlesXavier";
        String password = "unHash";
        IUserManager.CreateUserRequest userRequest = new IUserManager.CreateUserRequest(username, password);
        when(this.userRepository.findUserByUsername(username)).thenReturn(new User(username, "otroHash", 1));

        //When - Then
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userManager.addUser(userRequest));
    }

    @Test
    public void ValidUserRequestShouldReturnUserCreated() throws UserInvalidException, UserAlreadyExistsException, InvalidKeySpecException, NoSuchAlgorithmException, ConnectException {
        //Given
        String username = "CharlesXavier";
        String password = "greatHash";
        IUserManager.CreateUserRequest userRequest = new IUserManager.CreateUserRequest(username, password);
        when(this.userRepository.findUserByUsername(username)).thenReturn(null);
        when(this.userRepository.addUser(userRequest)).thenReturn(new User(username, password, 1));

        //When
        User newUser = this.userManager.addUser(userRequest);

        //Then
        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(username, newUser.getUsername());
        Assertions.assertEquals(password, newUser.getPassword());
    }
}
