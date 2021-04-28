# 17 냄새와 휴리스틱

##### 주석

###### C1: 부적절한 정보

다른 시스템에 (예를 들어, 소스 코드 관리 시스템, 버그 추적 시스템, 이슈 처적 시스템, 기타 기록 관리 시스템에) 저장할 정보는 주석으로 적절하지 못하다.
일반적으로 작성자, 최종 수정일, SPR(Software Problem Report) 번호 등과 같은 메타 정보만 주석으로 넣는다.
주석은 코드와 설계에 기술적인 설명을 부연하는 수단이다.



###### C2: 쓸모 없는 주석

* 오래된 주석, 엉뚱한 주석, 잘못된 주석은 더 이상 쓸모가 없다.
* 주석은 빨리 낡는다. 
* 쓸모 없어질 주석은 아예 달지 않는 편이 가장 좋다. 
* 쓸모 없어진 주석은 재빨리 삭제하는 편이 가장 좋다.
* 쓸모 없는 주석은 일단 들어가고 나면 코드에서 쉽게 멀어진다.



###### C3: 중복된 주석

코드만으로 충분한데 구구절절 설명하는 주석이 중복된 주석이다.

```java
i++; // i 증가
```

```java
/**
  * @param sellRequest
  * @return
  * @throws ManagedComponentException
  */
public SellResponse beginSellItem(SellRequest sellRequest) throws ManagedComponentException
```



###### C4: 성의 없는 주석

주석을 달 참이라면 시간을 들여 최대한 멋지게 작성한다.



###### C5: 주석 처리된 코드

코드를 읽다가 주석으로 처리된 코드가 줄줄이 나오면 신경이 아주 거슬린다. 얼마나 오래된 코드인지, 중요한 코드인지 아닌지, 알 길이 없다. 그럼에도 마우도 삭제하지 않는다. 누군가에게 필요하거나 다른 사람이 사용할 코드라 생각하기 때문이다.

주석으로 처리된 코드를 발견하면 즉각 지워버려라!



##### 환경

###### E1: 여러 단계로 빌드해야 한다

빌드는 간단히 한 단계로 끝나야 한다. 한 명령으로 전체를 체크아웃해서 한 명령으로 빌드할 수 있어야 한다.

```
svn get mySystem
cd mySystem
ant all
```



##### E2: 여러 단계로 테스트해야 한다

IDE에서 버튼 하나로 모든 테스트를 돌린다면 가장 이상적이다.



##### 함수

###### F1: 너무 많은 인수

함수에서 인수 개수는 작을수록 좋다. 넷 이상은 그 가치가 아주 의심스러우므로 최대한 피한다.



###### F2: 출력 인수



###### F3: 플래그 인수

플래그 인수는 혼란을 초래하므로 피해야 마땅하다.



###### F4: 죽은 함수

아무도 호출하지 않는 함수는 삭제한다. 죽은 코드는 낭비다. 과감히 삭제하라.



##### 일반

###### G1: 한 소스 파일에 여러 언어를 사용한다

소스 파일 하나에 언어 하나만 사용하는 방식이 가장 좋다.



###### G2: 단연한 동작을 구현하지 않는다

최소 놀람의 원칙(The Principle of Least Surprise)에 의거해 함수나 클래스는 다른 프로그래머가 당연하게 여길 만한 동작과 기능을 제공해야 한다.

```java
Day day = DayDate.StringToDay(String dayName);
```

함수가 'Monday'를 Day.MONDAY로 변환하리라 기대한다. 일반적인 요일 약어도 변환하리라 기대한다. 대소문자는 당연히 구분하지 않으리라 기대한다.



###### G3: 경계를 올바로 처리하지 않는다

스스로의 직관에 의존하지 마라. 모든 경계 조건을 찾아내고, 모든 경계 조건을 테스트하는 테스트 케이스를 작성하라.



###### G4: 안전 절차 무시

알전 절차를 무시하면 위험하다.  컴파일러 경고 일부를 꺼버리면 빌드가 쉬워질지 모르지만 자칫하면 끝없는 디버깅에 시달린다.



###### G5: 중복

코드에서 중복을 발견할 때마다 추상화할 기회로 간주하라. 중복된 코드를 하위 루틴이나 다른 클래스로 분리하라.

* 똑같은 코드가 나오는 경우
    * 간단한 함수로 교체한다.
