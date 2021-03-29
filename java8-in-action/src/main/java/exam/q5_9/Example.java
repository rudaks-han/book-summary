package exam.q5_9;

import exam.q4_1.Dish;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Dish> dishes = Dish.getMenuList();

        int count = dishes.stream().map(d -> 1).reduce(0, (x, y) -> x + y);
        System.out.println(count);
    }
}
