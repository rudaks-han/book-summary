package com.example.solid.ocp.after;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Subscriber {

    protected Long subscriberId;

    protected String address;

    protected Long phoneNumber;

    protected int baseRate;

    public abstract double calculateBill();
}
