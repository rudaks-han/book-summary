package com.example.solid.srp.before;

import java.util.HashMap;
import java.util.Map;

public class Store {

    private static final Map<String, User> STORAGE = new HashMap<>();

    public void store(User user) {
        synchronized (STORAGE) {
            STORAGE.put(user.getName(), user);
        }
    }

    public User getUser(String name) {
        synchronized (STORAGE) {
            return STORAGE.get(name);
        }
    }
}
