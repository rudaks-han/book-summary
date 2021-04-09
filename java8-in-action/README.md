# Chapter 1 자바 8을 눈여겨봐야 하는 이유

자바 역사를 통틀어 가장 큰 변화가 자바 8에서 일어났다.

기존에는 무게에 따라 목록에서 사과 리스트를 정렬하는 코드를 다음처럼 구현했다.

```java
Collections.sort(inventory, new Comparator<Apple {
  public int compare(Apple a1, Apple a2) {
    return a1.getWeight().compareTo(a2.getWeight());
  }
});
```

하지만 java 8에서는 아래와 같이 할 수 있다.

```java
inventory.sort(comparing(Apple::getWeight));
```



* 스트림 API
* 메서드에 코드를 전달하는 기법
    * 메서드 레퍼런트
    * 람다
* 인터페이스의 디폴트 메서드



## 1.1 왜 아직도 자바는 변화하는가?

### 1.1.1 프로그래밍 언어 생태계에서 자바의 위치

빅데이터라는 도전에 직면하기 시작하면서 멀티코어 컴퓨터나 컴퓨팅 클러스터를 이용해서 빅데이터를 효과적으로 처리하고자 하는 욕구가 강해졌다. 자ㅏ로서는 충분히 댕층하지 못하는 분야다.



### 1.1.2 스트림 처리



...



# Chapter 2 동작 파라미터 코드 전달하기

소비자 요구사항은 항상 바뀐다. 변화하는 요구사항은 피할 수 없는 문제이다.

예를 들어 농부가 재고 목록 조사를 쉽게 할 수 있도록 돕는 애플리케이션이 있다고 가정하자.

> '녹색 사과를 모두 찾고 싶군요'

하루밤 후에 농부는 다시 이렇게 말하는 것이다.

> 150그램 이상인 사과를 모두 찾고 싶군요

또 하루밤을 자고 일어났더니 

> 150그램 이상이면서 녹색인 사과를 모두 찾을 수 있다면 좋겠네요.

이렇게 시시각각 변하는 사용자 요구사항에 어떻게 대응해야 할까?



**동작 파라미터**를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다. 동작 파라미터란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다. 코드 블록은 나중에 프로그램에서 호출한다.



## 2.1 변화하는 요구사항에 대응하기

### 2.1.1 첫 번째 시도: 녹색 사과 필터링

다음 코드로 녹색 사과만 필터링 할 수 있다.

```java
 public static List<Apple> filterGreenApples(List<Apple> inventory) {
   List<Apple> result = new ArrayList<>();
   for (Apple apple: inventory) {
     if ("green".equals(apple.getColor())) {
       result.add(apple);
     }
   }

   return result;
 }
```

