녹색 사과만 필터링

### 2.1.1 첫번째 시도: 녹색 사과 필터링

```java
@Getter
@AllArgsConstructor
public class Apple {

    private String color;
}
```

녹색 사과만 필터링 하라.

```java
public class Example01 {
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

      // add code
        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("green"),
                new Apple("yellow"),
                new Apple("red")
        );

        List<Apple> result = filterGreenApples(apples);

        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
```



### 2.1.2 색을 파라미터화

```java
public class Example02 {
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
      // add code
      
      return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        // add code

        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
                new Apple("green", 10),
                new Apple("yellow", 20),
                new Apple("red", 30)
        );

        List<Apple> result = filterApplesByColor(apples, "red");
        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));

        List<Apple> result2 = filterApplesByWeight(apples, 20);
        result2.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
```

