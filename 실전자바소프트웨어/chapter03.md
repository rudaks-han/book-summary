# 3장 입출금 내역 분석기 확장판

## 3.3 확장된 입출금 내역 분석기 요구 사항

1. 특정 입출금 내역을 검색할 수 있는 기능. 예를 들어 주어진 날짜 범위 또는 특정 범주의 입출금 내역 얻기.
2. 검색 결과의 요약 통계를 텍스트, HTML 등 다양한 형식으로 만들기



## 3.4 개방/폐쇄 원칙

특정 금액 이상의 모든 입출금 내역을 검색하는 메서드를 구현해보자.

* '이 메서드를 어디에 정의해야 할까'라는 의문이 먼저 떠오른다.
* findTransactions() 메서드를 포함하는 BankTransactionFinder 클래스를 따로 만들 수 있다.
* 하지만 2장에서 이미 BankTransactionProcessor 클래스를 선언했다.
* BankTransactionProcessor 안에 정의하면 나중에 관련 메서드를 조금 더 쉽게 찾을 수 있다.



특정 금액 이상의 은행 거래 내역 찾기

```java
public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
  return findTransactions(bankTransaction -> bankTransaction.getAmount() >= amount);
}
```

특정 월의 입출금 내역 찾기

```java
public double calculateTotalInMonth(final Month month) {
  return summarizeTransactions((acc, bankTransaction) ->
                               bankTransaction.getDate().getMonth() == month ? acc + bankTransaction.getAmount() : acc);
}
```

특정 월이나 금액으로 입출금 내역 검색하기

```java
public List<BankTransaction> findTransactionsGreaterThanEqual(final Month month, final int amount) {
  final List<BankTransaction> result = new ArrayList();
  for (final BankTransaction bankTransaction: bankTransactions) {
    if (bankTransaction.getDate().getMonth() == month && bankTransaction.getAmount() > = amount) {
      result.add(bankTransaction);
    }
  }
  return result;
}
```

확실히 이 방식에는 여러 한계가 있다.

* 거래 내역의 여러 속성을 조합할수록 코드가 점점 복잡해진다.
* 반복 로직과 비즈니스 로직이 결합되어 분리가 어려워진다.
* 코드를 반복한다.

개방/폐쇄 원칙은 이런 상황에 적용한다. 

* 개방/폐쇄 원칙을 적용하면 코드를 직접 바꾸지 않고 해당 메서드나 클래스의 동작을 바꿀 수 있다.



BankTransactionFilter 인터페이스

```java
@FunctionalInterface
public interface BankTransactionFilter {
  boolean test(BankTransaction bankTransaction);
}
```

개방/폐쇄 원칙을 적용한 후 유연해진 findTransactions() 메서드

```java
public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
  for (final BankTransaction bankTransaction: bankTransactions) {
      if (bankTransactionFilter.test(bankTransaction)) {
        result.add(bankTransaction);
      }
   }
  return result;
}
```



### 3.4.1 함수형 인터페이스 만들기

BankTransactionFilter를 구현하는 클래스 선언

```java
class BankTransactionIsInFebruaryAndExpensive implements BankTransactionFilter {
  @Override
  public boolean test(BankTransaction bankTransaction) {
    return bankTransaction.getDate().getMonth() == month 
      && bankTransaction.getAmount() > = 1_000;
  }
}
```

특정 BankTransactionFilter 구현으로 findTransactions() 호출

```java
final List<BankTransaction> transactions = bankStatementProcessor.findTransactions(new BankTransactionIsInFebruaryAndExpensive());
```



### 3.4.2 람다 표현식

하지만 새로운 요구사항이 있을 때마다 별도의 클래스를 만들어야 한다. 이는 큰 의미가 없는 코드를 반복해서 만드는 귀찮은 작업이다.

람다 표현식으로 BankTransactionFilter 구현하기

```java
final List<BankTransaction> transactions = bankStatementProcessor.findTransactions(bankTransaction -> bankTransaction.getDate().getMonth() == month 
      && bankTransaction.getAmount() > = 1_000);
```

