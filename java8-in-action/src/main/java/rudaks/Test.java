package rudaks;

import java.util.Optional;
import java.util.function.Predicate;

import rudaks.ch02.example1.Apple;
import rudaks.ch08.Task;

public class Test {

    public static void something(Runnable r) {
        r.run();
    }

    public static void something(Task task) {
        task.execute();
    }

    public static void main(String[] args) {

        Apple apple = null;
        Optional<Apple> optionalApple = Optional.ofNullable(apple);

        System.out.println(optionalApple);
    }
}

