# 3장 입출금 내역 분석기 확장판

## 3.2 목표

* 2장에서는 CSV 형식의 입출금 내역을 분석하는 응용프로그램을 만들었다.
* 이 과정에서 유지보수할 수 있는 코드를 만드는 데 도움이 되는 핵심 디자인 원칙 즉, 단일 책임 원칙, 피해야 할 안티 패턴(갓 클래스와 코드 중복)을 배웠다.
* 코드를 점진적으로 리팩터링하는 과정에서 결합도와 응집도도 배웠다.
* 다양한 종류의 입출금 내역을 검색하고, 여러 포맷을 지원하고, 처리하며 텍스트, HTML 등의 형식으로 리포트를 멋지게 내보내려면 무엇이 필요할까?
* 코드베이스에 유연성을 추가하고 유지보수성을 개선하는 데 도움을 주는 개방/폐쇄 원칙(OCP)을 배운다.
* 언제 인터페이스를 사용해야 좋을지를 설명하는 일반적인 가이드라인과 높은 결합도를 피할 수 있는 기법도 배운다.
* 자바에서 언제 API에 예외를 포함하거나 포함하지 않을지를 결저하는 자바의 예외 처리 방법을 배운다.
* 마지막으로 메이븐, 그레이들 같은 검증된 빌드 도구를 이용해 자바 프로젝트를 시스템적으로 빌드하는 방법도 배워본다.



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
  final List<BankTransaction> result = new ArrayList<>();
  for (final BankTransaction bankTransaction: bankTransactions) {
    if (bankTransaction.getAmount() >= amount) {
      result.add(bankTransaction);
    }
  }
  return result;
}
```

특정 월의 입출금 내역 찾기

```java
public double calculateTotalInMonth(final Month month) {
  final List<BankTransaction> result = new ArrayList<>();
  for (final BankTransaction bankTransaction: bankTransactions) {
    if (bankTransaction.getDate().getMonth() == month) {
      result.add(bankTransaction);
    }
  }
}
```

특정 월이나 금액으로 입출금 내역 검색하기

```java
public List<BankTransaction> findTransactionsInMonthAndGreater(final Month month, final int amount) {
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

> 한 개의 추상 메서드를 포함하는 인터페이스를 함수형 인터페이스라 부르며 자바 8에서 처음 이 용어를 소개했다. @FunctionalInterface 애너테이션을 이용하면 인터페이스의 의도를 더 명확하게 표현할 수 있다.



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

* 한 인터페이스에 모든 기능을 추가하는 갓 인터페이스(god interface)를 만드는 일은 피해야 한다.



### 3.5.1 갓 인터페이스

* 여러분은 BankTransactionProcessor 클래스가 API 역할을 한다고 생각할 수 있다.

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

* 하지만 이 접근 방식에는 몇 가지 문제가 있다.
* 우선 모든 헬퍼 연산이 명시적인 API 정의에 포함되면서 인터페이스가 복잡해진다.
    * 자바의 인터페이스는 모든 구현이 지켜야 할 규칙을 정의한다. 즉 구현 클래스는 인터페이스에서 정의한 모든 연산의 구현 코드를 제공해야 한다. 따라서 인터페이스를 바꾸면 구현한 코드도 바뀐 내용을 지원하도록 갱신되어야 한다. 더 많은 연산을 추가할수록 더 자주 코드가 바뀌며, 문제가 발생할 수 있는 범위도 넓어진다.
    * 월, 카테고리 같은 BankTransaction의 속성이 calculateAverageForCategory(), calculateTotalInJanuary() 처럼 메서드 이름의 일부로 사용되었다. 인터페이스가 도메인 객체의 특정 접근자에 종속되는 문제가 생겼다. <u>도메인 객체의 세부 내용이 바뀌면 인터페이스도 바뀌어야 하며 결과적으로 구현코드도 바뀌어야 한다</u>.

* 이런 이유에서 보통 작은 인터페이스를 권장한다. 그래야 도메인 객체의 다양한 내부 연산으로의 디펜던시를 최소화할 수 있다.



### 3.5.2 지나친 세밀함

* 인터페이스는 작을수록 좋은 걸까? 아래는 각 동작을 별도의 인터페이스로 정의하는 극단적인 예다. BankTransactionProcessor 클래스는 이 모든 인터페이스를 구현해야 한다.

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

* 지나치게 인터페이스가 세밀해도 코드 유지보수에 방해가 된다. 
* 실제로 위 예제는 안티 응집도 문제가 발생한다. 
* 즉 기능이 여러 인터페이스로 분산되므로 필요한 기능을 찾기가 어렵다. 
* 자주 사용하는 기능을 쉽게 찾을 수 있어야 유지보수성이 좋아진다.



## 3.6 명시적 API vs 암묵적 API

* 이 문제를 어떻게 하면 제대로 해결할 수 있을까?
* 개방/폐쇄 원칙을 적용하면 연산에 유연성을 추가하고 가장 공통적인 상황을 클래스로 정의할 수 있다.
* 일반적인 findTransactions() 메서드를 쉽게 정의할 수 있는 상황에서 findTransactionsGreaterThanEqual()처럼 구체적으로 메서드를 정의해야 하는지 의문이 생긴다.
* 이런 딜레마를 명시적 API 제공 vs 암묵적 API 제공 문제라고 부른다.
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
	private final List<BankTransaction> bankTransactions;
  
  public BankTransactionProcessor(final List<BankTransaction> bankTransactions) {
    this.bankTransactions = bankTransactions;
  }
  
  public double summarizeTransactions(final BankTransactionSummarizer bankTransactionSummarizer) {
    //
  }
  
  public double calculateToTotalInMonth(final Month month) {
    //
  }
  
  public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
    //
  }
  
  public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
    //
  }
}
```



### 3.6.1 도메인 클래스 vs 원싯값

* BankTransactionSummarizer의 인터페이스를 간단하게 정의하면서 double이라는 원싯값을 결과로 반환하는데, 이는 일반적으로 좋은 방법이 아니다. 
* 원싯값으로는 다양한 결과를 반환할 수 없어 유연성이 떨어지기 때문이다.
* summarizeTransaction() 메서드는 현재 double을 반환한다.
* 다양한 결과를 포함하도록 메서드 시그니처를 바꾸려면 모든 BankTransactionProcessor의 구현을 바꿔야 한다.
* double을 감싸는 새 도메인 클래스 Summary를 만들면 이 문제를 해결할 수 있다.



## 3.7 다양한 형식으로 내보내기

* 선택한 입출금 목록의 요약 통계를 텍스트, HTML, JSON 등 다양한 형식으로 내보내야 한다.



### 3.7.1 도메인 객체 소개

* 사용자가 어떤 형식으로 내보내고 싶은지 정확하게 파악해야 한다.
* 각각 다음과 같은 장단점이 있다.
    * 숫자
        calculateAverageInMonth 처럼 연산의 반환 결과가 필요한 사용자가 있을 것이다. 이때 결과값은 double이다. double을 반환하면 간단하게 프로그램을 구현할 수있지만 요구 사항이 바뀔 때 유연하게 대처할 수 없다.
    * 컬렉션
        findTransaction()이 반환한느 입출금 목록을 원하는 사용자도 있을 것이다. Iterable을 반환하면 상황에 맞춰서 처리하기 때문에 유연성을 높일 수있다. 유연성은 좋아지지만 오직 컬렉션만 반환해야 한다는 제약이 따른다. 어떻게 하면 목록, 기타 요약 정보 등 다양한 종류의 결과를 반환할 수 있을까?
    * 특별한 도메인 객체
        사용자가 내보려는 요약 정보를 대표하는 SummaryStatistics라는 새로운 개념을 만들 수 있다. 도메인 객체를 이용하면 결합을 깰 수 있다. 새로운 요구사항이 생겨서 추가 정보를 내보내야 한다면 기존 코드를 바꿀 필요 없이 새로운 클래스의 일부로 이를 구현할 수 있다.
    * 더 복잡한 도메인 객체
        Report처럼 조금 더 일반적이며 거래 내역 컬렉션 등 다양한 결과를 저장하는 필드를 포함하는 개념을 만들 수 있다. 사용자의 요구사항이 무엇이며 더 복잡한 정보를 내보내야 하는지 여부에 따라 사용할 도메인 객체가 달라진다.



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

* Exporter라는 인터페이스를 정의해 다양한 내보내기 구현 코드가 다른 코드와 결합하지 않도록 방지한다.
* 이는 개방/폐쇄 원칙으로 다시 연결된다.



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



Exporter 인터페이스 구현

```java
public class HtmlExporter implements Exporter {
  @Override
  public String export(final SummaryStatistics summaryStatistics) {
    //
  }
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



* 예외를 활용해 다양한 방법으로 검증자를 구현할 수 있다. 예제는 <u>과도하게 자세한 방법</u>이다. 
* 이 방법을 적용하면 각각의 예외에 적합하고 정확한 회복 기법을 구현할 수 있지만 너무 많은 설정 작업이 필요하고, 여러 예외를 선언해야 하며, 사용자가 이 모든 예외를 처리해야 하므로 생산성이 현저하게 떨어진다. 다시 말해 사용자가 API를 쉽게 사용할 수 없게 된다.

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



### 3.8.3 예외 사용 가이드라인

#### 예외를 무시하지 않음

* 문제의 근본 원인을 알 수 없다고 예외를 무시하면 안 된다.
* 예외를 처리할 수 있는 방법이 명확하지 않으면 미확인 예외를 대신 던진다.

#### 일반적인 예외는 잡지 않음

* 가능한 구체적으로 예외를 잡으면 가독성이 높아지고 더 세밀하게 예외를 처리할 수 있다.
* 일반적인 Exception은 RuntimeException도 포함한다.

#### 예외 문서화

* API 수준에서 미확인 예외를 포함한 예외를 문서화하므로 API 사용자에게 문제 해결의 실마리를 제공한다.
* @throws 자바독 문법으로 예외를 문서화한 예다.

```java
@throws NoSuchFileException 파일이 존재하지 않을 때
@throws DirectoryNotEmptyException 파일이 디렉터리이고 비어 있지 않아 삭제할 수 없을 때
@throws IOException I/O 오류가 발생했을 때
@throws SecurityException 디폴트 프로바이더를 사용하고 보안 관리자가 설치된 상태에서 파일 삭제 권한 여부를 확인했을 때
```

#### 특정 구현에 종속된 예외를 주의할 것

* 특정 구현에 종속된 예외를 던지면 API의 캡슐화가 깨지므로 주의하자.
* 아래 예제는 read() 메서드 정의는 OracleException을 던질 수 있으므로 이를 사용하는 코드도 오라클에 종속된다.

```java
public String read(final Source source) throws OracleException { ... }
```

#### 예외 vs 제어 흐름

* 예외로 흐름을 제어하지 않는다.
* 예제는 자바에서 예외를 남용하는 나쁜 사례다.
* 이 코드는 예외를 이용해 읽기 작업을 수행하는 루프를 탈출한다.

```java
try {
  while (true) {
    System.out.println(source.read());
  } catch (NoDataException e) {
  }
```

* 이런 종류의 코드는 다음과 같은 여러 문제를 일으키므로 피해야 한다.
* 우선 예외를 처리하느라 불필요한 try/catch 문법이 추가되어 코드의 가독성을 떨어뜨린다.
* 또한 코드의 의도도 이해하기 어려워진다.
* 결론적으로 예외를 정말 던저야 하는 상황이 아니라면 예외를 만들지 말아야 한다.



### 3.8.4 예외 대안 기능

* 그렇다면 예외를 대체할 수 있는 기능이 있을까?



#### null 사용

* 예외를 던지지 않고 null을 반환하면 어떨까?

```java
final String [] columns = line.split(",");

if (columns.length < EXPECTED_ATTRIBUTES_LENGTH) {
  return null;
}
```

* 이 방법은 절대 사용하지 않아야 한다. 
* API 결과가 null인지 항상 확인해야 하므로 오류가 쉽게 발생할 수 있다.



#### null 객체 패턴

* 자바에서는 종종 null 객체 패턴을 사용한다.
* null 레퍼런스를 반환하는 대신에 필요한 인터페이스를 구현하는 객체를 반환하는 기법이다.
* 빈 객체는 아무것도 수행하지 않으므로 동작을 예측하기 쉽다.
* 하지만 이 패턴을 사용하면 데이터에 문제가 있어도 빈 객체를 이용해 실제 문제를 무시할 수 있어 나중에 문제를 해결하기가 더 어려워질 수 있다.

널 객체 패턴: https://johngrib.github.io/wiki/null-object-pattern/



#### Optional< T >

* 값이 없는 상태를 명시적으로 처리하는 다양한 메서드 집합을 제공하므로 버그의 범위를 줄이는 데 큰 도움이 된다.



#### Try< T >

* 성공하거나 실패할 수 있는 연산을 가리키는 Try< T > 데이터 형식도 있다.
* Optional< T >와 비슷하지만 값이 아니라 연산에 적용한다는 점이 다르다.
* 안타깞게도 JDK는 Try< T >를 지원하지 않으므로 외부 라이브러리를 이용해야 한다.



## 3.9 빌드 도구 사용

### 3.9.1 왜 빌드 도구를 사용할까?

응용프로그램을 실행하려면 몇 가지 사항을 고려해야 한다.

* 우선 프로젝트 코드를 구현하고 컴파일해야 한다.
* 여러 파일을 컴파일하는 명령어를 기억하는가?
* 여러 패키지를 컴파일하려면 어떤 명령을 사용해야 할까?
* 다른 자바 라이브러리를 사용한다면 디펜던시를 어떻게 관리할까?
* 프로젝트는 WAR이나 JAR 같은 특정한 형식으로 어떻게 패키징할 수 있을까?



빌드 도구는 다음과 같은 다양한 장점을 제공한다.

* 프로젝트에 적용되는 공통적인 구조를 제공하기 때문에 동료 가밸자가 여러분의 프로젝트를 좀 더 편안하게 받아들인다.
* 응용프로그램을 빌드하고 실행하는 반복적이고, 표준적인 작업을 설정한다.
* 저수준 설정과 초기화에 들이는 시간을 절약하므로 개발에만 집중할 수 있다.
* 잘못된 설정이나 일부 빌드 과정 생략 등으로 발생하는 오류의 범위를 줄인다.
* 공통 빌드 작업을 재사용해 이를 다시 구현할 필요가 없으므로 시간을 절약한다.



### 3.9.2 메이븐 사용

* 메이븐은 2004년에 처음 출시되었고 그 당시에는 XML이 큰 인기였다.



### 3.9.3 그레이들 사용

* 가장 널리 사용되는 도구는 메이븐인데 왜 다른 빌드 도구를 사용할까?
* 메이븐은 XML을 이용하는데 이는 작업하기 귀찮고, 가독성이 떨어진다.
* 그레이들의 가장 큰 장점은 그루비, 코틀린 프로그래밍 언어 등을 이용해 친근한 도메인 특화 언어를 이용해 친근한 도메인 특화 언어(DSL)를 적용한다는 점이다.
* 그레이들은 캐시, 점진적 컴파일 등 빌드 시간을 단축하는 가능도 지원한다.



### 3.10 총정리

* 개방/폐쇄 원칙을 이용하면 코드를 바꾸지 않고도 메서드나 클래스의 동작을 바꿀 수 있다.
* 개방/폐쇄 원칙을 이용하면 기존 코드를 바꾸지 않으므로 코드가 망가질 가능성이 줄어들며, 기존 코드의 재사용성을 높이고, 결합도가 높아지므로 코드 유지보수성이 개선된다.
* 많은 메서드를 포함하는 갓 인터페이스는 복잡도와 결합도를 높인다.
* 너무 세밀한 메서드를 포함하는 인터페이스는 응집도를 낮춘다.
* API의 가독성을 높이고 쉽게 이해할 수 있도록 메서드 이름을 서술적으로 만들어야 한다.
* 연산 결과로 void를 반환하면 동작을 테스트하기가 어렵다.
* 자바의 예외는 문서화, 형식 안정성, 관심사 분리를 촉진한다.
* 확인된 예외는 불필요한 코드를 추가해야 하므로 되도록 사용하지 않는다.
* 너무 자세하게 예외를 적용하면 소프트웨어 개발의 생산성이 떨어진다.
* 노티피케이션 패턴을 이용하면 도메인 클래스로 오류를  수집할 수 있다.
* 예외를 무시하거나 일반적인 Exception을 잡으면 근본적이 문제를 파악하기가 어렵다.
* 빌드 도구를 사용하면 응용프로그램 빌드, 테스트, 배포 등 소프트웨어 개발 생명 주기 작업을 자동화할 수 있다.
* 요즘 자바 커뮤니티에서는 빌드 도구로 메이븐과 그레이들을 주로 사용한다.



