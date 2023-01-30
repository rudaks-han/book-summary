package com.example.solid.srp.after;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController {

    private UserPersistenceService persistenceService = new UserPersistenceService();

    public String createUser(String userJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userJson, User.class);

        UserValidator validator = new UserValidator();
        boolean valid = validator.validateUser(user);

        if (!valid) {
            return "ERROR";
        }

        persistenceService.saveUser(user);

        return "SUCCESS";
    }
}
