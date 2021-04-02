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



























