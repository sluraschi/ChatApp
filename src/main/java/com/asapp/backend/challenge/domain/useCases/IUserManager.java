package com.asapp.backend.challenge.domain.useCases;

import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.domain.exceptions.UserInvalidException;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IUserManager {
    User addUser (CreateUserRequest userRequest) throws UserAlreadyExistsException, UserInvalidException, InvalidKeySpecException, NoSuchAlgorithmException, ConnectException;

    class CreateUserRequest{
        String username;
        String password;
//        int id;

        public CreateUserRequest(String username, String password) {
            this.username = username;
            this.password = password;
//            this.id = id;
        }

//        public int getId() {
//            return id;
//        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
