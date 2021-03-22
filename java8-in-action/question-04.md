# 4장. 스트림 소개



#### Q. Dish 중 400칼로리 미만의 음식을 필터링 하고 칼로리 적은 순서대로 정렬하라.

(Java 7 이하 코드를 사용하여)

```java
@Getter
@ToString
public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public enum Type {
        MEAT,
        FISH,
        OTHER
    }

    public static List<Dish> getMenuList() {
        return Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french", false, 530, Type.OTHER),
                new Dish("rice", false, 350, Type.OTHER),
                new Dish("season", false, 120, Type.OTHER),
                new Dish("pizza", false, 550, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH)
        );
    }
}

```



```java
public class Example1 {
    
}
```



#### Q. 위의 예제를 java 8의 stream을 이용하여 구현하라.

```java
public class Example1 {
    
}
```



#### Q. 위의 Menu를 MenuType으로 그룹핑하여 표시하라. (groupingBy 이용)

```java
// 결과
{
    FISH=[prawns, salmon],
    OTHER=[fresh fries, rice, season fruit, pizza],
    MEAT=[pork, beef, chicken]
}
```



#### Q. 다음 list를 stream을 이용하려 출력하라(System.out 이용)

```java
public static void main(String[] args) {
    List<String> titles = Arrays.asList("Java8", "In", "Action");
    
    // add code
}
```

