package com.example.ch02.item01;

public class NutritionFacts {

    private int servingSize;

    private int servings;

    private int calories;

    private int fat;

    private int sodium;

    private int carbohydrate;

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
