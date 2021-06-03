# 2 입출금 내역 분석기

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























