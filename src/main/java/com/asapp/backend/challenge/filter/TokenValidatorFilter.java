package com.asapp.backend.challenge.filter;

import com.asapp.backend.challenge.adapters.AuthManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.ConnectException;

public class TokenValidatorFilter {

    AuthManager authManager;

    public TokenValidatorFilter(AuthManager authManager) {
        this.authManager = authManager;
    }

    public void validateUser (Request req, Response resp) throws ConnectException {
        String token = req.headers("Authorization");
        Claims claims = null;
        try {
            claims = authManager.decodeJWT(token);
        } catch (ExpiredJwtException e) {
            Spark.halt(401, "Invalid token");
        } catch ( IllegalArgumentException e){
            Spark.halt(401, "Missing token");
        }
        if (!authManager.validClaims(claims))
            Spark.halt(401, "Invalid token");
    }
}
