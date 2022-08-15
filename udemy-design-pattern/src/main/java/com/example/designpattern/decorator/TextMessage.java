package com.example.designpattern.decorator;

// 구체적인 컴포넌트. 데코레이터로 싸여질 객체
public class TextMessage implements Message {

    private String message;

    public TextMessage(String message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return message;
    }
}
