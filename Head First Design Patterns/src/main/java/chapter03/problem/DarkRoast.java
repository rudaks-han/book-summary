package chapter03.problem;

public class DarkRoast extends Beverage {

    @Override
    public String getDescription() {
        return "this is dark roast";
    }

    @Override
    protected double cost() {
        return super.cost();
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

    public static void main(String[] args) {
        DarkRoast darkRoast = new DarkRoast();
        System.out.println(darkRoast.cost());
    }
}
