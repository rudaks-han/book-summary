package com.example.ch02.item02;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NutritionFacts {

    private int servingSize;

    private int servings;

    private int calories;

    private int fat;

    private int sodium;

    private int carbohydrate;

    public static void main(String[] args) {
        NutritionFacts nutritionFacts = new NutritionFactsBuilder()
                .servingSize(100)
                .servings(10)
                .build();
    }
}