* switch/case나 if/else 문으로 확인하는 중복
    * 다형성으로 대체
* 알고리즘은 유사하나 코드가 서로 다른 중복
    * template method 패턴이나 strategy 패턴으로 제거



###### G6: 추상화 수준이 올바르지 못하다

###### G7: 기초 클래스가 파생 클래스에 의존한다

###### G8: 과도한 정보

우수한 소프트웨어 개발자는 클래스나 모듈 인터페이스에 노출할 함수를 제한할 줄 알아야 한다. 클래스가 제공하는 메서드 수는 작을수록 좋다. 함수가 아는 변수 수도 작을수도 좋다.



###### G9: 죽은 코드

죽은 코드는 실행되지 않는 코드를 가리킨다.



###### G10: 수직 분리

변수와 함수는 사용되는 위치에 가깝게 정의한다. 지역변수는 처음으로 사용하기 직전에 선언



###### G11: 일관성 부족

표기법은 신중하게 선택하며, 일단 선택한 표기법은 신중하게 따른다.



###### G12: 잡동사니

비어있는 기본 생성자가 왜 필요한가? 쓸데없이 코드만 복잡하게 만든다.

아무도 사용하지 않는 변수, 아무도 호출하지 않는 함수, 정보를 제공하지 못하는 주석 ==> 모두 제거되야한다.



###### G13: 인위적 결합



###### G14: 기능 욕심

클래스 메서드는 자기 클래스의 변수오 ㅏ함수에 관심을 가져야지 다른 클래스의 변수와 함수에 관심을 가져서는 안된다.



###### G15: 선택자 인수

선택자 인수는 큰 함수를 작은 함수 여럽으로 쪼개지 않으려는 게으름의 소산이다.



###### G16: 모호한 의도



###### G17: 잘못 지운 책임



###### G18: 부적절한 static 함수

간혹 우리는 static으로 정의하면 안 되는 함수를 static으로 정의한다.

```java
HourlyPayCalculator.calculatePay(employee, overtimeRate);
```

함수를 재정의할 가능성이 존재한다.



###### G19: 서술적 변수

```java
Matcher match = headerPattern.matcher(line);
if (match.find()) {
    String key = match.group(1);
    String value = match.group(2);
    headers.put(key.toLowerCase(), value);
}
```

서술적인 변수 이름을 사용한 탓에 첫 번째로 일치하는 그룹이 키(key)이고 두 번째로 일치하는 그룹이 값(value)이라는 사실이 명확하게 드러난다.

서술적인 변수 이름은 많이 써도 괜찮다.



###### G20: 이름과 기능이 일치하는 함수

```java
Date newDate = date.add(5);
```

5일을 더하는 함수인가? 아니면 5주? 5시간?

5일을 더하는 함수라면 addDaysTo 혹은 increaseByDays라는 이름이 좋다.



###### G21: 알고리즘을 이해하라

구현이 끝났다고 선언하기 전에 함수가 돌아가는 방식을 확실히 이해하는지 확인하라. 테스트 케이스를 모두 통과한다는 사실만으로 부족하다. 작성자가 알고리즘이 올바르다는 사실을 알아야 한다.



###### G22: 논리적 의존성은 물리적으로 드러내라

한 모듈이 다른 모듈에 의존한다면 물리적인 의존성도 있어야 한다.



###### G23: If/Else 혹은 Switch/Case 문보다는 다형성을 사용하라

* 대다수 개발자가 switch문을 사용하는 이유는 그 상황에서 가장 올바른 선택이기보다는 당장 손쉬운 선택이기 때문이다. 그러므로 switch를 선택하기 전에 다형성을 먼저 고려하라는 의미다.
* 유형보다 함수가 더 쉽게 변하는 경우는 드물다. 그러므로 모든 switch문을 의심해야 한다.



###### G24: 표준 표기법을 따르라

팀은 업계 표준에 기반한 구현 표준을 따라야 한다. 구현 표준은 인스턴스 변수 이름을 선언하는 위치, 클래스/메서드/변수 이름을 정하는 방법, 괄호를 넣는 위치 등을 명시해야 한다. 표준을 설명하는 문서는 코드 자체로 충분해야 하며 별도 문서를 만들 필요는 없어야 한다.

팀이 정한 표준은 팀원들 모두가 따라야 한다. 실제 괄호를 넣는 위치는 중요하지 않다.모두가 동의한 위치에 넣는다는 사실이 중요하다.



