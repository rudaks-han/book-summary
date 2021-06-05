# 2장 입출금 내역 분석기

## 2.1 도전 과제

입출금 내역을 자동으로 분석해 재정 상태를 더 잘 보여주는 소프트웨어를 개발해달라고 우리에게 부탁했고, 우리는 그 부탁을 수락했다.



## 2.2 목표

* 버그가 발생하는 범위를 줄이는 데 도움을 주는 단일 책임 원칙을 배운다.
* 여러분이 개발하는 코드와 소프트웨어의 품질을 유지하는 데 유용한 응집도와 결합도의 특징도 소개한다.



## 2.3 입출금 내역 분석기 요구사항

[은행 거래 예시]

30-01-2017,-100,Deliveroo
20-01-2017,-50,Tesco
01-02-2017,6000,Salary
02-02-2017,-2000,Royalties
02-02-2017,-4000,Rent
03-02-2017,3000,Tesco
05-02-2017,-30,Cinema

* 은행 입출금 내역의 총 수입과 총 지출은 각각 얼마인가? 결과가 양수인가 음수인가?
* 특정 달엔 몇 건의입출금 내역이 발생했는가?
* 지출이 가장 높은 상위 10건은 무엇인가?
* 돈을 가장 많이 소비하는 항목은 무엇인가?



## 2.4 KISS 원칙

KISS(keep it short and simple) 원칙을 이용해 한 개의 클래스로 구현한다. 아직은 존재하지 않거나 파일 내용을 파싱할 때 발생하는 문제를 해결하기 위한 예외 처리에 신경 쓸 필요는 없다.

```java
public class BankTransactionAnalyzerSimple {
    private static final String RESOURCE = "src/main/resources";

    public static void main(String[] args) throws IOException {
        final Path path = Paths.get(RESOURCE + args[0]);
        final List<String> lines = Files.readAllLines(path);
        double total = 0d;
        for (final String line: lines) {
            final String [] columns = line.split(",");
            final double amount = Double.parseDouble(columns[1]);
            total += amount;
        }

        System.out.println("The total for all transaction is " + total);
    }
}
```

정상 실행되지만 아래와 같은 문제가 발생할 수 있다. 실제 제품으로 출시되었을 때 발생할 만한 문제를 어떻게 처리할 지 고려하는 것이 좋다.

* 파일이 비어있다면
* 데이터에 문제가 있어서 금액을 파싱하지 못한다면?
* 행의 데이터가 완벽하지 않는다면



```java
public static void main(String[] args) throws IOException {
  final Path path = Paths.get(RESOURCE + args[0]);
  final List<String> lines = Files.readAllLines(path);
  double total = 0d;
  final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  for (final String line: lines) {
    final String [] columns = line.split(",");
    final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
    if (date.getMonth() == Month.JANUARY) {
      final double amount = Double.parseDouble(columns[1]);
      total += amount;
    }
  }

  System.out.println("The total for all transaction is " + total);
}
```



### 2.4.1 final 변수

* 지역 변수나 필드는 final로 정의하기 때문에 이 변수에 값을 재할당할 수 없다.
* final 사용에 따른 장단점이 모두 있으므로 final 사용여부는 팀과 프로젝트에 따라 달라진다.



## 2.5 코드 유지보수성과 안티 패턴

코드를 구현할 때는 **코드 유지보수성**을 높이기 위해 노력한다. 이게 무슨 의미일까? 구현하는 코드가 가졌으면 하는 속성을 목록으로 만들어보자.

* 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 한다.
* 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 한다.
* 새로운 기능을 쉽게 추가하거나 기존 기능을 쉽게 제거할 수 이썽야 한다.
* 캡슐화가 잘 되어 있어야 한다. 즉 코드 사용자에게는 세부 구현 내용이 감춰져 있으므로 사용자가 쉽게 코드를 이해하고, 기능을 바꿀 수 있어야 한다.

