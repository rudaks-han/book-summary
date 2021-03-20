package rudaks.ch02.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example02 {
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if (color.equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if (apple.getWeight() > weight) {
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

        List<Apple> result = filterApplesByColor(apples, "red");
        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

        List<Apple> result2 = filterApplesByWeight(apples, 20);
        result2.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
