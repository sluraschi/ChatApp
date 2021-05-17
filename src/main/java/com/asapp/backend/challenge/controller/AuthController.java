package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.adapters.AuthManager;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.InvalidCredentialsException;
import com.asapp.backend.challenge.domain.useCases.IAuthManager;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.JSONUtil;
import com.asapp.backend.challenge.utils.Views;
import spark.*;

import java.io.IOException;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class AuthController {

    JSONUtil jsonUtil;
    AuthManager authManager;

    public AuthController(AuthManager authManager, JSONUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
        this.authManager = authManager;
    }

    public String login(Request req, Response resp){
        Map<String, Object> pair = null;
        UserResource userResource = null;

        try{
            userResource = (UserResource) this.jsonUtil.jsonToData(req.body(), Views.loginDeserialization.class, UserResource.class);
        } catch (IOException e){
            Spark.halt(400, "Invalid request");
        }

        try {
            pair = this.authManager.login(new IAuthManager.LoginUserRequest(userResource.getUsername(), userResource.getPassword()));
        } catch (InexistentUserException | InvalidCredentialsException e) {
            Spark.halt(401, "Credentials does not match to any user in our registry.");
        } catch (ConnectException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            Spark.halt(500, "Internal error, please try again.");
        }

        userResource.setId((Integer) pair.get("id"));
        userResource.setJwt((String) pair.get("token"));
        return jsonUtil.dataToJson(userResource, Views.loginSerialization.class);
    }
}
