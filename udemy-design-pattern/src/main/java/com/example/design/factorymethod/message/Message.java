package com.example.design.factorymethod.message;

/**
 * 이 클래스는 Message에 대한 인터페이스를 나타낸다.
 * 구현부에서 contentType을 명시할 것이다.
 */
public abstract class Message {

    public abstract String getContent();

    public void addDefaultHeaders() {
    }

    public void encrypt() {
    }
}
