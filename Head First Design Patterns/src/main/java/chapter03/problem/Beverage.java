package chapter03.problem;

import lombok.Getter;

@Getter
public abstract class Beverage {

    private String description;

    protected double cost() {
        double sum = 5;
        if (hasMilk()) {
            sum += 10;
        }

        if (hasSoy()) {
            sum += 20;
        }

        if (hasMocha()) {
            sum += 30;
        }

        if (hasWhip()) {
            sum += 40;
        }
        return sum;
    }

    protected abstract boolean hasMilk();

    protected abstract boolean hasSoy();

    protected abstract boolean hasMocha();

    protected abstract boolean hasWhip();


}
