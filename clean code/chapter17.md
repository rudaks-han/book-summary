# 17장 냄새와 휴리스틱

마틴 파울러의 Refactoring에서 '코드 냄새'와 내가 맡은 냄새를 추가했다.



##### 주석

###### C1: 부적절한 정보

다른 시스템에 (예를 들어, 소스 코드 관리 시스템, 버그 추적 시스템, 이슈 추적 시스템, 기타 기록 관리 시스템에) 저장할 정보는 주석으로 적절하지 못하다.
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

또 다른 예가 함수 서명만 달랑 기술하는 javadoc이다.


```java
/**
  * @param sellRequest
  * @return
  * @throws ManagedComponentException
  */
public SellResponse beginSellItem(SellRequest sellRequest) throws ManagedComponentException
```



###### C4: 성의 없는 주석

작성할 가치가 있는 주석은 잘 작성할 가치도 있다. 주석을 달 참이라면 시간을 들여 최대한 멋지게 작성한다. 단어를 신중하게 선택한다. 문법과 구두점을 올바로 사용한다. 주절대지 않는다. 당연한 소리를 반복하지 않는다. 간결하고 명료하게 작성한다.



###### C5: 주석 처리된 코드

코드를 읽다가 주석으로 처리된 코드가 줄줄이 나오면 신경이 아주 거슬린다. 얼마나 오래된 코드인지, 중요한 코드인지 아닌지, 알 길이 없다. 그럼에도 아무도 삭제하지 않는다. 누군가에게 필요하거나 다른 사람이 사용할 코드라 생각하기 때문이다.

그래서 코드는 그 자리에 남아 매일매일 낡아간다. 더 이상 존재하지 않는 함수를 호출한다. 이름이 바뀐 변수를 사용한다. 더 이상 사용하지 않는 표기법을 따른다. 자신이 포함된 모듈을 오염시킨다. 읽는 사람을 헷갈리게 만든다.

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

모든 단위 테스트는 한 명령으로 돌려야 한다. IDE에서 버튼 하나로 모든 테스트를 돌린다면 가장 이상적이다.



##### 함수

###### F1: 너무 많은 인수

함수에서 인수 개수는 작을수록 좋다. 아예 없으면 가장 좋다. 다음으로 하나, 둘, 셋이 차례로 좋다. 넷 이상은 그 가치가 아주 의심스러우므로 최대한 피한다.



###### F2: 출력 인수

일반적으로 독자는 인수를 입력으로 간주한다. 함수에서 뭔가의 상태를 변경해야 한다면 함수가 속한 객체의 상태를 변경한다.



###### F3: 플래그 인수

boolean 인수는 함수가 여러 기능을 수행한다는 명백한 증거다. 플래그 인수는 혼란을 초래하므로 피해야 마땅하다.



###### F4: 죽은 함수

아무도 호출하지 않는 함수는 삭제한다. 죽은 코드는 낭비다. 과감히 삭제하라.



##### 일반

###### G1: 한 소스 파일에 여러 언어를 사용한다

소스 파일 하나에 언어 하나만 사용하는 방식이 가장 좋다. 현실적으로는 여러 언어가 불가피하다. 하지만 최대한 줄이도록 애써야 한다.



###### G2: 단연한 동작을 구현하지 않는다

최소 놀람의 원칙(The Principle of Least Surprise)에 의거해 함수나 클래스는 다른 프로그래머가 당연하게 여길 만한 동작과 기능을 제공해야 한다.

```java
Day day = DayDate.StringToDay(String dayName);
```

함수가 'Monday'를 Day.MONDAY로 변환하리라 기대한다. 일반적인 요일 약어도 변환하리라 기대한다. 대소문자는 당연히 구분하지 않으리라 기대한다.



###### G3: 경계를 올바로 처리하지 않는다

부지런함을 대신할 지름길은 없다. 모든 경계 조건, 모든 구석진 곳, 모든 예외는 우아하고 직관적인 알고리즘을 좌초시킬 암초다. 스스로의 직관에 의존하지 마라. 모든 경계 조건을 찾아내고, 모든 경계 조건을 테스트하는 테스트 케이스를 작성하라.



###### G4: 안전 절차 무시

체르노빌 원전 사고는 책임자가 안전 절차를 차례로 무시하는 바람에 일어났다. 실험을 수행하기에 번거롭다는 이유에서였다.

안전 절차를 무시하면 위험하다. 컴파일러 경고 일부를 꺼버리면 빌드가 쉬워질지 모르지만 자칫하면 끝없는 디버깅에 시달린다.



