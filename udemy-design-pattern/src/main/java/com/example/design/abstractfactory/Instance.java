package com.example.design.abstractfactory;

// 추상 product
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
