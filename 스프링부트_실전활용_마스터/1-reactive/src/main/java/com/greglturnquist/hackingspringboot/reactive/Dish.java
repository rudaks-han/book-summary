package com.greglturnquist.hackingspringboot.reactive;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Dish {

    private String description;
    private boolean delivered = false;

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = new Dish(dish.description);
        deliveredDish.delivered = true;
        return deliveredDish;
    }

    Dish(String description) {
        this.description = description;
    }

}
