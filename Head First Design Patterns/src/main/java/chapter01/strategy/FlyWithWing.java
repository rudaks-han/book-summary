package chapter01.strategy;

public class FlyWithWing implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("날고 있어요");
    }
}