### 2.1.2 두 번째 시도: 색을 파라미터화

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
  List<Apple> result = new ArrayList<>();
  for (Apple apple: inventory) {
    if (color.equals(apple.getColor())) {
      result.add(apple);
    }
  }

  return result;
}
```

농부는 다음처럼 메서드를 호출할 수 있다.

```java
List<Apple> result = filterApplesByColor(apples, "green");
List<Apple> result = filterApplesByColor(apples, "red");
```



농부가 '색 이외에도 가벼운 사과와 무거운 사과로 구분할 수 있다면 정말 좋겠네요. 보통 150그램 이상의 무게가나가는 사과를 무거운 사과라고 한답니다' 라고 요구한다.

```java
public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
  List<Apple> result = new ArrayList<>();
  for (Apple apple: inventory) {
    if (apple.getWeight() > weight) {
      result.add(apple);
    }
  }

  return result;
}
```

위의 코드는 색 필터링 코드와 대부분 중복된다는 사실을 확인할 수 있다. 소프트웨어 공학의 DRY 원칙을 어기는 것이다.



### 2.1.3 세 번째 시도: 가능한 모든 속성으로 필터링

```java
public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
  List<Apple> result = new ArrayList<>();
  for (Apple apple: inventory) {
    if (flag && color.equals(apple.getColor()) ||
        (!flag && apple.getWeight() > weight)) {
      result.add(apple);
    }
  }

  return result;
}
```

다음처럼 활용할 수 있다. (정말 마음에 들지 않는 코드다)

```java
List<Apple> result = filterApples(apples, "green", 0, true);
List<Apple> result = filterApples(apples, "", 150, false);
```

형편없는 코드다! true, false는 뭘 의미하는 걸까? 앞으로 요구사항이 바뀌었을 때 유연하게 대응할 수도 없다. 예를 들어 사과의 크기, 모양, 출하지 등으로 사과를 필터링하고 싶다면 어떻게 될까? 심지어 녹색 사과 중에 무거운 사과를 필터링하고 싶다면?



## 2.2 동작 파라미터화

사과의 어떤 속성에 기초해서 불린값을 변환하는 방법이 있다.이와 같은 동작을 **프레디케이트**(predicate)라고 한다.

```java
public interface ApplePredicate {
  boolean test(Apple apple);
}
```



다음 예제처럼 다양한 선택 조건을 대표하는 여러 버전의 ApplePredicate를 정의할 수 있다.

```java
public class appleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}
```

```java
public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
}
```



위 조건에 따라 filter 메서드가 다르게 동작할 것이라고 예상할 수 있다. 이를 전략 디자인 패턴이라고 부른다.

ApplePredicate가 알고리즘 패밀리고 appleHeavyWeightPredicate와 AppleGreenColorPredicate가 전략이다.



### 2.2.1 네 번째 시도: 추상적 조건으로 필터링

```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
  List<Apple> result = new ArrayList<>();
  for (Apple apple: inventory) {
    if (p.test(apple)) {
      result.add(apple);
    }
  }

  return result;
}
```

###### 코드/동작 전달하기

```java
public class AppleRedAndHeavyPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return "red".equals(apple.getColor()) && apple.getWeight() > 150;
    }
}
```



## 2.3 복잡한 과정 간소화

## 2.3.1 익명 클래스

### 2.3.2 다섯 번째 시도: 익명 클래스 사용

### 2.3.3 여섯 번째 시도: 람다 표현식 사용

자바 8의 람다 표현식을 이용해서 위 예제 코드를 다음처럼 간단하게 재구현할 수 있다.

```java
List<Apple> result = filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));
```



### 2.3.4 일곱 번째 시도: 리스트 형식으로 추상화

```java
public interface Predicate<T> {
  boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
  List<T> result = new ArrayList<>();
  for (T e: list) {
    if (p.test(e)) {
      result.add(e);
    }
  }

  return result;
}
```

다음은 람다 표현식을 사용한 예제다.

```java
List<Apple> result = filter(apples, (Apple apple) -> "red".equals(apple.getColor()));
```



## 2.4 실전 예제

```java
public interface Comparator<T> {
  public int compare(T o1, T o2);
}
```



```java
inventory.sort(new Comparator<Apple> {
  public int compare(Apple a1, Apple a2) {
    return a1.getWeight().compareTo(a2.getWeight());
  }
});
```

람다 표현식으로 아래와 같이 구현할 수 있다.

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
```



### 2.4.2 Runnable로 코드 블록 실행하기

```java
public interface Runnable {
  public void run();
}

Thread t = new Thread(new Runnable() {
  public void run() {
      	System.out.println("Hello World");
   }
});          
```

람다 표현식으로 아래와 같이 구현할 수 있다.

```java
Thread t = new Thead(() -> System.out.println());
```



# Chapter 3 람다 표현식

## 3.1 람다란 무엇인가?

**람다 표현식**은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이라고 할 수 있다. 람다 표현식에는 이름은 없지만, 파라미터 리스트, 바디, 반환 형식, 발생할 수 있는 예외 리스트는 가질 수 있다.