궁극적으로 개발자의 목표는 현재 만들고 있는 응용프로그램의 복잡성을 관리하는 것이다. 하지만 새로운 요구사항이 생길 때마다 복사, 붙여넣기로 이를 해결하다면 다음과 같은 문제가 생긴다. 이는 효과적이지 않은 해결방법으로 잘 알려져 있으며, **안티 패턴**이라고 부른다.

* 한 개의 거대한 **갓 클래스** 때문에 코드를 이해하기가 어렵다.
* **코드 중복** 때문에 코드가 불안정하고 변화에 쉽게 망가진다.



### 2.5.1 갓 클래스

한 개의 파일에 모든 코드를 구현하다 보면 결국 하나의 거대한 클래스가 탄생하면서 클래스의 목적이 무엇인지 이해하기 어려워진다. 이 거대한 클래스가 모든 일을 수행하기 때문이다.

이런 문제를 **갓 클래스 안티 패턴**이라 부른다. 한 클래스로 모든 것을 해결하는 패턴이다.



### 2.5.2 코드 중복

CSV 대신 JSON 파일로 입력 형식이 바뀐다면 어떻게 될까? 또는 다양한 형식의 파일을 지원해야 한다면 어떨까? 현재 구현은 한 가지 문제만 해결하도록 하드코딩 되어있고, 여러 곳에 이 코드가 중복되어 있어 기존의 기능을 바꾸기가 어렵다. 결과적으로 모든 곳의 코드를 다 바꿔야 하며, 새로운 버그가 발생할 가능성이 커진다.

결론적으로 코드를 간결하게 유지하는 것도 중요하지만, KISS 원칙을 남용하지 않아야 한다. 여러분의 전체 응용프로그램의 설계를 되돌아보고, 한 문제를 작은 개별 문제로 분리해 더 쉽게 관리할 수 있는지 파악해야 한다. 이 과정을 통해 더 이해하기 쉽고, 쉽게 유지보수하며, 새로운 요구 사항도 쉽게 적용하는 결과물을 만들 수 있다.



## 2.6 단일 책임 원칙

단일 책임 원칙(이하 SRP)은 쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 포괄적인 소프트웨어 개발 지침이다.

다음 두 가지를 보완하기 위해 SRP를 적용한다.

* 한 클래스는 한 기능만 책임진다.
* 클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.

SRP는 일반적으로 클래스와 메서드에 적용한다. SRP는 한 가지 특정 동작, 개념, 카테고리와 관련된다. SRP를 적용하면 코드가 바뀌어야 하는 이유가 한 가지로 제한되므로 더 튼튼한 코드를 만들 수 있다.

그럼 어떻게 SRP를 적용할까? 현재 메인 클래스는 여러 책임을 모두 포함하므로 이를 개별로 분리해야 한다.

* 입력 읽기
* 주어진 형식의 입력 파싱
* 결과 처리
* 결과 요약 리포트



[파싱 로직을 추출해 한 클래스로 만듦]

```java
public class BankStatementCSVParser implements BankStatementParser {

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BankTransaction parseFrom(final String line) {
        final String[] columns = line.split(",");

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);

        return new BankTransaction(date, amount, columns[2]);
    }

    public List<BankTransaction> parseLinesFrom(final List<String> lines) {
        return lines.stream().map(this::parseFrom).collect(toList());
    }
}
```

BaseStatementCSVParser 클래스는 parseFromCSV()와 parseLinesFromCSV()라는 BankTransaction 객체를 생성하는 두 클래스를 정의한다. BankTransaction은 도메인 클래스로 입출금 내역을 표현한다.



메서드를 구현할 때는 놀람 최소화 원칙(Principle of least surprise)을 따라야 한다. 그래야 코드를 보고 무슨 일이 일어나는지 명확히 이해할 수 있기 때문이다.

* 메서드가 수행하는 일을 바로 이해할 수 있도록 자체 문서화를 제공하는 메서드명을 사용하낟
* 코드의 다른 부분이 파라미터의 상태에 의존할 수 있으므로 파라미터의 상태를 바꾸지 않는다.



## 2.7 응집도

