package exam.q4_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Java7Example {

    public static void main(String[] args) {
        List<Dish> dishList = Dish.getMenuList();

        List<Dish> results = new ArrayList<>();
        for (Dish dish: dishList) {
            if (dish.getCalories() < 400) {
                results.add(dish);
            }
        }

        Collections.sort(results, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return o1.getCalories() - o2.getCalories();
            }
        });

        results.forEach(System.out::println);
    }
}
