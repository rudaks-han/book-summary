package com.example.designpattern.factorymethod;

import com.example.designpattern.factorymethod.message.JSONMessage;
import com.example.designpattern.factorymethod.message.Message;

public class JSONMessageCreator extends MessageCreator {
    @Override
    public Message createMessage() {
        return new JSONMessage();
    }
}