응집도는 서로 어떻게 관련되어 있는지를 가리킨다. 정확히 말하자면 응집도는 클래스나 메서드의 책임이 서로 얼마나 강하게 연결되어 있는지를 측정한다.

높은 응집도는 개발자의 목표이고, 누구나 쉽게 코드를 찾고, 이해하고, 사용할 수 있도록 만들고 싶어한다.



### 2.7.1 클래스 수준 응집도

실무에서는 일반적으로 다음과 같은 여섯 가지 방법으로 그룹화한다.

* 기능
* 정보
* 유틸리지
* 논리
* 순차
* 시간



### 2.7.2 메서드 수준 응집도

메서드가 다양한 기능을 수행할수록 메서드가 어떤 동작을 하는지 이해하기가 점점 어려워진다. 즉 메서드가 연관이 없는 여러 일을 처리한다면 응집도가 낮아진다. 응집도가 낮은 메서드는 여러 책임을 포함하기 때문에 각 책임을 테스트하기가 어렵고, 메서드의 책임도 테스트하기가 어렵다. 일반적으로 클래스나 메서드 파라미터의 여러 필드를 바꾸는 if/else 블록이 여러 개 포함되어 있다면 이는 응집도에 문제가 있음을 의미하므로 응집도가 높은 더 작은 조각으로 메서드를 분리해야 한다.



## 2.8 결합도

결합도는 한 기능이 다른 클래스에 얼마나 의존하고 있는지를 가늠한다. 결합도는 어떤 클래스를 구현하는 데 얼마나 많은 지식(다른 클래스)을 참조했는가로 설명할 수 있다. 더 많은 클래스를 참조했다면 기능을 변경할 때 그만큼 유연성이 떨어진다. 어떤 클래스의 코드를 바꾸면 이 클래스에 의존하는 모든 클래스가 영향을 받는다.



## 2.9 테스트

### 2.9.1 테스트 자동화

### 2.9.2 제이유닛 사용하기

### 2.9.3 코드 커버리지























# 3장 입출금 내역 분석기 확장판

## 3.3 확장된 입출금 내역 분석기 요구 사항

1. 특정 입출금 내역을 검색할 수 있는 기능. 예를 들어 주어진 날짜 범위 또는 특정 범주의 입출금 내역 얻기.
2. 검색 결과의 요약 통계를 텍스트, HTML 등 다양한 형식으로 만들기



## 3.4 개방/폐쇄 원칙

특정 금액 이상의 모든 입출금 내역을 검색하는 메서드를 구현해보자. '이 메서드를 어디에 정의해야 할까'라는 의문이 먼저 떠오른다.



특정 금액 이상의은행 거래 내역 찾기

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
public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
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



BankTransactionFilter 인터페이스

```java
@FunctionalInterface
public interface BankTransactionFilter {
  boolean test(BankTransaction bankTransaction);
}
```

개방/폐쇠 원칙을 적용한 후 유연해진 findTransactions() 메서드

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

한 인터페이스에 모든 기능을 추가하는 갓 인터페이스를 만드는 일은 피해야 한다.



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

* 자바의 인터페이스는 모든 구현이 지켜야 할 규칙을 정의한다. 즉 구현 클래스는 인터페이스에서 정의한 모든 연산의 구현 코드를 제공해야 한다. 따라서 인터페이스를 바꾸면 구현한 코드도 바뀐 내용을 지원하도록 갱신되어야 한다. 더 많은 연산을 추가할수록 더 자주 코드가 바뀌며, 문제가 발생할 수 있는 범위도 넓어진다.
* 월, 카테고리 같은 BankTransaction의 속성이 calculateAverageForCategory(), calculateTotalInJanuary() 처럼 메서드 이름의 일부로 사용되었다. 인터페이스가 도메인 객체의 특정 접근자에 종속되는 문제가 생겼다. <u>도메인 객체의 세부 내용이 바뀌면 인터페이스도 바뀌어야 하며 결과적으로 구현코드도 바뀌어야 한다</u>.



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
* 암묵적 API
    * findTransactions()와 같이 메서드를 정의한다.
    * 처음에는 사용하기 어렵다.
    * 거래 내역을 검색하는 데 필요한 모든 상황을 단순한 API로 처리할 수 있다.

