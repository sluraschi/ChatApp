package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.domain.exceptions.UserInvalidException;
import com.asapp.backend.challenge.domain.useCases.IUserManager;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.Views;
import com.asapp.backend.challenge.utils.JSONUtil;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UsersController {

    IUserManager userManager;
    JSONUtil jsonUtil;


    public UsersController(IUserManager userManager, JSONUtil jsonUtil) {
        this.userManager = userManager;
        this.jsonUtil = jsonUtil;
    }

    public String createUser(Request req, Response resp){
        UserResource userResource = null;
        User user = null;
        try{
        userResource = (UserResource) this.jsonUtil.jsonToData(req.body(), Views.addUserDeserialization.class, UserResource.class);
        } catch (IOException e){
            Spark.halt(400, "Invalid request");
        }

        try {
            user = this.userManager.addUser(new IUserManager.CreateUserRequest(userResource.getUsername(), userResource.getPassword()));
        } catch (UserAlreadyExistsException e){
            Spark.halt(400, "User already exists");
        } catch (UserInvalidException e) {
            Spark.halt(400, "Invalid user");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Spark.halt(500, "Internal error, please try again.");
        } catch (ConnectException e) {
            Spark.halt(500, "Storage is offline, please try again in a couple of minutes.");
        }

        userResource.setId(user.getId());
        return jsonUtil.dataToJson(userResource, Views.addUserSerialization.class);
    }
}