Comparator객체를 기존보다 간단하게 구현할 수 있다.

다음은 기존 코드다.

```java
Comparator<Apple> byWeight = new Comparator<Apple) {
  public int compare(Apple o1, Apple o2) {
    return o1.getWeight().compareTo(o2.getWeight());
  }
};
```

다음은 람다를 이용한 새로운 코드다.

```java
Comparator<Apple> byWeight = (Apple o1, Apple o2) -> o1.getWeight().compareTo(o2.getWeight());
```



람다는 세 부분으로 이루어진다.

* 파라미터 리스트
* 화살표
* 람다의 바디



### 3.2 어디에, 어떻게 람다를 사용할까?

어디에서 람다를 사용할 수 있다는 건가? 함수형 인터페이스라는 문맥에서 람다 표현식을 사용할 수 있다.



### 3.2.1 함수형 인터페이스

2장에서 만든 Predicate<T> 인터페이스로 필터 메서드를 파라미터화할 수 있었음을 기억하는가? 바로 Predicate<T>가 함수형 인터페이스다. Predicate<T>는 오직 하나의 추상 메서드만 지정하기 때문이다.

```java
public Interface Predicate<T> {
  boolean test(T t);
}
```

**함수형 인터페이스**는 정확히 하나의 추상 메서드를 지정하는 인터페이스다.

> 많은 디폴트 메서드가 있더라도 추상 메서드가 오직 하나면 함수형 인터페이스다.



### 3.2.2 함수 디스크립터

함수형 인터페이스의 추상 메서드 시그너처는 람다 표현식의 시그너처를 가리킨다. 람다 표현식의 시그너처를 서술하는 메서드를 **함수 디스크립터**라고 부른다.



> @FunctionalInterface는 무엇인가?
>
> 함수형 인터페이스임을 가리키는 어노테이션이다. @FunctionalInterface로 인터페이스를 선언했지만 실제로 함수형 인터페이스가 아니면 컴파일러가 에러를 발생시킨다.



## 3.3 람다 활용: 실행 어라운드 패턴

자원 처리에 사용하는 순환 패턴은 자원을 열고, 처리한 다음에, 자원을 닫는 순서로 이루어진다. 즉, 실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태를 갖는다. 이와 같은 형식의 코드를 실행 어라운드 패턴이라고 부른다.

```java
pubic static String processFile throws IOException {
  try ( BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    return br.readLine();
  }
}
```



### 3.3.1 1단계: 동작 파라미터를 기억하라.

<내용 정리 필요>



## 3.4 함수형 인터페이스 사용

자바 8에는 java.util.function 패키지로 여러가지 함수형 인터페이스를 제공한다. 이 절에서는 Predicate, Consumer, Function 인터페이스를 설명한다.



### 3.4.1 Predicate

```java
@FunctionalInterface
public interface Predicate<T> {
  boolean test(T t);
}

public static <T> filter(List<T> list, Predicate<T> t) {
  List<T> results = new ArrayList<>();
  for (T s: list) {
    if (t.test(s)) {
      results.add(s);
    }
  }

  return results;
}

Predicate<String> notEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfString, notEmptyStringPredicate);
```



### 3.4.2 Consumer

```java
@FunctionalInterface
public interface Consumer<T> {
  void accept(T t);
}

public static <T> void forEach(List<T> list, Consumer<T> c) {
  for (T i: list) {
	c.accpet(i);
  }
}

forEach(
  Arrays.asList(1, 2, 3, 4),
  (Integer i) -> System.out.println(i);
);
```



### 3.4.3 Function

```java
@FunctionalInterface
public interface Function<T, R> {
  R apply(T t);
}

public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
  List<R> result = new ArrayList<>();
  for (T s: list) {
    result.add(f.apply(s));
  }
  
  return result;
}

List<Integer> l = map(
  Arrays.asList("lambdas", "In", "action"),
  (String s) -> s.length()
);
```



