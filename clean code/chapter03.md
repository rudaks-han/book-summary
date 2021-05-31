# 3장 함수

##### 작게 만들어라!

함수를 만드는 첫째 규칙은 '작게!'다. 둘째 규칙은 '더 작게'다.

얼마나 짧아야 하느냐고?

```java
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) {
    if (isTestPage(pageData)) 
        includeSetupAndTeardownPages(pageData, isSuite);
    return pageData.getHtml();
)
```



###### 블록과 들여쓰기

* if문/else문/while 문 등에 들어가는 블록은 한 줄이어야 한다는 의미다. 
* 함수에서 들여쓰기 수준은 1단이나 2단을 넘어서면 안된다.



##### 한 가지만 해라!

> 함수는 한 가지를 해야 한다. 그 한 가지를 잘 해야 한다. 그 한 가지만을 해야 한다.

지정된 함수 이름 아래에서 추상화 수준이 하나인 단계만 수행한다면 그 함수는 한가지 작업만 한다. 우리가 함수를 만드는 이유는 큰 개념을 다음 추상화 수준에서 여러 단계로 나눠 수행하기 위해서가 아니던가.

함수가 '한 가지'만 하는지 판단하는 방법이 하나 더 있다. <u>의미 있는 이름으로 다른 함수를 추출할 수 있다면 그 함수는 여러 작업을 하는 셈이다</u>.



##### 함수 당 추상화 수준은 하나로!

함수가 '한 가지' 작업만 하려면 함수 내 모든 문장의 추상화 수준이 동일해야 한다.

한 함수 내에 추상화 수준을 섞으면 코드를 읽는 사람이 헷갈린다. 근본 개념을 뒤섞기 시작하면, 깨어진 창문처럼 사람들이 함수에 세부사항을 점점 더 추가한다.



###### 위에서 아래로 코드 읽기: 내려가기 규칙

코드는 위에서 아래로 이야기처럼 읽혀야 좋다.



##### Switch 문

switch 문은 작게 만들기 어렵다. 본질적으로 switch 문은 N가지를 처리한다.

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

네째, OCP를 위반한다. 새 직원 유형을 추가할 때마다 코드를 변경하기 때문이다.



##### 서술적인 이름을 사용하라!

서술적인 이름이 짧고 어려운 이름보다 좋다.

길고 서술적인 이름이 길고 서술적인 주석보다 좋다.



##### 함수 인수

한수에서 이상적인 인수 개수는 0개다. 다음으로 1개고, 다음으로 2개다. 3개는 가능한 피하는 편이 좋다.



###### 많이 쓰는 단항 형식

이벤트 함수는 조심해서 사용한다. 이벤트라는 사실이 코드에 명확히 드러나야 한다.

StringBuffer transform(StringBuffer in)이 void transform(StringBuffer out)보다 좋다.
변환 함수 형식을 따르는 편이 좋다.



###### 플래그 인수

* 플래그 인수는 추하다.
* 함수로 부울 값을 넘기는 관례는 정말로 끔찍하다. 
* 함수가 한꺼번에 여러 가지를 처리한다고 대놓고 공표하는 셈이니까!

render(true) 라는 코드는 헷갈리기 십상이다.
--> renderForSuite()와 renderForSingleTest()라는 함수로 나눠야 마땅하다.



###### 이항 함수

writeField(name)는 writeField(outputStream, name)보다 이해하기 쉽다.
==> outputStream.write(FieldName)으로 호출한다.

assertEquals(expected, actual)의 문제는 expected 다음에 actual이 온다는 순서를 기억해야 한다는 것이다.



###### 삼항 함수

인수가 3개인 함수는 인수가 2개인 함수보다 훨씬 더 이해하기 어렵다.

assertEquals(message, expected, actual)이라는 함수를 살펴보자. 
첫 인수가 expected라고 예상하지 않았는가?



###### 인수 객체

인수가 2-3개 필요하다면 클래스 변수로 선언할 가능성을 짚어본다.

```java
Circle makeCircle(double x, double y, double radius);
Circle makeCircle(Point center, double radius);
```



###### 인수 목록

인수 개수가 가변적인 함수도 필요하다. String.format 메서드가 좋은 예다.

```java
String.format("%s worked %.2f hours.", name, hours);
```



###### 동사와 키워드

write(name)은 '이름'이 무엇이든 '쓴다'는 뜻이다. 좀 나은 이름은 writeField(name)이다.

assertEquals보다 assertExpectedEqualsActual이 더 좋다. 그러면 인수 순서를 기억할 필요가 없다.



###### 부수 효과를 일으키지 마라!

Session.initialize() 같은 시간적인 결합은 혼란을 일으킨다.

시간적 결합이 필요하다면 함수 이름에 분명히 명시한다. --> checkPasswordAndInitializeSession이라는 이름이 훨씬 좋다.



###### 출력 인수

```java
appendFooter(s);
```

이 함수는 무언가에 s를 바닥글로 첨부할까? 아니면 s에 바닥글을 첨부할까?
appendFooter는 다음과 같이 호출하는 방식이 좋다.

```java
report.appendFooter()
```



###### 명령과 조회를 분리하라!