어떤 것이 좋은 방법인지는 정해져 있지 않다. 질문의 종류에 따라 달라질 수 있기 때문이다.



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

### 3.8.1 예외를 사용해야 하는 이유

고전 적인 C 프로그래밍에서는 수많은 if 조건을 추가해 암호 같은 오류 코드를 반환했다. 코드 부분이 따로 분리되어 이해하기가 어려워진다. 결과적으로 코드를 유지보수하기 어렵다. 어떤 값이 실제 값인지 아니면 오류를 가리키는 값인지 구분하기가 어렵다.

자바는 이런 문제를 해결하도록 예외를 일급 언어 기능으로 추가하고 다음과 같은 장점을 제공한다.

* 문서화: 메서드 시그니처 자체에 예외를 지원한다.
* 형식 안정성: 개발자가 예외 흐름을 처리하고 있는지를 형식 시스템이 파악한다.
* 관심사 분리: 비즈니스 로직과 예외 회복이 각각 try/catch 블록으로 구분된다.

자바는 두 가지 종류의 예외를 지원한다.

* 확인된 예외: 회복해야 하는 대상의 예외다. 자바에서는 메서드가 던질 수 있는 확인된 예외 목록을 선언해야 한다. 아니면 해당 예외를 try/catch로 처리해야 한다.
* 미확인 예외: 프로그램을 실행하면서 언제든 발생할 수 있는 종류의 예외다. 확인된 예외와 달리 메서드 시그니처에 오류를 선언하지 않으면 호출자도 이를 꼭 처리할 필요가 없다.



### 3.8.2 예외의 패턴과 안티 패턴

#### 미확인 예외와 확인된 예외에서 선택하기

문법 예외 던지기

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



```java
public class OverlySpecificBankStatementValidator {
  private String description;
  private String date;
  private String amount;
  
  public OverlySpecificBankStatementValidator(final String description, final String date, final String amount) {
    ...
  }
  
  public boolean validate() throws DescriptionTooLongException, InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
    ...
  }
}
```



#### 과도하게 덤덤함

과한 세밀함과는 정반대로 모든 예외를 IllegalArgumentException 등의 미확인 예외로 지정하는 극단적인 상황도 있다.

```java
public boolean validate() {
  if (this.description.length() > 100) {
    throw new IllegalArumentException("The description is too long");
  }
  
  if (...) {
    throw new IllegalArumentException("...");
  }
  
  if (...) {
    throw new IllegalArumentException("...");
  }
}
```



#### 노티피케이션 패턴

노티피케이션 패턴은 너무 많은 미확인 예외를 사용하는 상황에 적합한 해결책을 제공한다.



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



























# 4장 문서 관리 시스템

## 4.3 문서 관리 시스템 요구 사항

문서 관리 시스템은 기존 환자 정보 파일을 읽어 색인을 추가하고 검색할 수 있는 형태의 정보로 변환해야 한다. 그녀는 다음과같은 세 가지 형식의 문서를 다룬다.

* 리포트: 환자의 수술과 관련된 상담 내용을 기록한 본문이다.
* 우편물: 특정 주소로 발송되는 텍스트 문서다.
* 이미지: 차이와 잇못 엑스레이 사진을 저장한다. 용량이 크다.



## 4.4 설계 작업

### 4.4.1 임포터

파일의 확장자로 파일을 어떻게 임포트할지 결정할 수 있다. 지금까지 우편물은 .letter, 리포트는 .report, 이미지는 .jpg인 전용 확장자를 사용해왔다.

확장자 switch 문 예제

```java
switch (extension) {
  case "letter":
    // 우편물 임포트 코드
    break;
  
  case: "report":
    // 레포트 임포트 코드
    break;
    
  case "jpg":
    //이미지 임포트 코드
    break;
    
  default:
    throw new UnknownFileTypeException("For file: " + path);
}
```

