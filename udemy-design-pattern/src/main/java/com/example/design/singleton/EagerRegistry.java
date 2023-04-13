package com.example.design.singleton;

public class EagerRegistry {

    private EagerRegistry() {

    }

    private static final EagerRegistry INSTANCE = new EagerRegistry();

    public static final EagerRegistry getInstance() {
        return INSTANCE;
    }
}
