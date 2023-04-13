package com.example.design.factorymethod.message;

public class JSONMessage extends Message {

    @Override
    public String getContent() {
        return "{\"JSON\":[]}";
    }
}