위 코드로 문제를 해결할 수 있지만 확장성은 부족하다. 다른 종류의 파일을 추가할 때마다 switch문에 다른 항목을 추가해 구현해야 하기 때문이다. 



```java
interface Importer {
  Document importFile(File file) throws IOException;
}
```



### 4.4.2 Document 클래스

문서의 종류에 따라 포함하는 속성이 달라진다.

가장 간단한 방법은 Map<String, String>으로 속성 이름을 값과 매핑하는 방법이다. 응용 프로그램에 직접 Map<String, String>을 사용하지 않는 이유가 뭘까?  응용프로그램의 유지보수성과 가동성을 고려해야 하는 일이다.

우선 응용 프로그램의 컴포넌트 이름을 구체적으로 지어야 함의 중요성은 아무리 강조해도 지나치지 않다. 의사소통은 왕이다! 훌륭한 소프트웨어 개발팀은 유비쿼터스 언어로 자신의 소프트웨어를 작성한다. 동료나 고객과 대화할 때 소프트웨어의 다양한 기능을 어떤 공통 언어로 약속한다. 이때 사용한 어휘를 코드로 매핑하면 코드의 어떤 부분을 바꿔야 하는지 쉽게 알 수 있다. 이를 발견성이라 한다.

강한 형식을 이용하면 데이터의 사용 방법을 규제할 수 있다. 예를 들어 Docoment 클래스는 불변 클래스, 즉 클래스를 생성한 다음에는 클래스의 속성을 바꿀 수 없다. 

Document가 HashMap<String, String>을 상속받도록 설계를 결정한 개발자도 있을 것이다. HashMap은 Document 모델링에 필요한 모든 기능을 포함하므로 처음에는 이 결정이 좋아보일 수 있다. 하지만 이런 설계 방법에는 몇 가지 문제가 있다.

소프트웨어를 설계할 때 필요한 기능은 추가하면서 동시에 불필요한 기능은 제한해야 한다. Document 클래스가 HashMap을 상속하면서 응용프로그램이 Document 클래스를 바꿀 수 없도록 결정한다면 이전에 불변성으로 얻을 수 있는 모든 이득이 단번에 사라진다.

요약하자면 도메인 클래스를 이용하면 개념에 이름을 붙이고 수행할 수 있는 동작고 ㅏ값을 제한하므로 발견성을 개선하고 버그 발생 범위를 줄일 수 있다.

```java
public class Document {
  private final Map<String, String> attributes;
  
  Document(final Map<String, String> attributes) {
    this.attributes = attributes;
  }
  
  public String getAttribute(final String attributeName) {
    return attributes.get(attributeName);
  }
}
```



### 4.4.4 임포터 구현과 등록

ImageImporter

```java
class ImageImporter implements Importer {
    @Override
    public Document importFile(final File file) throws IOException {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(PATH, file.getPath());

        final BufferedImage image = ImageIO.read(file);
        attributes.put(WIDTH, String.valueOf(image.getWidth()));
        attributes.put(HEIGHT, String.valueOf(image.getHeight()));
        attributes.put(TYPE, "IMAGE");

        return new Document(attributes);
    }
}
```



임포터 등록

```java
private final Map<String, Importer> extensionToImporter = new HashMap<>();

public DocumentManagementSystem() {
  extensionToImporter.put("letter", new LetterImporter());
  extensionToImporter.put("report", new ReportImporter());
  extensionToImporter.put("jpg", new ImageImporter());
}
```



## 4.5 리스코프 치환 원칙(LSP)

리스코프 치환원칙: 간편하게 자식 클래스는 부모로부터 물려받은 행동을 유지해야 한다고 생각하자.



## 4.6 대안

임포터의 클래스 계층을 만들고 인터페이스 대신 가장 상위에 Importer 클래스를 만든느 방법을 선택할 수도 있다. 언터페이스와 클래스는 서로 다른 기능을 제공한다. 인터페이스는 여러 개를 한 번에 구현할 수 있는 반면, 클래스는 일반 인스턴스 필드와 메서드를 갖는다.

