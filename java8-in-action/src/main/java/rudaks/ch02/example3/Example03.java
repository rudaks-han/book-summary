package rudaks.ch02.example3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example03 {
    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if (flag && color.equals(apple.getColor()) ||
                    (!flag && apple.getWeight() > weight)) {
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

        List<Apple> result = filterApples(apples, "red", 10, true);
        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

        List<Apple> result2 = filterApples(apples, "red", 10, false);
        result2.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

    }
}
