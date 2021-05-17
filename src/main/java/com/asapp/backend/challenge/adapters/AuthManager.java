package com.asapp.backend.challenge.adapters;

import com.asapp.backend.challenge.adapters.storage.IUserRepository;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.exceptions.InexistentUserException;
import com.asapp.backend.challenge.domain.exceptions.InvalidCredentialsException;
import com.asapp.backend.challenge.domain.useCases.IAuthManager;

import java.net.ConnectException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;

import com.asapp.backend.challenge.utils.PasswordHasher;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthManager implements IAuthManager {

    private final String secretKey;
    private final String issuer;
    private long ttlMillis = 600000;

    PasswordHasher passwordHasher;
    IUserRepository userRepository;

    public AuthManager(String secret_key, String issuer, PasswordHasher passwordHasher, IUserRepository userRepository) {
        secretKey = secret_key;
        this.issuer = issuer;
        this.passwordHasher = passwordHasher;
        this.userRepository = userRepository;
    }

    @Override
    public String createJWT(int id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Key for the signing
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Claims to add to the token
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(id))
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signingKey);

        //Expiration time
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    @Override
    public Claims decodeJWT(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .build()
                .parseClaimsJws(jwt).getBody();
    }

    @Override
    public HashMap<String, Object> login(LoginUserRequest loginUserRequest) throws InexistentUserException, InvalidCredentialsException, InvalidKeySpecException, NoSuchAlgorithmException, ConnectException {
        User user;

        user = this.userRepository.findUserByUsername(loginUserRequest.getUsername());
        if (user == null)
            throw new InexistentUserException();

        if (!isPasswordValid(loginUserRequest.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException();

        String jwt = createJWT(user.getId(), issuer, user.getUsername(), ttlMillis);
        HashMap<String, Object> returns = new HashMap<>();
        returns.put("id", user.getId());
        returns.put("token", jwt);
        return returns;
    }

    private boolean isPasswordValid(String loginPassword, String storedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return passwordHasher.createHash(loginPassword).equals(storedPassword);
    }

    @Override
    public boolean validClaims(Claims claims) throws ConnectException {
        String iss = claims.getIssuer();
        String sub = claims.getSubject();
        Date exp = claims.getExpiration();

        if(!iss.equals(issuer))
            return false;
        if (new Date().after(exp))
            return false;
        return userRepository.findUserByUsername(sub) != null;
    }
}
