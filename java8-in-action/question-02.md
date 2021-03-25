

# 2장. 동작 파라미터



#### Q1. 녹색 사과만 필터링 하라. (filterGreenApples)

For-loop 이용

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



#### Q2. Predicate를 이용하여 녹색 사과를 필터링 하라.

```java
public class Test {

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> results = new ArrayList<>();
   
        // add code

        return results;
    }

    public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
            new Apple("green"),
            new Apple("yellow"),
            new Apple("red")
        );

        List<Apple> result = filter(apples, (Apple apple) -> apple.getColor().equals("green"));

        result.stream().map(Apple::getColor).forEach(s -> System.out.println(s));
    }
}
```

