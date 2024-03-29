package org.eternity.movie.step03;

import java.time.LocalDateTime;

import org.eternity.money.Money;

public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getWhenScreened() {
        return this.whenScreened;
    }

    public int getSequence() {
        return sequence;
    }
}
