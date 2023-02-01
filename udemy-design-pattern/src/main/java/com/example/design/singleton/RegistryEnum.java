package com.example.design.singleton;

public enum RegistryEnum {

    INSTANCE;

    public static RegistryEnum getInstance() {
        return INSTANCE;
    }
}
