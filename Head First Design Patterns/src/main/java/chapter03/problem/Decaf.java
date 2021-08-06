package chapter03.problem;

public class Decaf extends Beverage {

    @Override
    public String getDescription() {
        return "this is decaf";
    }

    @Override
    public double cost() {
        return 20;
    }

    @Override
    protected boolean hasMilk() {
        return false;
    }

    @Override
    protected boolean hasSoy() {
        return false;
    }

    @Override
    protected boolean hasMocha() {
        return false;
    }

    @Override
    protected boolean hasWhip() {
        return false;
    }
}