함수는 뭔가를 수행하거나 뭔가에 답하거나 둘 중 하나만 해야 한다.
둘 다 하면 안 된다. 객체 상태를 변경하거나 아니면 객체 정보를 반환하거나 둘 중 하나다.
둘 다 하면 혼란을 초래한다. 예를 들어, 다음 함수를 살펴보자.

```java
public boolean set(String attribute, String value);

if (set("username", "unclebob")) ...
```

username이 unclebob으로 설정되어 있는지 확인하는 코드인가? 아니면 username을 unclebob으로 설정하는 코드인가?

진짜 해결책은 명령과 조회를 분리해 혼란을 애초에 뿌리뽑는 방법이다.

```java
if (attributeExists("username")) {
  setAttribute("username", "unclebob");
  ...
}
```



###### 오류 코드보다 예외를 사용하라!

```java
if (deletePage(page) == E_OK)
```

위 코드는 여러 단계로 중첩되는 코드를 야기한다. 오류 코드를 반환하면 오류 코드를 곧바로 처리해야 한다는 문제에 부딪힌다.
오류 코드 대신 예외를 사용하면 오류 처리 코드가 원래 코드에서 분리되므로 코드가 깔끔해진다.

```java
if (deletePage(page) == E_OK) {
  if (registry.deleteReference(page.name) == E_OK) {
    if (configKeys.deleteKey(page.name.makeKey()) == E_OK) {
      logger.log("page deleted");
    } else {
      logger.log("configKey not deleted");
    }
  } else {
    logger.log("deleteReference from registry failed");
  }
} else {
  logger.log("delete failed");
  return E_ERROR;
}
```

반면 오류 코드 대신 예외를 사용하면 오류 처리 코드가 원래 코드에서 분리되므로 코드가 깔끔해진다.

```java
try {
  deletePage(page);
  registry.deleteKey(page.name.makeKey());
  configKeys.deleteKey(page.name.makeKey());
} catch (Exception e) {
  logger.log(e.getMessage());
}
```



###### Try/Catch 블록 뽑아내기

try/catch 블록은 원래 추하다. 코드 구조에 혼란을 일으키며, 정상 동작과 오류 처리 동작을 뒤섞는다. 그러므로 try/catch 블록을 별도 함수로 뽑아내는 편이 좋다.

```java
public void delete(Page page) {
  try {
    deletePageAndAllReferences(page);
  } catch (Exception e) {
    logError(e);
  }
}
```

```java
private void deletePageAndAllReferences(Page page) throws Exception {
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
  logger.log(e.getMessage());
}
```

위에서 delete함수는 모든 오류 를 처리한다.
실제로 페이지를 제거하는 함수는 deletePageAndAllReferences다.

이렇게 정상 동작과 오류 처리 동작을 분리하면 코드를 이해하고 수정하기 쉬워진다.



###### 오류 처리도 한 가지 작업이다.

함수는 '한 가지' 작업만 해야 한다. 오류 처리도 '한 가지' 작업에 속한다.



###### Error.java 의존성 자석

```java
public enum Error {
    OK,
    INVALID,
    NO_SUCH,
    LOCKED,
    OUT_OF_RESOURCES,
    WAITING_FOR_EVENT;
}
```

위와 같은 클래스는 의존성 자석이다. Error 클래스 변경이 어려워진다.
프로그래머는 재컴파일/재배치가 번거롭기에 새 오류 코드를 정의하고 싶지 않다.

오류 코드 대신 예외를 사용하면 새 예외는 Exception 클래스에서 파생된다.



##### 반복하지 마라!

중복은 소프트웨어에서 모든 악의 근원이다. 많은 원칙과 기법이 중복을 없애거나 제어할 목적으로 나왔다. 

* 관계형 데이터베이스: 정규화
* 객체지향 프로그래밍: 부모 클래스로 몰아 중복을 없앤다.
* 구조적 프로그래밍, AOP, COP 모두 중복 제거 전략이다.



##### 구조적 프로그래밍

함수는 return문이 하나여야 한다.

하지만 함수를 작게 만든다면 간혹 return, break, continue를 여러 차례 사용해도 괜찮다.



##### 함수를 어떻게 짜죠?

소프트웨어를 짜는 행위는 여느 글짓기와 비슷하다.
처음에는 길고 복잡하다. 들여쓰기도 많고 중복된 루프도 많다. 인수목록도 길다. 이름은 즉흥적이고 코드는 중복된다. 하지만 나는 그 서투른 코드를 빠짐없이 테스트하는 단위 테스트 케이스도 만든다.

그런 다음 코드를 다듬고, 함수를 만들고, 이름도 바꾸고, 중복을 제거한다. 메서드를 줄이고 순서를 바꾼다. 전체 클래스를 쪼개기도 한다. 이 와중에도 코드는 항상 단위 테스트를 통과한다.

최종적으로는 이장에서 설명한 규칙을 따르는 함수가 얻어진다. <u>처음부터 탁 짜내지 않는다. 그게 가능한 사람은 없으리라.</u>



##### 결론

대가 프로그래머는 시스템을 구현할 프로그램이 아니라 풀어갈 이야기로 여긴다. 프로그래밍 언어라는 수단을 사용해 좀 더 풍부하고 좀 더 표현력이 강한 언어를 만들어 이야기를 풀어간다. 시스템에서 발생하는 모든 동작을 설명하는 함수 계층이 바로 그 언어에 속한다.





