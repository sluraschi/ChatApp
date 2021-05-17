package com.asapp.backend.challenge.adapters;

import com.asapp.backend.challenge.adapters.storage.IUserRepository;
import com.asapp.backend.challenge.domain.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.domain.exceptions.UserInvalidException;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.useCases.IUserManager;
import com.asapp.backend.challenge.utils.PasswordHasher;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserManager implements IUserManager {

    IUserRepository userRepository;
    PasswordHasher passwordHasher;

    public UserManager(IUserRepository userRepository, PasswordHasher passwordHasher){
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User addUser(CreateUserRequest userRequest) throws UserAlreadyExistsException, UserInvalidException, InvalidKeySpecException, NoSuchAlgorithmException, ConnectException {
        User userAdded;

        //Validate pass not null
        if (!User.isValid(userRequest.getUsername(), userRequest.getPassword()))
            throw new UserInvalidException();

        //Check user is unique
        if(this.userRepository.findUserByUsername(userRequest.getUsername()) != null)
            throw new UserAlreadyExistsException();

        userRequest.setPassword(passwordHasher.createHash(userRequest.getPassword()));

        try{
            userAdded = userRepository.addUser(userRequest);
        } catch (NullPointerException e) {
            throw new ConnectException("Communication to Db failed");
        }
         return userAdded;
    }
}
