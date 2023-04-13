package com.example.design.prototype;

import lombok.ToString;

// General은 clone을 지원하지 않는다.
@ToString
public class General extends GameUnit {

    private String state = "idle";

    public void boostMorale() {
        this.state = "MoralBoost";
    }

    @Override
    protected GameUnit clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Ganerals are unique");
    }

    @Override
    protected void reset() {
        throw new UnsupportedOperationException("Reset not supported");
    }
}
