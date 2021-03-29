package exam.q5_8;

import exam.q4_1.Dish;

import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<Dish> dishes = Dish.getMenuList();

        int sum = dishes.stream().map(Dish::getCalories).reduce(0, (x, y) -> x + y);

        System.out.println(sum);
    }
}