요약하자면, 다음과 같은 장점 덕분에 개방/폐쇄 원칙을 사용한다.

* 기존 코드를 바꾸지 않으므로 기존 코드가 잘못될 가능성이 줄어든다.
* 코드가 중복되지 않으므로 기존 코드의 재사용성이 줄어든다.
* 결합도가 낮아지므로 코드 유지보수성이 좋아진다.



## 3.5 인터페이스 문제

* 2장에서 구현한 서로 다른 세 개의 메서드를 어떻게 처리하느냐의 문제가 남았다.
    * calculateTotalAmount()
    * calculateTotalInMonth()
    * calculateTotalForCategory()

* 한 인터페이스에 모든 기능을 추가하는 갓 인터페이스를 만드는 일은 피해야 한다.



### 3.5.1 갓 인터페이스

갓 인터페이스

```java
interface BankTransactionProcessor {
  double calculateTotalAmount();
  double calculateTotalInMonth(Month month);
  double calculateTotalInJanuary();
  double calculateAverageAmount();
  double calculateAverageAmountForCategory(Category category);
  List<BankTransaction> findTransactions(BankTransactionFilter bankTransactionFilter);
}
```

모든 헬퍼 연산이 명시적인 API 정의에 포함되면서 인터페이스가 복잡해진다.

* 자바의 인터페이스는 모든 구현이 지켜야 할 규칙을 정의한다. 즉 구현 클래스는 인터페이스에서 정의한 모든 연산의 구현 코드를 제공해야 한다. 따라서 인터페이스를 바꾸면 구현한 코드도 바뀐 내용을 지원하도록 갱신되어야 한다. 더 많은 연산을 추가할수록 더 자주 코드가 바뀌며, 문제가 발생할 수 있는 범위도 넓어진다.
* 월, 카테고리 같은 BankTransaction의 속성이 calculateAverageForCategory(), calculateTotalInJanuary() 처럼 메서드 이름의 일부로 사용되었다. 인터페이스가 도메인 객체의 특정 접근자에 종속되는 문제가 생겼다. <u>도메인 객체의 세부 내용이 바뀌면 인터페이스도 바뀌어야 하며 결과적으로 구현코드도 바뀌어야 한다</u>.

이런 이유에서 보통 작은 인터페이스를 권장한다. 그래야 도메인 객체의 다양한 내부 연산으로의 디펜던시를 최소화할 수 있다.



### 3.5.2 지나친 세밀함

인터페이스는 작을수록 좋은 걸까? 아래는 각 동작을 별도의 인터페이스로 정의하는 극단적인 예다. BankTransactionProcessor 클래스는 이 모든 인터페이스를 구현해야 한다.

지나치게 세밀한 인터페이스

```java
interface CalculateTotalAmount {
  double calculateTotalAmount();
}

interface CalculateAverage {
  double calculateAverage();
}

interface CalculateTotalInMonth {
  double calculateTotalInMonth(Month month);
}
```

지나치게 인터페이스가 세밀해도 코드 유지보수에 방해가 된다. 실제로 위 예제는 안티 응집도 문제가 발생한다. 즉 기능이 여러 인터페이스로 분산되므로 필요한 기능을 찾기가 어렵다. 자주 사용하는 기능을 쉽게 찾을 수 있어야 유지보수성이 좋아진다.



## 3.6 명시적 API vs 암묵적 API

* 명시적 API
    * findTransactionsGreaterThanEqual() 처럼 구체적으로 메서드를 정의하는 방법
    * 어떤 동작을 수행하는지 잘 설명되어 있다.
    * 사용하기 쉽다.
    * API의 가독성을 높이고 쉽게 이해하도록 메서드 이름을 만들었다.
    * 하지만 이 메서드의 용도가 특정 상황에 국한되어 각 상황에 맞는 새로운 메서드를 많이 만들어야 하는 상황이 벌어진다.
* 암묵적 API
    * findTransactions()와 같이 메서드를 정의한다.
    * 처음에는 사용하기 어렵다.
    * 거래 내역을 검색하는 데 필요한 모든 상황을 단순한 API로 처리할 수 있다.