###### G25: 매직 숫자는 명명된 상수로 교체하라



###### G26: 정확하라

List로 선언할 변수를 ArrayList로 선언하는 행동은 지나친 제약이다.

모든 변수를 protected로 선언한 코드는 무절제하다.



###### G27: 관례보다 구조를 사용하라



###### G28: 조건을 캡슐화하라

```java
if (shouldBeDeleted(timer))
// 라는 코드는 다음 코드보다 좋다
if (time.hasExpired() && !timer.isReccurent())
```



###### G29: 부정 조건은 피하라

부정 조건은 긍정 조건보다 이해하기 어렵다. 가능하면 긍정 조건으로 표현한다.

```java
if (buffer.shouldCompact())
//라는 코드가 아래 코드보다 좋다.
if (!buffer.shouldCompact())
```



###### G30: 함수는 한 가지만 해야 한다

```java
public void pay() {
    for (Employee e: employees) {
        if (e.isPayday()) {
            Money pay = e.calculatePay();
            e.deliverPay(pay);
        }
    }
}
```

위 함수는 다음 함수 셋으로 나누는 편이 좋다.

```java
public void pay() {
    for (Employee e: employees) {
        payIfNeccesary(e);
}
    
private void payIfNeccesary(Employee e) {
    if (e.isPayday(()) {
        calculateAndDeliverPay(e);
    }
}
        
private void calculateAndDeliverPay(Employee e) {
    Money pay = e.calculatePay();
    e.deliverPay();
}
```



###### G31: 숨겨진 시간적인 결합

```java
public class MoogDiver() {
    Gradient gradient;
    List<Spline> splines;
    
    public void dive(String reason) {
        saturateGradient();
        reticulateSpines();
        diveForMoog(reason);
    }
}
```

세 함수가 시행되는 순서가 중요하다.

다음 코드가 더 좋다.

```java
public class MoogDiver() {
    Gradient gradient;
    List<Spline> splines;
    
    public void dive(String reason) {
        Gradient gradient = saturateGradient();
        List<Spline> splines = reticulateSplines(gradient);
        diveForMoog(splines, reason);
    }
}
```



###### G32: 일관성을 유지하라



###### G33: 경계 조건을 캡슐화하라

```java
if (level + 1 < tags.length) {
    parts = new Parse(body, tags, level + 1, offset + endTag);
    body = null;
}
```

level + 1이 두 번 나온다. 이런 경계 조건은 변수로 캡슐화하는 편이 좋다.

```java
int nextLevel = level + 1;
if (nextLevel < tags.length) {
    parts = new Parse(body, tags, nextLevel, offset + endTag);
    body = null;
}
```



###### G34: 함수는 추상화 수준을 한 단계만 내려간다



###### G35: 설정 정보는 최상위 단계에 둬라



###### G36: 추이적 탐색을 피하라

A가 B를 사용하고 B가 C를 사용한다 하더라도 A가 C를 알아야 할 필요는 없다는 뜻이다. (예를 들어 a.getB().getC().doSomething();은 바람직하지 않다)



##### 자바

###### J1: 긴 import 목록을 피하고 와일드카드를 사용하라



###### J2: 상수는 상속하지 않는다



###### J3: 상수 대 Enum

Enum을 마음껏 활용하라.



##### 이름

###### N1: 서술적인 이름을 사용하라

소프트웨어 가독성의 90%는 이름이 결정한다.



###### N2: 적절한 추상화 수준에서 이름을 선택하라



###### N3: 가능하면 표준 명명법을 사용하라



###### N4: 명확한 이름



###### N5: 긴 범위는 긴 이름을 사용하라



###### N6: 인코딩을 피하라



###### N7: 이름으로 부수 효과를 설명하라



##### 테스트

###### T1: 불충분한 테스트

테스트 케이스는 몇 개나 만들어야 충분할까?



###### T2: 커버리지 도구를 사용하라!



###### T3: 사소한 테스트를 건너뛰지 마라



###### T4: 무시한 테스트는 모호함을 뜻한다



###### T5: 경계 조건을 테스트하라



###### T6: 버그 주변은 철저히 테스트하라



###### T7: 실패 패턴을 살펴라



###### T8: 테스트 커버리지 패턴을 살펴라



###### T9ㅖ 테스트는 빨라야 한다







