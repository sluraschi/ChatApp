package com.asapp.backend.challenge.adapters.storage;

import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.useCases.IUserManager;

import java.net.ConnectException;

public interface IUserRepository {
    User addUser(IUserManager.CreateUserRequest userRequest) throws ConnectException;

    User findUserByUsername(String username) throws ConnectException;

    User findUserById(int user) throws ConnectException;
}
