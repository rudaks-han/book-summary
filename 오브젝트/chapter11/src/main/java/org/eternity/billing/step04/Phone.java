package org.eternity.billing.step04;

import org.eternity.money.Money;

import java.util.ArrayList;
import java.util.List;

public abstract class Phone {
    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(calculateCallFee(call));
        }

        return afterCalculated(result);
    }

    abstract protected Money calculateCallFee(Call call);
    protected Money afterCalculated(Money fee) {
        return fee;
    }
}
