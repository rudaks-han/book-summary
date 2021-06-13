package org.eternity.movie.step01;

import org.eternity.money.Money;

public class NoneDiscountPolicy extends DiscountPolicy{
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