다양한 임포트를 사용하도록 계층을 만든다. 쉽게 망가질 수 있는 상속 기반의 클래스를 피해야 한다고 설명했듯이 인터페이스를 이용하는 것이 클래스를 이용하는 것보다 명백하게 좋은 선택이다.



### 4.6.2 영역, 캡슐화 선택하기



## 4.7 기존 코드 확장과 재사용

### 4.7.1 유틸리티 클래스 사용

가장 간단한 방법은 유틸리티 클래스를 만드는 것이다. ImportUtil 클래스를 만들어 여러 임포트에서 공유해야 하는 기능을 이 유틸리티 클래스에 구현한다.

유틸리티 클래스는 가장 그럭저럭 단순하고 쓸만하지만 객체지향 프로그래밍의 지향점과는 거리가 멀다. 객체지향에서는 클래스로 기능을 마든다. 인스턴스를 만들고 싶다면 무조건 new Thing()을 호출한다.

유틸리티 클래스는 이런 예상을 뒤엎으며 보통 어떤 한 의만 개념과 상관없는 다양한 코드의 모음으로 귀결된다. 시간이 흐를수록 이는 갓 클래스의 모양을 갖춰간다. 즉 여러 의무를 담당하는 한 개의 거대 클래스가 탄생한다.



### 4.7.2 상속 사용

각각의 임포터가 TextImport 클래스를 상속받는 방법이다. TextImporter 클래스에 모든 공통 기능을 구현하고 서브클래스에서는 공통 기능을 재사용한다.

시간이 흐르고 응용프로그램이 바뀔 때, 응용프로그램을 그에 맞게 바꾸는 것보다는 변화를 추상화하는 것이 더 좋다. 일반적으로 상속 관계로 코드를 재사용하는 것은 좋은 방법이 아니다.



### 4.7.3 도메인 클래스 사용

마지막으로 도메인 클래스로 텍스트 파일을 모델링하는 방법이 있다. 먼저 기본 개념을 모델링 한 다음, 기본 개념이 제공하는 메서드를 호출해 다양한 임포터를 만든다. 여기서 기본 개념이 뭘까? 예제에서는 텍스트 파일의 내용을 처리해야 하므로 TextFile 클래스를 사용한다. 새롭거나 창의적이지 않다는 점이 바로 핵심이다.



#### 도메인 클래스 구현

```java
class TextFile {
  private final Map<String, String> attributes;
  private final List<String> lines;
  // 클래스 계속됨...
}
```



## 4.8 테스트 위생

자동화된 테스트는 퇴행(regression)이 발생하는 범위를 줄이며 어떤 동작이 문제를 일으켰는지 이해할 수 있도록 도와준다. 또한 자동화된 테스트가 있으면 자신 있게 코드를 리팩터링할 수 있다. 이런 호화를 누리려면 코드를 많이 구현해보고 유지보수해야 하기 때문이다.

테스트 유지보수 문제를 해결하려면 테스트 위생을 지켜야 한다. 테스트 위생이란 테스트 대상 코드베이스뿐 아니라 테스트 코드도 깔끔하게 유지하며 유지보수하고 개선해야 함을 의미한다.



### 4.8.1 테스트 이름 짓기

이름 짓기에도 여러 안티 패턴이 존재한다. 가령 test1처럼 말도 안 되는 테스트 이름은 최악의 안티 패턴이다. test1은 뭘 테스트하는 걸까?

흔히 발생하는 안티 패턴으로 file, document 처럼 개념이나 명사로 테스트의 이름을 결정하는 것이다. 테스트 이름은 개념이 아니라 테스트하는 동작을 묘사해야 한다. 테스트 중 실행하는 메서드명을 그대로 사용하는 것도 또 다른 안티 패턴이다. 예를 들어 테스트 이름을 importFile로 짓는 실수를 할 수 있다.























# 5장 비즈니스 규칙 엔진

## 5.3 비즈니스 규칙 엔진 요구사항

* **팩트**: 규칙이 확인할 수 있는 정보
* **액션**: 수행하려는 동작
* **조건**: 액션을 언제 발생시킬지 결정
* **규칙**: 실행하려는 비즈니스 규칙을 지정, 보통 팩트, 액션, 조건을 한 그룹으로 묶어 규칙으로 만듦



