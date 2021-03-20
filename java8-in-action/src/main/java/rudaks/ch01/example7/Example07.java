package rudaks.ch01.example7;

import rudaks.ch01.example6.Apple;
import rudaks.ch01.example6.ApplePredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example07 {
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e: list) {
            if (p.test(e)) {
                result.add(e);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("green", 10),
                new Apple("yellow", 20),
                new Apple("red", 30)
        );

        List<Apple> result = filter(apples, (Apple apple) -> "red".equals(apple.getColor()));
        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

    }
}
