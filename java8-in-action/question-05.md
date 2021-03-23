# 5장. 스트림 활용



#### Q. 다음 코드를 stream을 이용하여 작성하라.

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



#### Q. 다음 배열에서 짝수이면서 중복을 필터링하여 출력하라. 

```java
public static void main(String[] args) {
	List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
  
  	// add code
}
```



#### Q. 다음 배열에서 글자수를 출력하라. 

```java
public static void main(String[] args) {
	List<String> words = Arrays.asList("Java8", "Lamdas", "In", "Action");
  
  	// add code
}
```



#### Q. Hello World를 다음 출력값이 나오게 stream을 작성하라.

```java
public static void main(String[] args) {
        List<String> words = Arrays.asList("Hello", "World");
  
  	// add code
  	// 출력 ["H", "e", "l", "l", "o", "W", "o", "r", "l", "d"]
}
```



#### Q. 숫자 리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환하시오. 예를 들어 [1,2,3,4,5]가 주어지면 [1,4,9,16,25]를 반환해야 한다.



```java
public static void main(String[] args) {
        List<Integer> words = Arrays.asList(1, 2, 3, 4, 5);
  
  	// add code
}
```



#### Q. 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하시오. 예를 들면 두 개의 리스트 [1,2,3]과 [3,4]가 주어지면 [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]를 반환해야 한다. 

```java
public static void main(String[] args) {
  List<Integer> number1 = Arrays.asList(1,2,3);
  List<Integer> number2 = Arrays.asList(3,4);
  
  //add code
}
```



#### Q. 다음 예제에서 채식요리가 있는지 확인하는 코드를 작성하라. (anyMatch 이용)

```java
public static void main(String[] args) {
	Dish.getMenuList(). // add code
}
```



#### Q. 다음 코드를 모든 칼로리의 합계를 나타내는 코드이다. 해당 코드를 reduce를 이용하여 코드를 작성하라.

```java
public static void main(String[] args) {
    List<Dish> dishes = Dish.getMenuList();

    int sum = 0;
    for (Dish dish: dishes) {
      	sum += dish.getCalories();
    }
  
  // add code
}
```



#### Q. map과 reduce 메서드를 이용하여 스트림의 요리 개수를 계산하시오.

```java
public static void main(String[] args) {
	List<Dish> dishes = Dish.getMenuList();
  
  	// add code
}
```



다음 코드는 Transaction.getTransactionList()를 stream으로 조작하는 문제이다.

#### Q. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.



#### Q. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.



#### Q. 케임브리지(Cambridge)에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.



#### Q. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.



#### Q. 밀라노(Milan)에 거래자가 있는가?



#### Q. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.



#### Q. 전체 트랜잭션 중 최댓값은 얼마인가?



#### Q. 전체 트랜잭션 중 최솟값은 얼마인가?



#### Q. 칼로리의 합계를 sum을 이용하여 출력하라.

```java
List<Dish> dishes = Dish.getMenuList();
int calories = dishes.stream()
    	// add code
```



#### Q. 다음 문자를 Stream.of를 이용하여 대문자로 변환한 후 출력하라.

```java
// "Java 8", "Lamdbas", "In", "Action"
```



#### Q. Stream.iterate를 이용하여 짝수를 5개 출력하라.

```java
// 0, 2, 4, 6, 8
```



#### Q. Stream.iterate를 이용하여 피보나치수열을 10개 출력하라.

```java
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34
```



