## 5.6 조건 추가하기

### 5.6.1 상태 모델링

익명 클래스로 액션 추가

```java
final Customer customer = new Customer("Mark", "CEO");

businessRuleEngine.addAction(new Action() {
  @Override
  public void perform() {
    if ("CEO".equals(customer.getJobTitle())) {
      Mailer.sendEmail("sales@company.com", "Relevant customer: " + customer);
    }
  }
});
```

람다 표현식으로 액션 추가

```java
final Customer customer = new Customer("Mark", "CEO");

businessRuleEngine.addAction(() -> {
    if ("CEO".equals(customer.getJobTitle())) {
      Mailer.sendEmail("sales@company.com", "Relevant customer: " + customer);
    }
});
```



Facts 클래스

```java
public class Facts {
  private final Map<String, String> facts = new HashMap<>();
  
  public String getFact(final String name) {
    return this.facts.get(name);
  }
  
  public void addFact(final String name, final String value) {
    this.facts.put(name, value);
  }
}
```



Facts를 인수로 받는 Action 클래스

```java
@FunctionalInterface
public interface Action {
  void perform(Facts facts);
}
```



Facts를 이용한 BusinessRuleEngine

```java
public class BusinessRuleEngine {
  private final List<Action> actions;
  private final Facts facts;
  
  public BusinessRuleEngine(final Facts facts) {
    this.facts = facts;
    this.actions = new ArrayList();
  }
  
  public void addAction(final Action action) {
    this.actions.add(action);
  }
  
  public int count() {
    return this.actions.size();
  }
  
  public void run() {
    this.actions.forEach(action -> action.perform(facts));
  }
}
```



Facts를 이용하는 액션

```java
businessRuleEngine.addAction(facts -> {
  final String jobTitle = facts.getFacts("jobTitle");
  if ("CEO".equals(jobTitle)) {
    final String name = facts.getFact("name");
    Mailer.sendMail("sales@company.com", "Relevant customer:" + name);
  }
})
```



### 5.6.3 switch 문

다양한 거래 상태를 나타내는 enum

```java
public enum Stage {
  LEAD, INTERESTED, EVALUATING, CLOSED
}
```



특정 거래의 예상치를 계산하는 규칙

```java
businessRuleEngine.addAction(facts -> {
  var forecastedAmount = 0.0;
  var dealStage = Stage.valueOf(facts.getFact("stage"));
  var amount = Double.parseDouble(facts.getFact("amount"));
  if (dealStage == Stage.LEAD) {
    forcastedAmount = amount * 0.2;
  } else if (dealStage == Stage.EVALUATING) {
    forcastedAmount = amount * 0.5;
  } else if (dealStage == Stage.INTERESTED) {
    forcastedAmount = amount * 0.8;
  } else if (dealStage == Stage.CLOSED) {
    forcastedAmount = amount;
  }
  facts.addFact("forecastedAmount", String.valueOf(forecastedAmount));
});
```

switch문을 이용하면 조금 더 깔끔하게 코드를 정리할 수 있다.

```java
switch (dealStage) {
  case LEAD:
    forcastedAmount = amount * 0.2;
    break;
  case EVALUATING:
    forcastedAmount = amount * 0.5;
    break;
  case INTERESTED:
    forcastedAmount = amount * 0.8;
    break;
  case CLOSED:
    forcastedAmount = amount;
    break;
}
```



### 5.6.4 인터페이스 분리 원칙

```java
public interfact ConditionAction {
  boolean evaluate(Facts facts);
  void perform(Facts facts);
}
```



## 5.7 플루언트 API 설계

### 5.7.1 플루언트 API란

플루언트 API란 특정 문제를 더 직관적으로 해결할 수 있도록 특정 도메인에 맞춰진 API를 가리킨다. 플루언트 API의 메서드 체이닝을 이용하면 더 복잡한 연산도 지정할 수 있다.



### 5.7.2 도메인 모델링

