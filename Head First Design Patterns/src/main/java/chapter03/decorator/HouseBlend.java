package chapter03.decorator;

public class HouseBlend extends Beverage {

    public HouseBlend() {
        description = "house blend";
    }

    @Override
    protected double cost() {
        return .89;
    }
}
