# 3 함수

##### 작게 만들어라!

함수를 만드는 첫째 규칙은 '작게!'다. 둘째 규칙은 '더 작게'다.



###### 블록과 들여쓰기

if문/else문/while 문 등에 들어가는 블록은 한 줄이어야 한다는 의미다. 



##### 한 가지만 해라!

> 함수는 한 가지를 해야 한다. 그 한 가지를 잘 해야 한다. 그 한 가지만을 해야 한다.

함수가 '한 가지'만 하는지 판단하는 방법이 하나 더 있다. 의미 잇는 이름으로 다른 함수를 추출할 수 있다면 그 함수는 여러 작업을 하는 셈이다.



##### 함수 당 추상화 수준은 하나로!

함수가 '한 가지' 작업만 하려면 함수 내 모든 문장의 추상화 수준이 동일해야 한다.

한 함수 내에 추상화 수준을 섞으면 코드를 읽는 사람이 헷갈린다. 



###### 위에서 아래로 코드 읽기: 내려가기 규칙

코드는 위에서 아래로 이야기처럼 읽혀야 좋다.



##### Switch 문

switch 문은 작게 만들기 어렵다.

본질적으로 switch 문은 N가지를 처리한다.

```java
public Money calculatePay(Employee e) throws InvalidEmployeeType {
    switch (e.type) {
        case COMMISSIONED:
            return calculateCommissionedPay(e);
        case HOURLY:
            return calculateHourlyPay(e);
        case SALARIED:
            return calculateSalariedPay(e);
        default: 
            throw new InvalidEmployeeType(e.type);
    }
}
```

위 함수의 문제점

첫째, 함수가 길다.

둘째, '한 가지' 작업만 수행하지 않는다.

세째, SRP를 위반한다. 코드를 변경할 이유가 여럿이기 때문이다.

네째, OCP를 위반한다. 새 직원 유형을 추갛ㄹ 때마다 코드를 변경하기 때문이다.



##### 서술적인 이름을 사용하라!

서술적인 이름이 짧고 어려운 이름보다 좋다.

길고 서술적인 이름이 길고 서술적인 주석보다 좋다.



##### 함수 인수

한수에서 이상적인 인수 개수는 0개다. 다음으로 1개고, 다음으로 2개다. 3개는 가능한 피하는 편이 좋다.



###### 많이 쓰는 단항 형식



###### 플래그 인수

플래그 인수는 추하다.함수로 부울 값을 넘기는 관례는 정말로 끔찍하다. 함수가 한꺼번에 여러 가지를 처리한다고 대놓고 공표하는 셈이니까!



###### 이항 함수

writeField(name)는 writeField(outputStream, name)보다 이해하기 쉽다.

assertEquals(expected, actual)은 expected 다음에 actual이 온다는 순서를 기억해야 한다.



###### 삼항 함수

인수가 3개인 함수는 인수가 2개인 함수보다 훨씬 더 이해하기 어렵다.



###### 인수 객체

인수가 2-3개 필요하다면 클래스 변수로 선언할 가능성을 짚어본다.

```java
Circle makeCircle(double x, double y, double radius);
Circle makeCircle(Point center, double radius);
```



###### 인수 목록















