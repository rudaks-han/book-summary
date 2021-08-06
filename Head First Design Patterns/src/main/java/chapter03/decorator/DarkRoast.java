package chapter03.decorator;

public class DarkRoast extends Beverage {

    public DarkRoast() {
        description = "dark roast";
    }

    @Override
    protected double cost() {
        return 2.99;
    }
}
