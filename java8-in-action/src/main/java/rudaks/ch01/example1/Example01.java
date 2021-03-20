package rudaks.ch01.example1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example01 {
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("green"),
                new Apple("yellow"),
                new Apple("red")
        );

        List<Apple> result = filterGreenApples(apples);

        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