'어떤 조건이 주어졌을 때(when)', '이런 작업을 한다(then)' 같은 간단한 조합을 규칙으로 지정할 수 있게 한다. 

* 조건: 어떤 팩트에 적용할 조건(참이나 거짓으로 평가됨).
* 액션: 실행할 연산이나 코드 집합
* 규칙: 조건과 액션을 합친 것. 조건이 참일 때만 액션을 실행한다.



```java
@FunctionalInterface
public interface Condition {
  boolean evaluate(Facts fact);
}
```



```java
@FunctionalInterface
interface Rule {
  void perform(Facts facts);
}

public class DefaultRule implements Rule {
  private final Condition condition;
  private final Action action;
  
  public Rule(final Condition condition, final Action action) {
    this.condition = condition;
    this.action = action;
  }
  
  public void perform(final Facts facts) {
    if (condition.evaluate(facts)) {
      actions.execute(facts);
    }
  }
}
```



규칙 만들기

```java
final Condition condition = (Facts facts) -> "CEO".equals(facts.getFact("jobTitle"));
final Action action = (Facts facts) -> {
  var name = facts.getFact("name");
  Mailer.sendEmail("sales@company.com", "Relavant customer!!!: " + name);
}

final Rule rule = new DefaultRule(condition, action);
```



### 5.7.3 빌더 패턴

규칙을 만드는 빌더 패턴

```java
public class RuleBuilder {
  private Condition condition;
  private Action action;
  
  public RuleBuilder when(final Condition condition) {
    this.condition = condition;
    return this;
  }
  
  public RuleBulder then(final Action action) {
    this.action = action;
    return this;
  }
  
  public Rule createRule() {
    return new DefaultRule(condition, action);
  }
}
```



RuleBuilder 사용

```java
Rule rule = new RuleBuilder()
  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
  .then(facts -> {
    var name = facts.getFact("name");
    Mailer.sendEmail("sales@company.com", "Relavant customer: " + name);
  })
```



























# 6장 트우터

## 6.4 설계 개요

어떤 사용자는 집에 있는 데스크톰으로 트우터 웹사이트에 접속할 수 있고 어떤 사용자는 스마트폰으로 트우터를 실행할 수 있기 때문이다. 다양한 환경의 사용자가 어떻게 서로 통신할 수 있을까?

보통 소프트웨어 개발자는 이런 문제를 클라이언트 서버 모델로 해결한다.



### 6.4.1 풀 기반

풀 기반 통신에서는 클라이언트가 서버로 정보를 요청한다.



### 6.4.2 푸시 기반

푸시 기반 통신 방법도 있다. 이를 리액티브 또는 이벤트 주도 통신이라고 부른다.



## 6.5 이벤트에서 설계까지

### 6.5.1 통신

* 웹소켓은 TCP 스트림으로 양방향 이벤트 통신을 지원하는 가벼운 통신 프로토콜이다.
* 요즘은 아마존의 단순 큐 서비스(SQS) 같은 호스트된 클라우드 기반 메시지 큐를 메시지 송출이나 수신에 점점 많이 사용한다.
* 메시지 전송이나 메시지 큐를 구현하는 오픈 소스인 Aeron, ZeroMQ, AMPQ로 구현하는 방법도 있다.



### 6.5.2 GUI

### 6.5.3 영구 저장

트우터는 수신한 데이터를 어떻게 저장할까?

* 직접 인덱스하고 검색할 수 있는 일반 텍스트 파일
* 전통적인 SQL 데이터베이스
* NoSQL 데이터베이스



### 6.4.2 육각형 아키텍처

앨리스터 콕번이 정립한 포트와 어댑터 또는 육각형 아키텍처라 불리는 조금 더 일반화된 아키텍처를 적용해 이 문제를 해결할 수 있다.

코어 비즈니스 로직과 분리하려는 특정 기술이 있다면 포트를 이용한다. 외부 이벤트는 포트를 통해 코어 비즈니스 로직으로 전달된다. 어댑터는 포트로 연결하는 특정 기술을 이요한 구현 코드다.































