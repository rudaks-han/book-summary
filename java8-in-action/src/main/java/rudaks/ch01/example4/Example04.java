package rudaks.ch01.example4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example04 {
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if (applePredicate.test(apple)) {
                result.add(apple);
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

        List<Apple> result = filterApples(apples, new AppleWeightPredicate());
        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

        List<Apple> result2 = filterApples(apples, new AppleColorPredicate());
        result2.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
