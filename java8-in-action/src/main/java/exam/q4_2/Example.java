package exam.q4_2;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;

import exam.q4_1.Dish;

public class Example {
    public static void main(String[] args) {
        List<Dish> dishList = Dish.getMenuList();

        Map<Dish.Type, List<Dish>> map = dishList.stream().collect(groupingBy(Dish::getType));

        System.out.println(map);
    }
}