어떤 것이 좋은 방법인지는 정해져 있지 않다. 질문의 종류에 따라 달라질 수 있기 때문이다.



BankTransactionProcessor 클래스의 핵심 연산

```java
@FunctionalInterface
public interface BankTransactionSummarizer {
  double summarize(double accumulator, BankTransaction bankTransaction);
}

@FunctionalInterface
public interface BankTransactionFilter {
  boolean test(BankTransaction bankTransaction);
}

public class BankTransactionProcessor {
	// ...
}
```



### 3.6.1 도메인 클래스 vs 원싯값

BankTransactionSummarizer의 인터페이스를 간단하게 정의하면서 double이라는 원싯값을 결과로 반환하는데, 이는 일반적으로 좋은 방법이 아니다. 원싯값으로는 다양한 결과를 반환할 수 없어 유연성이 떨어지기 때문이다.

double을 감싸는 새 도메인 클래스 Summary를 만들면 이 문제를 해결할 수 있다.



## 3.7 다양한 형식으로 내보내기

선택한 입출금 목록의 요약 통계를 텍스트, HTML, JSON 등 다양한 형식으로 내보내야 한다.



### 3.7.1 도메인 객체 소개

요약 정보를 저장하는 도메인 객체

```java
public class SummaryStatistics {
    private final double sum;
    private final double max;
    private final double min;
    private final double average;

    public SummaryStatistics(final double sum, final double max, final double min, final double average) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.average = average;
    }

    public double getSum() {
        return sum;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getAverage() {
        return average;
    }
}
```



### 3.7.2 적절하게 인터페이스를 정의하고 구현하기

Exporter 인터페이스의 나쁜 예

```java
public interface Exporter {
  void export(SummaryStatistics summaryStatistics);
}
```

인터페이스를 이렇게 정의하면 다음과 같은 문제가 발생한다.

* void 반환 형식은 아무 도움이 되지 않고, 기능을 파악하기도 어렵다.
* void를 반환하면 어서션으로 결과를 테스트하기도 매우 어렵다.

Exporter 인터페이스의 좋은 예

```java
public interface Exporter {
  String export(SummaryStatistics summaryStatistics);
}
```



## 3.8 예외 처리

* 입출금 내역 분석기 소프트웨어가 아래와 같은 오작동을 일으킨다면 어떻게 해야 할까?

    * 데이터를 적절하게 파싱하지 못한다면?
    * 입출금 내역을 포함하는 CSV 파일을 읽을 수 없다면?
    * 응용프로그램을 실행하는 하드웨어에 램이나 저장 공간이 부족하다면?

    

### 3.8.1 예외를 사용해야 하는 이유

고전적인 C 프로그래밍에서는 수많은 if 조건을 추가해 암호 같은 오류 코드를 반환했다. 코드 부분이 따로 분리되어 <u>이해하기가 어려워진다</u>. 결과적으로 코드를 유지보수하기 어렵다. 어떤 값이 실제 값인지 아니면 오류를 가리키는 값인지 구분하기가 어렵다.

자바는 이런 문제를 해결하도록 예외를 일급 언어 기능으로 추가하고 다음과 같은 장점을 제공한다.

* **문서화**: 메서드 시그니처 자체에 예외를 지원한다.
* **형식 안정성**: 개발자가 예외 흐름을 처리하고 있는지를 형식 시스템이 파악한다.
* **관심사 분리**: 비즈니스 로직과 예외 회복이 각각 try/catch 블록으로 구분된다.

자바는 두 가지 종류의 예외를 지원한다.

* **확인된 예외**: 회복해야 하는 대상의 예외다. 자바에서는 메서드가 던질 수 있는 확인된 예외 목록을 선언해야 한다. 아니면 해당 예외를 try/catch로 처리해야 한다.
* **미확인 예외**: 프로그램을 실행하면서 언제든 발생할 수 있는 종류의 예외다. 확인된 예외와 달리 메서드 시그니처에 오류를 선언하지 않으면 호출자도 이를 꼭 처리할 필요가 없다.

