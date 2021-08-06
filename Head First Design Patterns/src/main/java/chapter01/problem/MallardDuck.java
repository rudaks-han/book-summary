package chapter01.problem;

public class MallardDuck extends Duck {
    @Override
    void quack() {
        System.out.println("Quack");
    }

    @Override
    void swim() {
        System.out.println("Swim");
    }

    @Override
    void display() {
        System.out.println("MallardDuck");
    }

    @Override
    void fly() {

    }
}
