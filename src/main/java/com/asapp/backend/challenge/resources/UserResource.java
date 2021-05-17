package com.asapp.backend.challenge.resources;

import com.asapp.backend.challenge.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class UserResource {

    @JsonView(Views.addUserDeserialization.class)
    private String username;
    @JsonView(Views.addUserDeserialization.class)
    private String password;
    @JsonView({Views.addUserSerialization.class,
               Views.loginSerialization.class})
    private int id;
    @JsonView(Views.loginSerialization.class)
    private String jwt;

    public UserResource(){}

    public UserResource(String user, String pass, int id) {
        this.username = user;
        this.password = pass;
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
