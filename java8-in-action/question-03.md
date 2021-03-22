#### Q. 아래 코드에서 filter 함수를 구현하라. (java.util.function.Predicate 이용)

Apple에서 green만 필터링

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}

// filter

public static void main(String[] args) {
        List<Apple> apples = Arrays.asList(
            new Apple("green"),
            new Apple("yellow"),
            new Apple("red")
        );

        List<Apple> result = filter(apples, (Apple apple) -> apple.getColor().equals("green"));
    }
```



#### Q. 아래 코드에서 forEach 메소드를 구현하라. (java.util.function.Consumer 이용)

1~5까지 입력받아서 System.out.println한다.

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

// forEach

public static void main(String[] args) {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
    forEach(list, (Integer i) -> System.out.println(i));
}
```



#### Q. 아래 코드에서 map 메소드를 구현하라. (java.util.function.Function 이용)

Java In Action 문자를 받아서 글자 길이를 리턴하는 배열

```java
@FunctionalInterface
public interface Function<T, R> {    
    R apply(T t);
}

// map

public static void main(String[] args) {
        List<Integer> list = map(
            Arrays.asList("java", "in", "action"), 
            (String s) -> s.length()
        );
    }
```

