# 6장. 스트림으로 데이터 수집 



#### Q1. 아래 예제에서 가장 칼로리가 높은 음식을 출력하라. (Comparator, maxBy 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // add code
}
```



#### Q2. 아래 코드에서 모든 음식의 이름을 ,로 구분하는 문자열을 출력하라. (joining 이용)

```java
public static void main(String[] args) {
        List<Dish> dishes = Dish.getMenuList();

       // pork, beef, chicken, french fries, rice, season, pizza, prawns, salmon
    }
```



#### Q3. 아래 코드에서 모든 음식의 칼로리 합계를 출력하라. (1. reducing 이용, 2. reduce 이용, 3. sum() 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // reducing, reduce, mapToInt
    int reducingSum = 
    System.out.println(reducingSum);

    int reduceSum = 
    System.out.println(reduceSum);

    int sum = 
    System.out.println(sum);
}
```



#### Q4. 아래 코드에서 메뉴의 타입으로 그룹핑하라. (groupingBy 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // add code
}
```



#### Q5. 요리의 수를 종류(type)별로 계산하여 출력하라. (groupingBy 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // add code
}
```



#### Q6 요리의 종류별로 가장 칼로리가 높은 음식을 출력하라. (groupingBy, maxBy 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // add code
}
```



#### Q7. 요리에서 채식인 것과 아닌 것을 구분하여 출력하라. (partitioningBy 이용)

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    // add code
}
```









