package com.example.designpattern.factorymethod;

import com.example.designpattern.factorymethod.message.Message;
import com.example.designpattern.factorymethod.message.TextMessage;

public class TextMessageCreator extends MessageCreator {
    @Override
    public Message createMessage() {
        return new TextMessage();
    }
}
