package com.example.design.factorymethod;

import com.example.design.factorymethod.message.JSONMessage;
import com.example.design.factorymethod.message.Message;

public class JSONMessageCreator extends MessageCreator {
    @Override
    public Message createMessage() {
        return new JSONMessage();
    }
}