Error와 RuntimeException 클래스는 미확인 예외이며 throwable의 서브클래스다. 보통 이런 오류는 잡지 않는다.

Exception 클래스는 일반적으로 프로그램에서 잡아 회복해야 하는 오류를 가리킨다.



### 3.8.2 예외의 패턴과 안티 패턴

#### 미확인 예외와 확인된 예외에서 선택하기

CSV 파일에 잘못된 문법이 포함될 수 있다.

```java
final String [] columns = line.split(",");

if (columns.length < EXPECTED_ATTRIBUTES_LENGTH) {
  throw new CSVSyntaxException();
}
```

CSVSyntaxException은 확인된 예외로 사용해야 할까, 아니면 미확인 예외로 사용해야 할까?

보통 비즈니스 검증 로직 시 발생하는 문제는 불필요한 try/catch 구문을 줄일 수 있도록 미확인 예외로 결정한다. 시스템 오류도 미확인 예외로 지정한다. 즉, 대다수의 예외를 미확인 예외로 지정하고 꼭 필요한 상황에서만 확인된 예외로 지정해 불필요한 코드를 줄여야 한다.



#### 과도하게 세밀함

그렇다면 어디에 검증 로직을 추가해야 할까? 전용 Validator 클래스를 만드는 것을 권한다.

* 검증 로직을 재사용해 코드를 중복하지 않는다.
* 시스템의 다른 부분도 같은 방법으로 검증할 수 있다.
* 로직을 독립적으로 유닛 테스트할 수 있다.
* 이 기법은 프로그램 유지보수와 이해하기 쉬운 SRP를 따른다.



* 예외를 활용해 다양한 방법으로 검증자를 구현할 수 있다. 예제는 과도하게 자세한 방법이다. 
* 이 방법을 적용하면 각각의 예오에 적합하고 정확한 회복 기법을 구현할 수 있지만 너무 많은 설정 작업이 필요하고, 여러 예외를 선언해야 하며, 사용자가 이 모든 예외를 처리해야 하므로 생산성이 현저하게 떨어진다. 다시 말해 사용자가 API를 쉽게 사용할 수 없게 된다.

과도하게 세밀한 예외

```java
public class OverlySpecificBankStatementValidator {
  private String description;
  private String date;
  private String amount;
  
  public OverlySpecificBankStatementValidator(final String description, final String date, final String amount) {
    ...
  }
  
  public boolean validate() throws DescriptionTooLongException, InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
    if (this.description.length() > 100) {
      throw new DescriptionTooLongException();
    }
    
    final LocalDate parsedDate;
    try {
      parsedDate = LocalDate.parse(this.date);
    } catch (DateTimeParseException e) {
      throw new InvalidDateFormat();
    }
    
    if (parsedDate.isAfter(LocalDate.nowI())) throw new DateInTheFutureException();
    
    try {
      Double.parseDouble(this.amount);
    } catch (NumberFormatException e) {
      throw new InvalidAmountException();
    }
    
    return true;
  }
}
```



#### 과도하게 덤덤함

과한 세밀함과는 정반대로 모든 예외를 IllegalArgumentException 등의 미확인 예외로 지정하는 극단적인 상황도 있다.

```java
public boolean validate() {
  if (this.description.length() > 100) {
    throw new IllegalArgumentException("The description is too long");
  }
  
  final LocalDate parsedDate;
    try {
      parsedDate = LocalDate.parse(this.date);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid format for date", e);
    }
    
    if (parsedDate.isAfter(LocalDate.nowI())) throw new IllegalArgumentException("date cannot be in the future");
    
    try {
      Double.parseDouble(this.amount);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid format for amount", e);
    }
    
    return true;
  }
}
```



#### 노티피케이션 패턴

노티피케이션 패턴은 너무 많은 미확인 예외를 사용하는 상황에 적합한 해결책을 제공한다.

오류를 수집하는 도메인 클래스 Notification

