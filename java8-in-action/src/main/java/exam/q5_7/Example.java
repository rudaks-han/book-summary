package exam.q5_7;

import exam.q4_1.Dish;

public class Example {

    public static void main(String[] args) {
        boolean flag = Dish.getMenuList().stream().anyMatch(Dish::isVegetarian);

        System.out.println(flag);
    }
}
