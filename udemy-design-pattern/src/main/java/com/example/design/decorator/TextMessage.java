package com.example.design.decorator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextMessage implements Message {

    private String msg;

    @Override
    public String getContent() {
        return msg;
    }
}
