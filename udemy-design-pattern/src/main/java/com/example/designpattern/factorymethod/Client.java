package com.example.designpattern.factorymethod;

import com.example.designpattern.factorymethod.message.Message;

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
