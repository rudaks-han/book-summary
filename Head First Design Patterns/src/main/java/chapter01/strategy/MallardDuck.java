package chapter01.strategy;

public class MallardDuck extends Duck {

    public MallardDuck() {
        flyBehavior = new FlyWithWing();
        quackBehavior = new Quack();
    }
}
