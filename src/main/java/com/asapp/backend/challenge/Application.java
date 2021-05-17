package com.asapp.backend.challenge;

import com.asapp.backend.challenge.adapters.AuthManager;
import com.asapp.backend.challenge.adapters.MessageManager;
import com.asapp.backend.challenge.adapters.UserManager;
import com.asapp.backend.challenge.adapters.storage.db.MessageRepository;
import com.asapp.backend.challenge.adapters.storage.db.UserRepository;
import com.asapp.backend.challenge.controller.AuthController;
import com.asapp.backend.challenge.controller.HealthController;
import com.asapp.backend.challenge.controller.MessagesController;
import com.asapp.backend.challenge.controller.UsersController;
import com.asapp.backend.challenge.filter.TokenValidatorFilter;
import com.asapp.backend.challenge.utils.JSONUtil;
import com.asapp.backend.challenge.utils.PasswordHasher;
import com.asapp.backend.challenge.utils.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sql2o.Sql2o;
import spark.Spark;

public class Application {

    private static String SALT_PROPERTY_NAME = "salt";
    private static String ISSUER_PROPERTY_NAME = "issuer";
    private static String AUTH_KEY_PROPERTY_NAME = "AuthKey";

    public static void main(String[] args) {
        // Spark Configuration
        Spark.port(8080);

        Sql2o dbConnection = getDbConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JSONUtil jsonUtil = new JSONUtil(objectMapper);
        PasswordHasher passwordHasher = new PasswordHasher(getProperty(SALT_PROPERTY_NAME).getBytes());

        UserRepository userRepository = new UserRepository(dbConnection);
        MessageRepository messageRepository = new MessageRepository(dbConnection);

        UserManager userManager = new UserManager(userRepository, passwordHasher);
        MessageManager messageManager = new MessageManager(messageRepository, userRepository);
        AuthManager authManager = new AuthManager(getProperty(AUTH_KEY_PROPERTY_NAME), getProperty(ISSUER_PROPERTY_NAME),
                passwordHasher, userRepository);

        System.out.println("Initializing Controllers...");
        UsersController usersController = new UsersController(userManager, jsonUtil);
        MessagesController messagesController = new MessagesController(messageManager, jsonUtil);
        AuthController authController = new AuthController(authManager, jsonUtil);
        HealthController healthController = new HealthController();
        TokenValidatorFilter tokenValidatorFilter = new TokenValidatorFilter(authManager);

        // Configure Endpoints
        // Users
        Spark.post(Path.USERS, usersController::createUser);
        // Auth
        Spark.post(Path.LOGIN, authController::login);
        // Messages
        Spark.before(Path.MESSAGES, tokenValidatorFilter::validateUser);
        Spark.post(Path.MESSAGES, messagesController::sendMessage);
        Spark.get(Path.MESSAGES, messagesController::getMessages);
        // Health
        Spark.post(Path.HEALTH, healthController::check);
        System.out.println("Listening at port 8080...");
    }

    private static Sql2o getDbConnection() {
        Sql2o conn = null;

        try {
            String currentDir = System.getProperty("user.dir");
            String dbDir = getProperty("dbAddress");
            System.out.println("Connecting to db at: " + currentDir + dbDir);
            conn = new Sql2o("jdbc:sqlite:" + currentDir + dbDir, null, null);
        } catch (Exception e) {
            System.out.println("Failed to establish connection with the database");
        }

        return conn;
    }

    private static String getProperty(String propertyName) {
        String property = System.getProperty(propertyName);
        if (property == null) {
            property = System.getenv(propertyName);
        }
        return property;
    }
}