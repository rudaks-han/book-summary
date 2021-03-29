package exam.q4_1;

import java.util.Comparator;
import java.util.List;

public class Java8Example {

    public static void main(String[] args) {
        List<Dish> dishList = Dish.getMenuList();

        dishList.stream()
            .filter(dish -> dish.getCalories() < 400)
            .sorted(Comparator.comparingInt(Dish::getCalories))
            .forEach(System.out::println);
    }
}