###### G5: 중복

이 책에 나오는 가장 중요한 규칙 중 하나이므로 심각하게 숙고하기 바란다.

* 데이비드 토머스와 앤디 헌트는 이를 DRY(Don't Repeat Yourself) 원칙이라 부른다.
* 켄트 벡은 익스트림 프로그래밍의 핵심 규칙 중 하나로 선언한 후 "한 번, 단 한 번만(Once, and only once)"이라 명명했다.
* 롤 제프리스는 이 규칙을 "모든 테스트를 통과한다"는 규칙 다음으로 중요하게 꼽았다.



코드에서 중복을 발견할 때마다 추상화할 기회로 간주하라. 중복된 코드를 하위 루틴이나 다른 클래스로 분리하라.
이렇듯 추상화로 중복을 정리하면 설계 언어의 어휘가 늘어난다. 추상화 수준을 높였으므로 구현이 빨라지고 오류가 적어진다.

* 가장 뻔한 유형은 똑같은 코드가 여러 차례 나오는 중복이다.
    * 간단한 함수로 교체한다.
* switch/case나 if/else 문으로 확인하는 중복이다.
    * 다형성으로 대체해야 한다.
* 알고리즘은 유사하나 코드가 서로 다른 중복이다.
    * template method 패턴이나 strategy 패턴으로 제거

사실 최근 15년 동안 나온 디자인 패턴은 대다수가 중복을 제거하는 잘 알려진 방법에 불과하다.



###### G6: 추상화 수준이 올바르지 못하다

추상화는 저차원 상세 개념에서 고차원 일반 개념을 분리한다. 모든 저차원 개념을 파생 클래스에 넣고, 모든 고차원 개념은 기초 클래스에 넣는다.

예를 들어, 세부 구현과 관련된 상수, 변수, 유틸리티 함수는 기초 클래스에 넣으면 안 된다. 기초 클래스는 구현 정보에 무지해야 마땅하다.



###### G7: 기초 클래스가 파생 클래스에 의존한다

개념을 기초 클래스와 파생 클래스로 나누는 가장 흔한 이유는 고차원 기초 클래스 개념을 저차원 파생 클래스 개념으로부터 분리해 독립성을 보장하기 위해서다. 일반적으로 기초 클래스는 파생 클래스를 아예 몰라야 마땅하다.



###### G8: 과도한 정보

우수한 소프트웨어 개발자는 클래스나 모듈 인터페이스에 노출할 함수를 제한할 줄 알아야 한다. 클래스가 제공하는 메서드 수는 작을수록 좋다. 함수가 아는 변수 수도 작을수도 좋다.

* 자료를 숨겨라
* 유틸리티 함수를 숨겨라
* 상수와 임시 변수를 숨겨라
* 메서드나 인스턴스 변수가 넘쳐나는 클래스는 피하라.
* 하위 클래스에서 필요하다는 이유로 protected 변수나 함수를 마구 생성하지 마라.
* 인터페이스를 매우 작게 그리고 매우 깐깐하게 만들어라.
* 정보를 제한해 결합도를 낮춰라.



###### G9: 죽은 코드

죽은 코드는 실행되지 않는 코드를 가리킨다.
불가능한 조건을 확인하는 if 문과 throw 문이 없는 try 문에서 catch 블록이 좋은 예다. 아무도 호출하지 않는 유틸리티 함수와 switch/case 문에서 불가능한 case 조건도 또 다른 좋은 예다.

죽은 코드는 시간이 지나면 악취를 풍기기 시작한다. 죽은 지 오래될수록 악취는 강해진다. 죽은 코드는 설계가 변해도 제대로 수정되지 않기 때문이다.



###### G10: 수직 분리

변수와 함수는 사용되는 위치에 가깝게 정의한다. 지역변수는 처음으로 사용하기 직전에 선언하여 수직으로 가까운 곳에 위치해야 한다. 선언한 위치로부터 몇백 줄 아래에서 사용하면 안 된다.

비공개 함수는 처음으로 호출한 직후에 정의한다. 비공개 함수는 처음으로 호출되는 위치를 찾은 후 조금만 아래로 내려가면 쉽게 눈에 띄어야 한다.



###### G11: 일관성 부족

어떤 개념을 특정 방식으로 구현했다면 유사한 개념도 같은 방식으로 구현한다. 앞서 언급한 '최소 놀람의 법칙(The Principle of Least Surprise)'에도 부합한다. 표기법은 신중하게 선택하며, 일단 선택한 표기법은 신중하게 따른다.

한 함수에서 response라는 변수에 HttpServletResponse 인스턴스를 저장했다면 다른 함수에서도 일관성 있게 동일한 변수 이름을 사용한다. 한 메서드를 processVerificationRequest라 명명했다면 다른 메서드도 ProcessDeletionRequest처럼 유사한 이름을 사용한다.



###### G12: 잡동사니

비어있는 기본 생성자가 왜 필요한가? 쓸데없이 코드만 복잡하게 만든다. 아무도 사용하지 않는 변수, 아무도 호출하지 않는 함수, 정보를 제공하지 못하는 주석 등이 좋은 예다. 모두가 코드만 복잡하게 만들 뿐이므로 제거해야 마땅하다. 소스 파일은 언제나 깔끔하게 정리하라! 잡동사니를 없애라!



###### G13: 인위적 결합

서로 무관한 개념을 인위적으로 결합하지 않는다. 예를 들어, 일반적인 enum은 특정 클래스에 속할 이유가 없다. enum이 클래스에 속한다면 enum을 사용하는 코드가 특정 클래스를 알아야 한다.

일반적으로 인위적인 결합은 직접적인 상호작용이 없는 두 모듈 사이에 일어난다. 뚜렷한 목적 없이 변수, 상수, 함수를 당장 편한 위치에 넣어버린 결과다.

함수, 상수, 변수를 선언할 때는 시간을 들여 올바른 위치를 고민한다. 그저 당장 편한 곳에 선언하고 내버려두면 안 된다.



###### G14: 기능 욕심

마틴 파울러가 말하는 코드 냄새 중 하나다. 클래스 메서드는 자기 클래스의 변수와 함수에 관심을 가져야지 다른 클래스의 변수와 함수에 관심을 가져서는 안된다.

```java
public class HourlyPayCalculator {
  public Money calculateWeeklyPay(HourlyEmployee e) {
    int tenthRate = e.getTenthRate().getPennies();
    int tenthsWorked = e.getTenthsWorked();
    int straightTime = Math.min(400, tenthsWorked);
    int overTime = Math.max(0, tenthsWorked - straightTime);
    int straightPay = straightTime * tenthRate;
    int overtimePay = (int) Math.round(overTime*tenthRate*1.5);
    return new Money(straightPay + overtimePay);
  }
}
```

calculateWeeklyPay 메서드는 HourlyEmployee 객체에서 온갖 정보를 가져온다. 

기능 욕심은 한 클래스의 속사정을 다른 클래스에 노출하므로, 별다른 문제가 없다면, 제거하는 편이 좋다. 하지만 때로는 어쩔 수 없는 경우도 생긴다. 다음 코드를 살펴보자.

```java
public class HourlyEmployeeReport {
  private HourlyEmployee employee;
  
  public HourlyEmployeeReport(HourlyEmployee e) {
    this.employee = e;
  }
  
  String reportHours() {
    return String.format(
      "Name: %s\tHours:%d.%1d\n",
      employee.getName(),
      employee.getTenthsWorked()/10,
      employee.getTenthsWorked()%10
    )
  }
}
```
확실히 reportHours 메서드는 HourlyEmployee 클래스를 욕심낸다. 하지만 그렇다고 HourlyEmployee 클래스가 보고서 형식을 알 필요는 없다. HourlyEmployee가 보고서 형식과 결합되므로 보고서 형식이 바뀌면 클래스도 바뀐다.




###### G15: 선택자 인수

함수 호출 끝에 달리는 false 인수만큼이나 밉살스런 코드도 없다. 도대체 무슨 뜻인가? true로 바꾸면 뭐가 달라지는가? 선택자(selector) 인수는 목적을 기억하기 어려울 뿐 아니라 각 선택자 인수가 여러 함수를 하나로 조합한다. 선택자 인수는 큰 함수를 작은 함수 여럿으로 쪼개지 않으려는 게으름의 소산이다.

```java
public int calculateWeeklyPay(boolean overtime) {
  int tenthRate = getTenthRate();
  int tenthsWorked = getTenthsWorked();
  int straightTime = Math.min(400, tenthsWorked);
  int overTime = Max.max(0, tenthsWorked - straightTime);
  int straightPay = straightTime * tenthRate;
  double overtimeRate = overtime ? 1.5 : 1.0 * tenthRate;
  int overtimePay = (int)Math.round(overtime*overtimeRate);
  return straightPay + overtimePay;
}
```

초과근무 수당을 1.5배로 지급하면 true고 아니면 false다. 독자는 calculateWeeklyPay(false)라는 코드를 발견할 때마다 의미를 떠올리느라 골치를 앓는다. 안타깝게도 저자는 다음과 같이 코드를 구현할 기회를 놓쳤다.

```java
public int straightPay() {
  return getTenthsWorked() * getTenthRate();
}

public int overTimePay() {
  int overTimeTenths = Math.max(0, getTenthsWorked() - 400);
  int overTimePay = overTimeBonus(overTimeTenths);
  return straightPay() + overTimePay;
}

private int overTimeBonus(int overTimeTenths) {
  double bonus = 0.5 * getTenthRate() * overTimeTenths;
  return (int) Math.round(bonus);
}
```

물론 부울 인수만이 문제라는 말은 아니다. enum, int 등 함수 동작을 제어하려는 인수는 하나 같이 바람직하지 않다. 일반적으로 인수를 넘겨 동작을 선택하는 대신 새로운 함수를 만드는 편이 좋다.



###### G16: 모호한 의도

코드를 짤 때는 의도를 최대한 분명히 밝힌다. 행을 바꾸지 않고 표현한 수식, 헝가리식 표기법, 매직 번호 등은 모두 저자의 의도를 흐린다.

```java
public int m_otCalc() {
  return iThsWkd * iThsRte +
    (int) Math.round(0.5 * iThsRte * 
		Math.max(0, iThsWkd - 400)
	);
}
```



###### G17: 잘못 지운 책임

소프트웨어 개발자가 내리는 가장 중요한 결정 중 하나가 코드를 배치하는 위치다.

예를 들어, 직원이 근무한 총 시간을 보고서로 출력하는 함수가 필요하다 가정하자. 보고서를 출력하는 함수에서 총계를 계산하는 방법이 있다. 아니면 근무 시간을 입력 받는 코드에서 총계를 보관하는 방법이 있다.

어느 쪽이든 상관없지만 이런 사실을 반영해 함수 이름을 제대로 지어야 한다. (computeRunningTotalOfHours)



###### G18: 부적절한 static 함수

Math.max(double a, double b)는 좋은 static 메서드다. 특정 인스턴스와 관련된 기능이 아니다. new Math().max(a, b)나 a.max(b)라 하면 오히려 우습다. Math.max 메서드를 재정의할 가능성은 거의 아니 전혀 없다.

간혹 우리는 static으로 정의하면 안 되는 함수를 static으로 정의한다.

```java
HourlyPayCalculator.calculatePay(employee, overtimeRate);
```

언뜻 보면 static 함수로 여겨도 적당하다. 특정 객체와 관련이 없으면서 모든 정보를 인수에서 가져오니까. 하지만 함수를 재정의할 가능성이 존재한다. 예를 들어 OvertimeHourlyPayCalculator와 StraightHourlyPayCalculator를 분리하고 싶을지도 모른다. 그러므로 위 함수는 static 함수로 정의하면 안 된다. Employee 클래스에 속하는 인스턴스 함수여야 한다.



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

서술적인 변수 이름은 많이 써도 괜찮다. 일반적으로 많을수록 더 좋다.



###### G20: 이름과 기능이 일치하는 함수

```java
Date newDate = date.add(5);
```

5일을 더하는 함수인가? 아니면 5주? 5시간? date 인스턴스를 변경하는 함수인가? 아니면 예전 date 인스턴스는 그대로 두고 새로운 Date를 반환하는 함수인가? 코드만 봐서는 알 수가 없다.

Date 인스턴스에 5일을 더해 date 인스턴스를 변경하는 함수라면 addDaysTo 혹은 increaseByDays라는 이름이 좋다. 반면, date 인스턴스는 변경하지 않으면서 5일 뒤인 새 날짜를 반환한다면 daysLater나 DaysSince라는 이름이 좋다.



###### G21: 알고리즘을 이해하라

대다수 괴상한 코드는 사람들이 알고리즘을 충분히 이해하지 않은 채 코드를 구현한 탓이다. 잠시 멈추고 실제 알고리즘을 고민하는 대신 여기저기 if 문과 플래그를 넣어보며 코드를 돌리는 탓이다.

알고리즘을 안다고 생각하지만 실제는 코드가 '돌아갈'때까지 이리저리 찔러보고 굴려본다. '돌아간다'는 사실은 어떻게 아냐고? 생각할 수 있는 테스트 케이스를 모두 통과하니까.

이 방식이 틀렸다는 말이 아니다. 사실상 대다수 상황에서는 원하는 대로 함수를 돌리는 유일한 방법이다. 하지만 '돌아간다'고 말하기는 뭔가 부족하다.

구현이 끝났다고 선언하기 전에 함수가 돌아가는 방식을 확실히 이해하는지 확인하라. 테스트 케이스를 모두 통과한다는 사실만으로 부족하다. 작성자가 알고리즘이 올바르다는 사실을 알아야 한다.

알고리즘이 올바르다는 사실을 확인하고 이해하려면 기능이 뻔히 보일 정도로 함수를 깔끔하고 명확하게 재구성하는 방법이 최고다.



###### G22: 논리적 의존성은 물리적으로 드러내라

한 모듈이 다른 모듈에 의존한다면 물리적인 의존성도 있어야 한다. 논리적인 의존성만으로는 부족하다.

```java
public class HourlyReporter {
  private HourlyReporFormatter formatter;
  private List<LineItem> page;
  private final int PAGE_SIZE = 55;
  
  ...
}
```

위 코드는 논리적인 의존성이 존재한다. 무엇인지 알겠는가? 바로 PAGE_SIZE라는 상수다. 어째서 HourlyReporter 클래스가 페이지 크기를 알아야 하는가? 페이지 크기는 HourlyReportFormatter가 책임질 정보다.

HourlyRepoter 클래스는 HourlyReportFormatter가 페이지 크기를 알 거라고 가정한다. 바로 이 가정이 논리적 의존성이다.

HourlyReportFormatter에 getMaxPageSize()라는 메서드를 추가하면 논리적인 의존성이 물리적인 의존성으로 변한다. HourlyReporter 클래스는 PAGE_SIZE 상수를 사용하는 대신 getMaxPageSize() 함수를 호출하면 된다.



###### G23: If/Else 혹은 Switch/Case 문보다는 다형성을 사용하라

* 대다수 개발자가 switch문을 사용하는 이유는 그 상황에서 가장 올바른 선택이기보다는 당장 손쉬운 선택이기 때문이다. 그러므로 switch를 선택하기 전에 다형성을 먼저 고려하라는 의미다.
* 유형보다 함수가 더 쉽게 변하는 경우는 드물다. 그러므로 모든 switch문을 의심해야 한다.

나는 'switch 문 하나' 규칙을 따른다. 즉, 선택 유형 하나에는 switch 문을 한번만 사용한다. 같은 선택을 수행하는 다른 코드에서는 다형성 객체를 생성해 switch 문을 대신한다.



###### G24: 표준 표기법을 따르라

팀은 업계 표준에 기반한 구현 표준을 따라야 한다. 구현 표준은 인스턴스 변수 이름을 선언하는 위치, 클래스/메서드/변수 이름을 정하는 방법, 괄호를 넣는 위치 등을 명시해야 한다. 표준을 설명하는 문서는 코드 자체로 충분해야 하며 <u>별도 문서를 만들 필요는 없어야 한다.</u>

팀이 정한 표준은 팀원들 모두가 따라야 한다. 실제 괄호를 넣는 위치는 중요하지 않다. <u>모두가 동의한 위치에 넣는다는 사실이 중요하다.</u>



###### G25: 매직 숫자는 명명된 상수로 교체하라

예를 들어, 86,400이라는 숫자는 SECONDS_PER_DAY라는 상수 뒤로 수긴다. 쪽 당 55줄을 인쇄한다면 숫자 55는 LINES_PER_PAGE 상수 뒤로 숨긴다.

어떤 상수는 이해하기 쉬우므로, 코드 자체가 자명하다면, 상수 뒤로 숨길 필요가 없다.

```java
double milesWalked = feetWalked/5280.0;
int dailyPay =. ourlyRate * 8;
double circumference =. adius * Math.PI * 2;
```

위 예제에서 FEET_PER_MILE, WORK_HOURS_PER_DAY, TWO라는 상수가 반드시 필요할까? 어떤 공식은 그냥 숫자를 쓰는 편이 훨씬 좋다.

'매직 숫자'라는 용어는 단지 숫자만 의미하지 않는다. 의미가 분명하지 않은 토큰을 모두 가리킨다.

```java
assertEquals(7777, Employee.find("john Doe").employeeNumber());
```

위 코드는 다음과 같이 해석된다.

```java
assertEquals(
  HOURLY_EMPLOYEE_ID, 
  Employee.find("john Doe").employeeNumber());
```



###### G26: 정확하라

* 검색 결과 중 첫 번째 결과만 유일한 결과로 간주하는 행동은 순진하다(naive).
* 부동소수점으로 통화를 표현하는 행동은 거의 범죄에 가깝다.
* 갱신할 가능성이 희박하다고 잠금과 트랜잭션 관리를 건너뛰는 행동은 아무리 잘 봐줘도 게으름이다.
* List로 선언할 변수를 ArrayList로 선언하는 행동은 지나친 제약이다.
* 모든 변수를 protected로 선언한 코드는 무절제하다.

코드에서 뭔가를 결정할 때는 정확히 결정한다.



###### G27: 관례보다 구조를 사용하라

설계 결정을 강제할 때는 규칙보다 관례를 사용한다. 명명 관례도 좋지만 <u>구조 자체로 강제하면 더 좋다</u>. 예를 들어, enum 변수가 멋진 switch/case 문보다 추상 메서드가 있는 기초 클래스가 더 좋다. switch/case 문을 매번 똑같이 구현하게 강제하기는 어렵지만, 파생 클래스는 추상 메서드를 모두 구현하지 않으면 안 되기 때문이다.



###### G28: 조건을 캡슐화하라

부울 논리는 이해하기 어렵다. 조건의 의도를 분명히 밝히는 함수로 표현하라.

```java
if (shouldBeDeleted(timer))
```

라는 코드는 다음 코드보다 좋다

```java
if (time.hasExpired() && !timer.isReccurent())
```



###### G29: 부정 조건은 피하라

부정 조건은 긍정 조건보다 이해하기 어렵다. 가능하면 긍정 조건으로 표현한다.

```java
if (buffer.shouldCompact())
```

라는 코드가 아래 코드보다 좋다.

```java
if (!buffer.shouldCompact())
```



###### G30: 함수는 한 가지만 해야 한다

함수를 짜다보면 한 함수 안에 여러 단락을 이어, 일련의 작업을 수행하고픈 유혹에 빠진다. 이런 함수는 한 가지만 수행하는 함수가 아니다. 한 가지만 수행하는 좀 더 작은 함수 여럿으로 나눠야 마땅하다.

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

위 코드는 세 가지 임무를 수행한다. 직원 목록을 루프로 돌며, 각 직원의 월급일을 확인하고, 해당 직원에게 월급을 지급한다. 위 함수는 다음 함수 셋으로 나누는 편이 좋다.

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

때로는 시간적인 결합도 필요하다. 하지만 시간적인 결합을 숨겨서는 안 된다.

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

위 코드에서 세 함수가 시행되는 순서가 중요하다. 프로그래머가 reticulateSpines를 먼저 호출하고 saturateGradient를 다음으로 호출하는 바람에 UnsaturatedGradientException 오류가 발생해도 막을 도리가 없다.

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

위 코드는 <u>시간적인 결합</u>을 노출한다. 각 함수가 내 놓는 결과는 다음 함수에 필요하다. 그러므로 순서를 바꿔 호출할 수가 없다.



###### G32: 일관성을 유지하라

코드 구조를 잡을 때는 이유를 고민하라. 그리고 그 이유를 코드 구조로 명백히 표현하라. 구조에 일관성이 없어 보이다면 남들이 맘대로 바꿔도 괜찮다고 생각한다.



###### G33: 경계 조건을 캡슐화하라

경계 조건은 빼먹거나 놓치기 십상이다. 경계 조건은 한 곳에서 별도로 처리한다.

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

함수 내 모든 문장은 추상화 수준이 동일해야 한다. 그리고 그 추상화 수준은 함수 이름이 의미하는 작업보다 한 단계만 낮아야 한다.

HR 태그 예시



###### G35: 설정 정보는 최상위 단계에 둬라

추상화 최상위 단계에 둬야 할 기본값 상수나 설정 관련 상수를 저차원 함수에 숨겨서는 안 된다.



###### G36: 추이적 탐색을 피하라

A가 B를 사용하고 B가 C를 사용한다 하더라도 A가 C를 알아야 할 필요는 없다는 뜻이다. (예를 들어 a.getB().getC().doSomething();은 바람직하지 않다.)

이를 디미터의 법칙이라 부른다. 실용주의 프로그래머들은 '부끄럼 타는 코드 작성'이라고 한다.

다시 말해, 다음과 같은 간단한 코드로 충분해야 한다.

```java
myCollaborator.doSomething();
```



##### 자바

###### J1: 긴 import 목록을 피하고 와일드카드를 사용하라

패키지에서 클래스를 둘 이상 사용한다면 와일드카드를 사용해 패키지 전체를 가져오라.

```java
import package.*;
```

명시적인 import 문은 강한 의존성을 생성하지만 와일드카드는 그렇지 않다. 명시적으로 클래스를 import하면 그 클래스가 반드시 존재해야 한다. 하지만 와일드카드로 패키지를 지정하면 특정 클래스가 존재할 필요는 없다. 그러므로 모듈 간에 결합성이 낮아진다.



###### J2: 상수는 상속하지 않는다

어떤 프로그래머는 상수를 인터페이스에 넣은 다음 그 인터페이스를 상속해 해당 상수를 사용한다.

```java
public class HourlyEmployee extends Employee {
  private int tenthsWorked;
  private double hourlyRate;
  
  public Money calculatePay() {
    int straightTime = Math.min(tenthsWorked, TENTHS_PER_WEEK);
    int overTime = tenthsWorked - straightTime;
    return new Money(hourlyRate * (tenthsWorked + OVERTIME_RATE * overtime));
  }
}
```

TENTHS_PER_WEEK과 OVERTIME_RATE라는 상수는 어디서 왔을까? Employee클래스에서 왔을지도 모른다.

```java
public abstract class Employee implements PayrollConstants {
  public abstract boolean isPayday();
  public abstract Money calculatePay();
  public abstract void deliverPay(Money pay);
}
```

아니다. 상수가 없다. 그렇다면 어디서 왔을까? PayrollConstants라는 인터페이스를 구현한다.

```java
public interface PayrollConstants {
  public static final int TENTHS_PER_WEEK = 400;
  public static final double OVERTIME_RATE = 1.5;
}
```

참으로 끔찍한 관행이다! 상수를 상속 계층 맨 위에 숨겨놨다. 우엑! 상속을 이렇게 사용하면 안 된다. 대신 static import를 사용하라.

```java
import static PayrollConstants.*;

public class HourlyEmployee extends Employee {
  private int tenthsWorked;
  private double hourlyRate;
  
  public Money calculatePay() {
    int straightTime = Math.min(tenthsWorked, TENTHS_PER_WEEK);
    int overTime = tenthsWorked - straightTime;
    return new Money(hourlyRate * (tenthsWorked + OVERTIME_RATE * overtime));
  }
}
```



###### J3: 상수 대 Enum

자바 5는 enum을 제공한다. 마음껏 활용하라. public static final int라는 옛날 기교를 더 이상 사용할 필요가 없다.

한 가지 덧붙이자면, enum 문법을 자세히 살펴보기 바란다. 메서드와 필드도 사용할 수 있다. int보다 훨씬 더 유연하기 서술적인 강력한 도구다.

```java
public enum HourlyPayGrade {
  APPRENTICE {
    public double rate() {
      return 1.0;
    }
  },
  LIEUTENANT_LOURNEYMAN {
    public double rate() {
      return 1.2;
    }
  },
  JOURNEYMAN {
    public double rate() {
      return 1.5;
    }
  },
  MASTER {
    public double rate() {
      return 2.0;
    }
  }
}
```



##### 이름

###### N1: 서술적인 이름을 사용하라

이름은 성급하게 정하지 않는다. 서술적인 이름을 신중하게 고른다. 소프트웨어가 진화하면 의미도 변하므로 선택한 이름이 적합한지 자주 되돌아본다.

단순히 '듣기 좋은' 충고가 아니다. <u>소프트웨어 가독성의 90%는 이름이 결정한다</u>. 그러므로 시간을 들여 현명한 이름을 선택하고 유효한 상태로 유지한다.



###### N2: 적절한 추상화 수준에서 이름을 선택하라

구현을 드러내는 이름은 피하라. 작업 대상 클래스나 함수가 위치하는 추상화 수준을 반영하는 이름을 선택하라. 쉽지 않은 작업이다.



###### N3: 가능하면 표준 명명법을 사용하라

기존 명명법을 사용하는 이름은 이해하기 더 쉽다. 예를 들어, DECORATOR 패턴을 활용한다면 장식하는 클래스 이름에 Decorator라는 단어를 사용해야 한다.

자바에서 객체를 문자열로 변환하는 함수는 toString이라는 이름을 많이 쓴다. 이런 이름은 관계를 따르는 편이 좋다.

에릭 에반스는 이를 프로젝트의 유비쿼터스 언어라 부른다.



###### N4: 명확한 이름

함수나 변수의 목적을 명확히 밝히는 이름을 선택한다.

```java
private String doRename() throws Exception {
  if (refactorReferences)
    renameReferences();
  renamePage();
  
  pathToRename.removeNameFromEnd();
  pathToRename.addNameToEnd(newName);
  return PathParser.render(pathToRename);
}
```

doRename 함수 안에 renamePage라는 함수가 있어 더더욱 모호하다. 이름만으로도 두 함수 사이의 차이점이 드러나는가? 전혀 아니다.

renamePageAndOptionallyAllReferences라는 이름이 더 좋다. 아주 길지만 모듈에서 한 번만 호출된다. 길다는 단점을 서술성이 충분히 메꾼다.



###### N5: 긴 범위는 긴 이름을 사용하라

이름 길이는 범위 길이에 비례해야 한다. 범위가 작으면 아주 짧은 이름을 사용해도 괜찮다. 하지만 범위가 길어지면 긴 이름을 사용한다.

범위가 5줄 안팎이라면 i나 j와 같은 변수 이름도 괜찮다.

```java
private void rollMany(int n, int pins) {
  for (int i=0; i<n; i++)
    g.roll(pins);
}
```

깔끔한 코드다. 오히려 변수 i를 rollCount라고 썼다면 헷갈릴 터이다.



###### N6: 인코딩을 피하라

이름에 유형 정보나 범위 정보를 넣어서는 안 된다. 오늘날 개발 환경에서는 이름 앞에 m_이나 f와 같은 접두어가 불필요하다. 헝가리안 표기법의 오염에서 이름을 보호하라.



###### N7: 이름으로 부수 효과를 설명하라

함수, 변수, 클래스가 하는 일을 모두 기술하는 이름을 사용한다. 이름에 부수효과를 숨기지 않는다.

```java
public ObjectOutputStream getOos() throws IOException {
  if (m_oos == null) {
    m_oos = new ObjectOutputStream(m_socket.getOutputStream());
  }
  return m_oos;
}
```

위 함수는 단순히 "oos"만 가져오지 않는다. 기존에 "oos"가 없으면 생성한다.
그러므로 createOrReturnOos라는 이름이 더 좋다.



##### 테스트

###### T1: 불충분한 테스트

테스트 케이스는 몇 개나 만들어야 충분할까?
테스트 케이스는 잠재적으로 깨질 만한 부분을 모두 테스트해야 한다. 테스트 케이스가 확인하지 않는 조건이나 검증하지 않는 계산이 있다면 그 테스트는 불완전하다.



###### T2: 커버리지 도구를 사용하라!

커버리지 도구는 테스트가 빠뜨리는 공백을 알려준다. 커버리지 도구를 사용하면 테스트가 불충분한 모듈, 클래스, 함수를 찾기가 쉬워진다.



###### T3: 사소한 테스트를 건너뛰지 마라

사소한 테스트는 짜기 쉽다. 사소한 테스트가 제공하는 문서적 가치는 구현에 드는 비용을 넘어선다.



###### T4: 무시한 테스트는 모호함을 뜻한다

때로는 요구사항이 불분명하기에 프로그램이 돌아가는 방식을 확신하기 어렵다. 불분명한 요구사항은 테스트 케이스를 주석으로 처리하거나 테스트 케이스에 @ignore를 붙여 표현한다.



###### T5: 경계 조건을 테스트하라

경계 조건은 각별히 신경 써서 테스트한다. 알고리즘의 중앙 조건은 올바로 짜놓고 경계 조건에서 실수하는 경우가 흔하다.



###### T6: 버그 주변은 철저히 테스트하라

버그는 서로 모이는 경향이 있다. 한 함수에서 버그를 발견했다면 그 함수를 철저히 테스트하는 편이 좋다. 십중팔구 다른 버그도 발견하리라.



###### T7: 실패 패턴을 살펴라

때로는 테스트 케이스가 실패하는 패턴으로 문제를 진단할 수 있다. 테스트 케이스를 최대한 꼼꼼히 짜라는 이유도 여기에 있다. 

간단한 예로, 입력이 5자를 넘기는 테스트 케이스가 모두 실패한다면? 함수 둘째 인수로 음수를 넘기는 테스트 케이스가 실패한다면? 때로는 테스트 보고서에서 빨간색/녹색 패턴만 보고도 "아!"라는 깨달음을 얻는다.



###### T8: 테스트 커버리지 패턴을 살펴라

통과하는 테스트가 실행하거나 실행하지 않는 코드를 살펴보면 실패하는 테스트 케이스의 실패 원인이 드러난다.



###### T9: 테스트는 빨라야 한다

느린 테스트 케이스는 실행하지 않게 된다. 일정이 촉박하면 느린 테스트 케이스를 제일 먼저 건너뛴다. 그러므로 테스트 케이스는 빨리 돌아가게 최대한 노력한다.







