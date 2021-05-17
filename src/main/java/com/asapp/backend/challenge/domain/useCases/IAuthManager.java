package com.asapp.backend.challenge.domain.useCases;

import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.InvalidCredentialsException;
import com.asapp.backend.challenge.resources.UserResource;
import io.jsonwebtoken.Claims;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

public interface IAuthManager {

    HashMap<String, Object> login(LoginUserRequest loginUserRequest) throws InexistentUserException, InvalidCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException, ConnectException;

    String createJWT(int id, String issuer, String subject, long ttlMillis);

    boolean validClaims(Claims claims) throws ConnectException;

    Claims decodeJWT(String jwt);

    class LoginUserRequest extends UserResource {
        private String username;
        private String password;

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        public LoginUserRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
