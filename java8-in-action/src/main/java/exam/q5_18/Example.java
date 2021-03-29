package exam.q5_18;

import exam.q4_1.Dish;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Dish> dishes = Dish.getMenuList();
        int calories = dishes.stream().map(Dish::getCalories).reduce((x, y) -> Integer.sum(x, y)).get();

        System.out.println(calories);
    }
}
