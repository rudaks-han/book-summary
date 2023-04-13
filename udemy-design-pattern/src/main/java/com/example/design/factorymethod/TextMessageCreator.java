package com.example.design.factorymethod;

import com.example.design.factorymethod.message.Message;
import com.example.design.factorymethod.message.TextMessage;

public class TextMessageCreator extends MessageCreator {
    @Override
    public Message createMessage() {
        return new TextMessage();
    }
}
