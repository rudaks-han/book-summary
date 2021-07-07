package org.example.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import sun.jvm.hotspot.debugger.Page;

public class AppleFinder {

    private static List<Apple> filterByColor(List<Apple> apples, String color) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple: apples) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }

        return result;
    }

    private static List<Apple> filterByWeight(List<Apple> apples, int weight) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple: apples) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }

        return result;
    }

    private static List<Apple> filterByColorAndWeight(List<Apple> apples, String color,  int weight) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple: apples) {
            if (apple.getColor().equals(color) && apple.getWeight() > weight) {
                result.add(apple);
            }
        }

        return result;
    }

    /*private static List<Apple> filter(List<Apple> apples, ApplePredicate predicate) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple: apples) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }

        return result;
    }*/

    private static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();

        for (T t: list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
            new Apple("red", 10),
            new Apple("green", 20),
            new Apple("yellow", 30)
        );

        // 색깔로 찾기
        /*List<Apple> apples1 = filterByColor(apples, "red");
        apples1.stream().map(Apple::getColor).forEach(System.out::println);*/

        // 무게로 찾기
        List<Apple> apples2 = filterByWeight(apples, 11);
        apples2.stream().map(Apple::getColor).forEach(System.out::println);

        /*List<Apple> apples1 = filter(apples, new AppleColorPredicate());
        List<Apple> apples3 = filter(apples, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getColor().equals("red");
            }
        });
*/
        List<Apple> apples4 = filter(apples, (Apple apple) -> apple.getColor().equals("red"));

    }
}
