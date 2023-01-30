package com.example.solid.ocp.before;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneSubscriber {

    private Long subscriberId;

    private String address;

    private Long phoneNumber;

    private int baseRate;

    public double calculateBill() {
        List<CallHistory.Call> sessions = CallHistory.getCurrentCalls(subscriberId);
        long totalDuration = sessions.stream().mapToLong(CallHistory.Call::getDuration).sum();
        return totalDuration * baseRate / 100;
    }
}