#### 기본형 특화

* 박싱(boxing) : 기본형을 참조형으로 변환

* 언박싱(unboxing): 참조형을 기본형으로 변환

* 오토박싱(auto boxing): 기본형을 참조형 변환이 자동으로 이뤄지는 것

    

```java
public interface IntPredicate {
  boolean test(int t);
}

IntPredicate evenNumbers = (int i) -> i % 2 == 0; // 박싱 없음
eventNumbers.test(1000);

Predicate<Integer> evenNumbers = (Integer i) -> i % 2 == 0; // 박싱 있음
evenNumbers.test(1000);
```



## 3.5 형식 검사, 형식 추론, 제약

### 3.5.1 형식 검사

람다가 사용되는 콘텍스트를 이용해서 람다의 형식을 추론할 수 있다. 어떤 콘텍스트에서 기대되는 람다 표현식의 형식을 대상 형식이라고 부른다. 람다 표현식을 사용할 때 실제 어떤 일이 일어나는지 보여주는 예제를 확인하자.

```java
List<Apple> heavierThan150g = filter(inventory, (Apple a) -> a.getWeight() >. 150);
```



하나의 람다 표현식을 다양한 함수형 인터페이스에 사용할 수 있다.

```java
Comparator<Apple> c1 = (Apple o1, Apple o2) -> o1.getColor().compareTo(o2.getColor());

ToIntBiFunction<Apple, Apple> c2 = (Apple o1, Apple o2) -> o1.getColor().compareTo(o2.getColor();
                                                                                   
BiFunction<Apple, Apple, Integer> c3 = (Apple o1, Apple o2) -> o1.getColor().compareTo(o2.getColor();
```



### 3.5.3 형식 추론

람다 표현식이 사용된 콘텍스트를 이용하여 람다 표현식과 관련된 함수형 인터페이스를 추론한다. 대상 형식을 이용해서 함수 드스크립터를 알 수 있으므로 컴파일러는 람다의 시그너처도 추론할 수 있다. 결과적으로 컴파일러는 람다 표현식의 파라미터 형식에 접근할 수 있으므로 람다 문법에서 이를 생략할 수 있다.

```java
List<Apple> greenApples = filter(inventory, a -> "green".equals(a.getColor())); //a의 형식을 지정하지 않았다.
```



### 3.5.4 지역 변수 사용

람다 표현식에서는 익명 함수가 하는 것처럼 자유 변수를 활용할 수 있다. 이와 같은 동작을 람다 캡처링이라고 부른다.

```java
int portNumber = 1337;
Runnable r = () -> System.out.println(portNumber);
```



지역 변수는 명식적으로 final로 선언되어 있어야 하거나 실질적으로 final로 선언된 변수와 똑같이 사용되어야 한다.

```java
int portNumber = 1337;
Runnable r = () -> System.out.println(portNumber);
portNumber = 31337; // 컴파일 에러
```



## 3.6 메서드 레퍼런스

메서드 레퍼런스는 특정 람다 표현식을 축약한 것이라고 생각하면 된다.

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

inventory.sort(comparing(Apple::getWeight());
```



### 3.6.1 요약

메서드 레퍼런스는 특정 메서드만을 호출하는 람다의 축약형이라고 생각할 수 있다.

Apple::getWeight는 Apple클래스에 정의된 getWeight의 메서드 레퍼런스이다.



```java
(Apple a) -> a.getWeight();
Apple::getWeight
  
() -> Thead.currentThread().dumpStack()
Thread.currentThread::dumpStack
  
(str, i) -> str.substring
String:substring
  
(String s) -> System.out.println(s)
System.out::println
  
```



다음의 람다 표현식과 일치하는 메서드 레퍼런스를 작성하시오.

```java
1)
Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
>
Function<String, Integer> stringToInteger = Integer::parseInt;

