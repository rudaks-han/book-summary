package rudaks.ch04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Java7Ex {
    public static void main(String[] args) {
        // 400 calories 미만을 구하고 calories로 정렬하라.

        List<Dish> lowCaloriesDishes = new ArrayList<>();
        for (Dish dish: Dish.getMenuList()) {
            if (dish.getCalories() < 400) {
                lowCaloriesDishes.add(dish);
            }
        }

        Collections.sort(lowCaloriesDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });

        for (Dish dish: lowCaloriesDishes) {
            System.out.println(dish.toString());
        }
    }
}
