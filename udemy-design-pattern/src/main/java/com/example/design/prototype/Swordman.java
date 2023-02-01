package com.example.design.prototype;

import lombok.ToString;

@ToString
public class Swordman extends GameUnit {

    private String state = "idle";

    public void attack() {
        this.state = "attacking";
    }

    @Override
    protected void reset() {
        this.state = "idle";
    }
}