2) 
BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
>
BiPredicate<List<String>, String> contains = List:contains;
```



### 3.6.2 생성자 레퍼런스

```java
Supplier<Apple> c1 = Apple::new;
Apple a = c1.get();

// 위의 예제는 다음 코드와 같다.
Supplier<Apple> c1 = () -> new Apple();
Apple a = c1.get();
```



## 3.7 람다, 메서드 레퍼런스 활용하기!

### 3.7.1 1단계: 코드 전달

```java
void sort(Comparator<? super E> c)
  
public class AppleComparator implements Comparator<Apple> {
  public int compare(Apple a1, Apple a2) {
    return a.getWeight().compareTo(a2.getWeight());
  }
}

inventory.sort(new AppleComparator());
```



### 3.7.2 2단계: 익명 클래스 사용

```java
inventory.sort(new Comparator() {
   public int compare(Apple a1, Apple a2) {
    return a.getWeight().compareTo(a2.getWeight());
  }
})
```



### 3.7.3 3단계: 람다 표현식 사용

```java
inventory.sort((Apple a1, Apple a2) -> a.getWeight().compareTo(a2.getWeight()));
```



### 3.7.4 4단계: 메서드 레퍼런스 사용

```java
inventory.sort(Comparator.comparing(Apple::getWeight));
```



## 3.8 람다 표현식을 조합할 수 있는 유용한 메서드

### 3.8.1 Comparator 조합

```java
Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
```

역정렬

```java
inventory.sort(comparing(Apple::getWeight).reverse());
```



**Comparator 연결**

```java
inventory.sort(comparing(Apple:getWeight))
  .reversed()
  .thenComparing(Apple::getCountry));
```



### 3.8.2 Pedicate 조합

Predicate 인터페이스는 복잡한 프레디케이트를 만들 수 있도록 negate, and, or 세가지 메서드를 제공한다. 예를 들어 '빨간 색이 아닌 사과' 처럼 특정 프레디케이트를 반전시킬 때 negate 메서드를 사용할 수 있다.

```java
Predicate<Apple> notRedApple = redApple.negate(); // redApple의 결과를 반전시킨 객체를 만든다.

Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150); // and 메서드를 이용해서 빨간색이면서 무거운 사과를 선택

Predicate<Apple> readAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
  .or(a -> "green".equals(a.getColor()));
```



### 3.8.3 Function 조합

andThen 메서드는 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환한다.

```java
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.andThen(g);
int result = f.apply(1); // 4를 반환
```

compse 메서드는 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다.

```java
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.compose(g);
int result = f.apply(1); // 3을 반환
```







# Chapter 4 스트림 소개

## 4.1 스트림이란 무엇인가?

스트림을 이용하면 선언형으로 컬렉션 데이터를 처리할 수 있다.

스트림을 이용하면 멀티 스레드 코드를 구현하지 않아도 데이터를 **투명하게** 병렬로 처리할 수 있다.



다음은 기존 코드다(자바 7)

```java
List<Dish> lowCaloriesDishes = new ArrayList<>();
for (Dish dish: Dish.getMenuList()) {
  if (dish.getCalories() < 400) {
    lowCaloriesDishes.add(dish);
  }
}

Collections.sort(lowCaloriesDishes, new Comparator<Dish>() {
  @Override
  public int compare(Dish o1, Dish o2) {
    return Integer.compare(o1.getCalories(), o2.getCalories());
  }
});

for (Dish dish: lowCaloriesDishes) {
  System.out.println(dish.toString());
}
```



다음은 최신 코드다(자바 8)

```java
List<Dish> lowCaloriesDishes = Dish.getMenuList().stream()
  .filter(dish -> dish.getCalories() < 400)
  .sorted(Comparator.comparing(Dish::getCalories))
  .collect(Collectors.toList());

