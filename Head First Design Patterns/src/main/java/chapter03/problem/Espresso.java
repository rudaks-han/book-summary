package chapter03.problem;

public class Espresso extends Beverage {

    @Override
    public String getDescription() {
        return "this is espresso";
    }

    @Override
    public double cost() {
        return 10;
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
