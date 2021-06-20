package org.eternity.billing.step05;

import org.eternity.money.Money;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Phone phone0 = new Phone(
                new RegularPolicy(Money.wons(10), Duration.ofSeconds(10))
        );
        phone0.addCall(getCall());

        System.out.println(phone0.calculateFee());

        Phone phone = new Phone(
                new TaxablePolicy(0.05,
                        new RegularPolicy(Money.wons(10), Duration.ofSeconds(10))
                )
        );
        System.out.println(phone.calculateFee());

        Phone phone1 = new Phone(
                new RateDiscountPolicy(Money.wons(1000),
                        new TaxablePolicy(0.5,
                                new RegularPolicy(Money.wons(1000), Duration.ofSeconds(10))))
        );
        System.out.println(phone1.calculateFee());

        Phone phone2 = new Phone(
                new RateDiscountPolicy(Money.wons(1000),
                        new TaxablePolicy(0.05,
                                new NightlyDiscountPolicy(Money.wons(1000), Money.wons(1000), Duration.ofSeconds(10))))
        );
        System.out.println(phone2.calculateFee());
    }

    private static Call getCall() {
        return new Call(
                LocalDateTime.of(2020, 01, 01, 00, 00, 00),
                LocalDateTime.of(2020, 01, 01, 00, 00, 10)
        );
    }
}