lowCaloriesDishes.stream().forEach(System.out::println);
```

Stream()을 parallelStream()으로 바꾸면 이 코드를 멀티코어 아키텍처에서 병렬로 실행할 수 있다.

```java
List<Dish> lowCaloriesDishes = Dish.getMenuList().parallelStream()
  .filter(dish -> dish.getCalories() < 400)
  .sorted(Comparator.comparing(Dish::getCalories))
  .collect(Collectors.toList());
```

* 선언형으로 코드를 구현할 수 있다. 루프오 if 조건문 등의 제어 블록을 사용할 필요 없다.
* filter, sorted, map, collect 같은 연산을 연결해서 복잡한 데이터 처리 파이프라인을 만들 수 있다.



자바 8의 스트림 API의 특징을 다음처럼 요약할 수 있다.

* 선언형
    * 더 간결하고 가독성이 좋아진다.
* 조립할 수 있음
    * 유연성이 좋아진다.
* 병렬화
    * 성능이 좋아진다.



## 4.2 스트림 시작하기

**스트림**이란 정확히 뭘까? 스트림이란 '데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소'로 정의할 수 있다.



## 4.3 스트림과 컬렉션

자바의 기존 컬렉션과 새로운 스트림 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다. 

DVD에 전체 자료구조가 저장되어 있으므로  DVD도 컬렉션이다.

인터넷 스트리밍은 몇 프레임으로부터 재생할 수 있다.

컬렉션은 현재 자료구조가 포함하는 모든 값을 메모리에 저장하는 자료구조다.

반면 스트림은 요청할 때만 요소를 계산하는 고정된 자료구조다. (스트림에 요소를 추가하거나 스트림에서 요소를 제거할 수 없다).



# Chapter 5 스트림 활용

4장에서는 스트림을 이용하여 외부 반복을 내부 반복으로 바꾸는 방법을 살펴봤다.

[java 7 방법]

``` java
List<Dish> vegetarianDishes = new ArrayList<>();
for (Dish d: menu) {
  if (d.isVegetarian()) {
    vegetarianDishes.add(d);
  }
}
```

[java 8 streams 방법]

```java
List<Dish> vegetarianDishes = menu.streams()
  .filter(Dish::isVegetarian)
  .collect(Collectors.toList());
```



## 5.1 피터링과 슬라이싱

### 5.1.1 프레디케이트로 필터링

```java
List<Dish> vegetarianDishes = menu.streams()
  .filter(Dish::isVegetarian)
  .collect(Collectors.toList());
```



### 5.1.2 고유 요소 필터링

```java
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
  .filter(i -> i % 2 == 0)
  .distinct()
  .forEach(System.out::println);
```



### 5.1.3 스트림 축소

```java
List<Dish> vegetarianDishes = menu.streams()
  .filter(d -> d.getCalories > 300)
  .limit(3)
  .collect(Collectors.toList());
```



### 5.1.4 요소 건너뛰기

```java
List<Dish> vegetarianDishes = menu.streams()
  .filter(d -> d.getCalories > 300)
  .skip(2)
  .collect(Collectors.toList());
```



## 5.2 매핑

### 5.2.1 스트림의 각 요소에 함수 적용하기

```java
List<String> dishNames = menu.stream()
  .map(Dish::getName)
  .collect(toList());
```



```java
List<String> words = Arrays.asList("Java8", "Lamdbas", "in", "action");
List<Integer> wordLengths = words.stream()
  .map(String::length)
  .collect(toList());
```



### 5.2.2 스트림 평면화

**map과 Arrays.stream 활용**

```java
String [] arrayOfWords = {"Goodbye", "World"};
Stream<String> streamOfWords = Arrays.stream(arrayOfWords);

words.stream()
  .map(word.split(""))
  .map(Arrays::stream)
  .distinct()
  .collect(toList());
```



**flatMap 사용**

```java
List<String> uniqueCharacters = words.stream()
  .map(w -> w.split(""))
  .flatMap(Arrays::stream)
  .distinct()
  .collect(toList());
