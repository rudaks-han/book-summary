package chapter03.decorator;

public class Espresso extends Beverage {

    public Espresso() {
        description = "espresso";
    }

    @Override
    protected double cost() {
        return 1.99;
    }
}
