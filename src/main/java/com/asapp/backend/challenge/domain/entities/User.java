package com.asapp.backend.challenge.domain.entities;

import com.asapp.backend.challenge.domain.useCases.IUserManager;
import com.asapp.backend.challenge.resources.UserResource;

import java.util.UUID;

public class User {

    String username;
    String password;
    int id;

    public static boolean isValid(String username, String pass) {
        return !(username.isEmpty() | pass.isEmpty());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }
}
