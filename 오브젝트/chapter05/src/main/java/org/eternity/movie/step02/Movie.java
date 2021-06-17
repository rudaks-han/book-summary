package org.eternity.movie.step02;

import java.time.Duration;
import java.util.List;

import org.eternity.money.Money;

public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    private List<PeriodCondition> periodConditions;
    private List<SequenceCondition> sequenceConditions;

    public Movie(String title, Duration runningTime, Money fee, List<PeriodCondition> periodConditions, List<SequenceCondition> sequenceConditions) {
        this.title = title;
        this.runningTime = runningTime;
        this.periodConditions = periodConditions;
        this.sequenceConditions = sequenceConditions;
    }

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return checkPeriodCondition(screening) || checkSequenceCondition(screening);
    }

    private boolean checkPeriodCondition(Screening screening) {
        return periodConditions.stream().anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    private boolean checkSequenceCondition(Screening screening) {
        return periodConditions.stream().anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    private Money calculateDiscountAmount() {
        switch (movieType) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }

        throw new IllegalArgumentException();
    }

    private Money calculateAmountDiscountAmount() {
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount() {
        return fee.times(discountPercent);
    }

    private Money calculateNoneDiscountAmount() {
        return Money.ZERO;
    }

}
