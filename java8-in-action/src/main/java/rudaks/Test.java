package rudaks;

import rudaks.ch02.example1.Apple;

import java.util.Comparator;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        List<Apple> inventory = null;

        inventory.sort((Apple a1, Apple a2) -> a1.getColor().compareTo(a2.getColor()));

        inventory.sort(Comparator.comparing(Apple::getColor));



    }
}

