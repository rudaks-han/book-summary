



# 3장. 람다 표현식



#### Q3-1. 아래 코드에서 filter 함수를 구현하라. (java.util.function.Predicate 이용)

짝수만 필터링하여 출력

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}

public class Example {
    public static List filter() {
        // add code
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> result = filter(list, (Integer i) -> i % 2 == 0);

        result.forEach(System.out::println);
    }
}
```



#### Q3-2. 아래 코드에서 forEach 메소드를 구현하라. (java.util.function.Consumer 이용)

1~5까지 입력받아서 System.out.println한다.

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

// forEach
public static void forEach() {
    // add code
}

public static void main(String[] args) {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
    forEach(list, (Integer i) -> System.out.println(i));
}
```



#### Q3-3. 아래 코드에서 map 메소드를 구현하라. (java.util.function.Function 이용)

Java In Action 문자를 받아서 글자 길이를 리턴하는 배열

```java
@FunctionalInterface
public interface Function<T, R> {    
    R apply(T t);
}

public static List map() {
    // add code
}

public static void main(String[] args) {
        List<Integer> list = map(
            Arrays.asList("java", "in", "action"), 
            (String s) -> s.length()
        );
    }
```



# 4장. 스트림 소개

#### Q4-1. stream 문제

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



#### Dish 중 400칼로리 미만의 음식을 필터링 하고 칼로리 적은 순서대로 정렬하라. (Java 7 이용)

```java
public class Java7Example {
    
}
```



#### 위의 예제를 java 8의 stream을 이용하여 구현하라.

```java
public class Java8Example {
    
}
```



#### Q4-2. 위의 Menu를 MenuType으로 그룹핑하여 표시하라. (groupingBy 이용)

```java
// 결과
{
    FISH=[prawns, salmon],
    OTHER=[fresh fries, rice, season fruit, pizza],
    MEAT=[pork, beef, chicken]
}
```



#### Q4-3. 다음 list를 stream을 이용하려 출력하라(System.out 이용)

```java
public static void main(String[] args) {
    List<String> titles = Arrays.asList("Java8", "In", "Action");
    
    // add code
}
```



# 5장. 스트림 활용



#### Q5-1. 다음 코드를 stream을 이용하여 작성하라.

```java
public static void main(String[] args) {
        List<Dish> vegetarianDishes = new ArrayList<>();
        for (Dish d: Dish.getMenuList()) {
            if (d.isVegetarian()) {
                vegetarianDishes.add(d);
            }
        }
  
  	// add code
}
```



#### Q5-2. 다음 배열에서 짝수이면서 중복을 필터링하여 출력하라. 

```java
public static void main(String[] args) {
	List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
  
  	// add code
}
```



#### Q5-3. 다음 배열에서 글자수를 출력하라. 

```java
public static void main(String[] args) {
	List<String> words = Arrays.asList("Java8", "Lamdas", "In", "Action");
  
  	// add code
}
```



#### Q5-4. Hello World를 다음 출력값이 나오게 stream을 작성하라.

```java
public static void main(String[] args) {
        List<String> words = Arrays.asList("Hello", "World");
  
  	// add code
  	// 출력 ["H", "e", "l", "l", "o", "W", "o", "r", "l", "d"]
}
```



#### Q5-5. 숫자 리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환하시오. 예를 들어 [1,2,3,4,5]가 주어지면 [1,4,9,16,25]를 반환해야 한다.



```java
public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
  
  	// add code
}
```



#### Q5-6. 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하시오. 예를 들면 두 개의 리스트 [1,2,3]과 [3,4]가 주어지면 [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]를 반환해야 한다. 

```java
public static void main(String[] args) {
  List<Integer> number1 = Arrays.asList(1,2,3);
  List<Integer> number2 = Arrays.asList(3,4);
  
  //add code
}
```



#### Q5-7. 다음 예제에서 채식요리가 있는지 확인하는 코드를 작성하라. (anyMatch 이용)

```java
public static void main(String[] args) {
	Dish.getMenuList(). // add code
}
```



#### Q5-8. 다음 코드를 모든 칼로리의 합계를 나타내는 코드이다. 해당 코드를 reduce를 이용하여 코드를 작성하라.

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();
  
  // add code
}
```



#### Q5-9. map과 reduce 메서드를 이용하여 스트림의 요리 개수를 계산하시오.

```java
public static void main(String[] args) {
	List<Dish> dishes = Dish.getMenuList();
  
  	// add code
}
```



다음 코드는 Transaction.getTransactionList()를 stream으로 조작하는 문제이다.

```java
@AllArgsConstructor
@Getter
@ToString
public class Trader {
  p2rivate final String name;

  private final String city;
}
```



```java
public static List<Transaction> getTransactionList() {
  Trader raoul = new Trader("Raoul", "Cambridge");
  Trader mario = new Trader("Mario", "Milan");
  Trader alan = new Trader("Alan", "Cambridge");
  Trader brian = new Trader("Brian", "Cambridge");

  List<Transaction> transactions = Arrays.asList(
    new Transaction(brian, 2011, 300),
    new Transaction(raoul, 2012, 1000),
    new Transaction(raoul, 2011, 400),
    new Transaction(mario, 2012, 710),
    new Transaction(mario, 2012, 700),
    new Transaction(alan, 2012, 950)
  );

  return transactions;
}
```



#### Q5-10. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.



#### Q5-11. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.



#### Q5-12. 케임브리지(Cambridge)에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.



#### Q5-13. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.



#### Q5-14. 밀라노(Milan)에 거래자가 있는가?



#### Q5-15. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.



#### Q5-16. 전체 트랜잭션 중 최댓값은 얼마인가? (reduce 이용)



#### Q5-17. 전체 트랜잭션 중 최솟값은 얼마인가? (reduce 이용)



#### Q5-18. 칼로리의 합계를 sum을 이용하여 출력하라.

```java
List<Dish> dishes = Dish.getMenuList();
int calories = dishes.stream()
    	// add code
```



#### Q5-19. 다음 문자를 Stream.of를 이용하여 대문자로 변환한 후 출력하라.

```java
// "Java 8", "Lamdbas", "In", "Action"
```



#### Q5-20. Stream.iterate를 이용하여 짝수를 5개 출력하라.

```java
// 0, 2, 4, 6, 8
```



#### Q5-21. Stream.iterate를 이용하여 피보나치수열을 10개 출력하라. 

```java
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34
```

