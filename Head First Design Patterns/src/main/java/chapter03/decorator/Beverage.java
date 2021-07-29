package chapter03.decorator;

import lombok.Getter;

@Getter
public abstract class Beverage {

    public String description = "기본";

    protected abstract double cost();
}
