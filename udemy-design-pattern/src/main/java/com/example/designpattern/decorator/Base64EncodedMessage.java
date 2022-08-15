package com.example.designpattern.decorator;

import java.util.Base64;

public class Base64EncodedMessage implements Message {

    private Message message;

    public Base64EncodedMessage(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return Base64.getEncoder().encodeToString(message.getContent().getBytes());
    }
}
