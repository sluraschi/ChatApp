package com.asapp.backend.challenge.adapters.storage.entity;

public class DbUser {
    int id;
    String username;
    String password;

    public DbUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
