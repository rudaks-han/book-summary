package com.example.design.factorymethod;

import com.example.design.factorymethod.message.Message;

public class Client {

    public static void main(String[] args) {
        printMessage(new JSONMessageCreator());
        printMessage(new TextMessageCreator());
    }

    private static void printMessage(MessageCreator creator) {
        Message message = creator.getMessage();
        System.out.println(message.getContent());
    }
}
