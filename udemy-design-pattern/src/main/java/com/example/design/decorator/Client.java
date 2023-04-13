package com.example.design.decorator;

public class Client {

    public static void main(String[] args) {
        Message m = new TextMessage("Type <FORCE> is strong with this one!");
        System.out.println(m.getContent());

        Message decorator = new HtmlEncodedMessage(m);
        System.out.println(decorator.getContent());

        decorator = new Base64EncodedMessage(decorator);
        System.out.println(decorator.getContent());
    }
}