```java
public class Notification {
  private final List<String> errors = new ArraysList<>();
  
  public void addError(final String message) {
    errors.add(message);
  }
  
  public boolean hasErrors() {
    return !errors.isEmpty();
  }
  
  public String errorMessage() {
    return errors.toString();
  }

  public List<String> getErrors() {
    return this.errors;
  }
}
```



노티피케이션 패턴

```java
public Notification validate() {
  final Notification notification = new Notification();
  if (this.description.length() > 100) {
    notification.addError("The description is too long");
  }
  
   final LocalDate parsedDate;
   try {
      parsedDate = LocalDate.parse(this.date);
    } catch (DateTimeParseException e) {
      notification.addError("Invalid format for date");
    }
    
    if (parsedDate.isAfter(LocalDate.nowI())) {
		notification.addError("date cannot be in the future");
    }
    
    try {
      Double.parseDouble(this.amount);
    } catch (NumberFormatException e) {
      notification.addError("Invalid format for amount");
    }
  
     return notification;
}
```



### 3.8.4 예외 대안 기능

#### null 사용

예외를 던지지 않고 null을 반환하면 어떨까?

```java
final String [] columns = line.split(",");

if (columns.length < EXPECTED_ATTRIBUTES_LENGTH) {
  return null;
}
```

이 방법은 절대 사용하지 않아야 한다. API 결과가 null인지 항상 확인해야 하므로 오류가 쉽게 발생할 수 있다.



#### null 객체 패턴



#### Optional< T >



## 3.9 빌드 도구 사용

### 3.9.1 왜 빌드 도구를 사용할까?

빌드 도구는 다음과 같은 다양한 장점을 제공한다.

* 프로젝트에 적용되는 공통적인 구조를 제공하기 때문에 동료 가밸자가 여러분의 프로젝트를 좀 더 편안하게 받아들인다.
* 응용프로그램을 빌드하고 실행하는 반복적이고, 표준적인 작업을 설정한다.
* 저수준 설정과 초기화에 들이는 시간을 절약하므로 개발에만 집중할 수 있다.
* 잘못된 설정이나 일부 빌드 과정 생략 등으로 발생하는 오류의 범위를 줄인다.
* 공통 빌드 작업을 재사용해 이를 다시 구현할 필요가 없으므로 시간을 절약한다.



### 3.9.2 메이븐 사용

### 3.9.3 그레이들 사용



### 3.10 총정리

* 개방/폐쇄 원칙을 이용하면 코드를 바꾸지 않고도 메서드나 클래스의 동작을 바꿀 수 있다.
* 개방/폐쇄 원칙을 이용하면 기존 코드를 바꾸지 않으므로 코드가 망가질 가능성이 줄어들며, 기존 코드의 재사용성을 높이고, 결합도가 높아지므로 코드 유지보수성이 개선된다.
* 많은 메서드를 포함하는 갓 인터페이스는 복잡도와 결합도를 높인다.
* 너무 세밀한 메서드를 포함하는 인터페이스는 응집도를 낮춘다.
* API의 가독성을 높이고 쉽게 이해할 수 있도록 메서드 이름을 서술적으로 만들어야 한다.
* 연산 결과로 void를 반환하면동작을 테스트하기가 어렵다.
* 자바의 예외는 문서화, 형식 안정성, 관심사 분리를 촉진한다.
* 확인된 예외는 불필요한 코드를 추가해야 하므로 되도록 사용하지 않는다.
* 너무 자세하게 예외를 적용하면 소프트웨어 개발의 생산성이 떨어진다.
* 노티피케이션 패턴을 이용하면 도메인 클래스로 오류를  수집할 수 있다.
* 예외를 무시하거나 일반적인 Exception을 잡으면 근본적이 문제를 파악하기가 어렵다.
* 빌드 도구를 사용하면 응용프로그램 빌드, 테스트, 배포 등 소프트웨어 개발 생명 주기 작업을 자동화할 수 있다.
* 요즘 자바 커뮤니티에서는 빌드 도구로 메이븐과 그레이들을 주로 사용한다.



























