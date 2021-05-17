package adapters;

import com.asapp.backend.challenge.adapters.AuthManager;
import com.asapp.backend.challenge.adapters.storage.db.UserRepository;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.InvalidCredentialsException;
import com.asapp.backend.challenge.domain.useCases.IAuthManager;
import com.asapp.backend.challenge.utils.PasswordHasher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthManagerTest {

    private final String testSecretKey = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK923";

    private AuthManager authManager;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    @BeforeEach
    public void setup(){
        this.userRepository = mock(UserRepository.class);
        this.passwordHasher = new PasswordHasher("unsalt".getBytes());
        this.authManager = new AuthManager(testSecretKey, "issuer", passwordHasher, userRepository);
    }

    @Test
    public void createAndDecodeJWT() {
        //Given
        int jwtId = 1234;
        String jwtIssuer = "ChallengeIssuer";
        String jwtSubject = "Charles Xavier";
        int jwtTimeToLive = 800000;

        //When
        String jwt = authManager.createJWT(
                jwtId, // claim = jti
                jwtIssuer, // claim = iss
                jwtSubject, // claim = sub
                jwtTimeToLive // used to calculate expiration (claim = exp)
        );

        Claims claims = authManager.decodeJWT(jwt);

        //Then
        Assertions.assertEquals(String.valueOf(jwtId), claims.getId());
        Assertions.assertEquals(jwtIssuer, claims.getIssuer());
        Assertions.assertEquals(jwtSubject, claims.getSubject());
    }


    @Test
    public void decodeShouldFail() {
        //Given
        String notAJwt = "This is not a JWT";

        //When - Then
        Assertions.assertThrows(MalformedJwtException.class, () -> authManager.decodeJWT(notAJwt) );
    }

    @Test
    public void userNotRegisteredThrowsInexistentUserException(){
        //Given
        String username = "Charles";
        String password = "";
        IAuthManager.LoginUserRequest loginUserRequest = new IAuthManager.LoginUserRequest(username, password);

        //When-Then
        Assertions.assertThrows(InexistentUserException.class, () -> authManager.login(loginUserRequest));
    }

    @Test
    public void wrongPasswordThrowsInvalidCredentialsException() throws ConnectException {
        //Given
        String username = "Charles";
        String password = "pass";
        String invalidPassword = "invalidPass";
        when(userRepository.findUserByUsername(username)).thenReturn(new User(username, password, 0));

        IAuthManager.LoginUserRequest loginUserRequest = new IAuthManager.LoginUserRequest(username, invalidPassword);

        //When-Then
        Assertions.assertThrows(InvalidCredentialsException.class, () -> authManager.login(loginUserRequest));
    }
}
