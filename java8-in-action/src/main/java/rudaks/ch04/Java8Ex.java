package rudaks.ch04;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Java8Ex {
    public static void main(String[] args) {
        // 400 calories 미만을 구하고 calories로 정렬하라.

        List<Dish> lowCaloriesDishes = Dish.getMenuList().stream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .collect(Collectors.toList());

        lowCaloriesDishes.stream().forEach(System.out::println);
    }
}
