package com.example.solid.ocp.before;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ISPSubscriber {

    private Long subscriberId;

    private String address;

    private Long phoneNumber;

    private int baseRate;

    private long freeUsage;

    public double calculateBill() {
        List<InternetSessionHistory.InternetSession> sessions = InternetSessionHistory.getCurrentSessions(subscriberId);
        long totalData = sessions.stream().mapToLong(InternetSessionHistory.InternetSession::getDataUsed).sum();
        long chargeableData = totalData - freeUsage;
        return chargeableData * baseRate / 100;
    }
}
