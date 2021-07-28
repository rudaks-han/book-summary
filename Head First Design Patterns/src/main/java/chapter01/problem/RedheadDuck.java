package chapter01.problem;

public class RedheadDuck extends Duck {
    @Override
    void quack() {
        System.out.println("No Quack");
    }

    @Override
    void swim() {
        System.out.println("No Swim");
    }

    @Override
    void display() {
        System.out.println("Redhead Duck");
    }

    @Override
    void fly() {
        System.out.println("no swim...?? ");
    }
}