```



## 5.3 검색과 매칭

### 5.3.1 프레디케이트가 적어도 한 요소와 일치하는지 확인

```java
if (menu.stream().anyMatch(Dish::isVegetarian) {
  System.out.println("The menu is vegetarian friendly!");
}
```



### 5.3.2 프레디케이트가 모든 요소와 일치하는지 검사

```java
boolean isHealthy = menu.stream().allMatch(d -> d.getCalories() < 1000);
```

```java
boolean isHealthy = menu.stream().noneMatch(d -> d.getCalories() < 1000);
```



### 5.3.3 요소 검색

```java
Optional<Dish> dish = menu.stream()
  .filter(Dish::isVegetarian)
  .findAny();
```



**Optional이란?**

Optional 클래는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스다. null은 쉽게 에러를 일으킬 수 있으므로 자바 8 라이브러리 설계자는 Optional<T>라는 기능을 만들었다.

```java
menu.stream()
  .filter(Dish::isVegetarian)
  .findAny()
  .ifPresent(d -> System.out.println(d.getName()));
```



### 5.3.4 첫 번째 요소 찾기

```java
List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> firstSquareDivisibleByThree =
  someNumbers.stream()
  .map(x -> x * x)
  .filter(x -> x % 3 == 0)
  .findFirst(); // 9
```



> **findFirst와 findAny는 언제 사용하나?**
>
> findFirst와 findAny 두가지 메서드가 모두 필요할까? 바로 병렬성 때문이다. 병렬실행에서는 첫번째 요소를 찾기 힘들다. 따라서 요소의 반환순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용한다.



## 5.4 리듀싱

'메뉴에서 칼로리가 가장 높은 요리는?'같이 스트림 요소를 조합해서 더 복잡한 질의를 표현하는 방법을 설명한다. 이러한 질의를 수행하려면 Integer 같은 결과가 나올때까지 스트림의 모든 요소를 반복적으로 처리해야 한다. 이런 질의를 **리듀싱 연산**(모든 스트림 요소를 처리해서 값으로 도출하는)이라고 한다. 함수형 프로그래밍 언어 용어로는 이 과정이 마치 종이(우리의 스트림)를 작은 조각이 될 때까지 반복해서 접는 것과 비슷하다는 의미로 **폴드(fold)**라고 부른다.



### 5.4.1 요소의 합

```java
int sum = 0;
for (int x : numbers) {
  sum += x;
}
```

```java
numbers.stream().reduce(0, (a, b) -> a + b);
```

```java
numbers.stream().reduce(0, Integer::sum);
```



초깃값 없음

```java
Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
```



### 5.4.2 최댓값과 최솟값

```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```





# Chapter 6 스트림으로 데이터 수집







# Chapter 7 병렬 데이터 처리와 성능



## 7.1 병렬 스트림

숫자 n을 인수로 받아 1부터 n까지 모든 숫자의 합계를 반환하는 함수

```java
public static long sequentialSum(long n) {
  return Stream.iterate(1L, i -> i + 1)
   						.limit(n)
    					.reduce(0L, Long::sum);
}
```



전통적인 자바

```java
public static long iterativeSum(long n) {
  long result = 0;
  for (long i=1L; i<=n; i++) {
    result += i;
  }
  
  return result;
}
```

병렬 실행

```java
public static long sequentialSum(long n) {
  return Stream.iterate(1L, i -> i + 1)
   						.limit(n)
    					.parallel()
    					.reduce(0L, Long::sum);
}
```



위의 3개를 실행하면 병렬버전이 가장 느리다.

그 이유는

* iterate가 박싱된 객체를 생성하므로 이를 언방식하는 과정이 필요했다.
* iterate는 병렬로 실행될 수 있도록 독립적인 청크로 분할하기가 어렵다.



**더 특화된 메서드 사용**





























