package com.example.design.abstractfactory;

public interface Instance {

    void start();

    void addStorage(Storage storage);

    void stop();

    enum Capacity {
        micro,
        small,
        large
    }
}
