package com.example.design.factorymethod;

import com.example.design.factorymethod.message.Message;

public abstract class MessageCreator {

    public Message getMessage() {
        Message message = createMessage();
        message.addDefaultHeaders();
        message.encrypt();

        return message;
    }

    // 팩토리 메소드
    public abstract Message createMessage();
}
