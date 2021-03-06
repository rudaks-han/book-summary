# 2장 입출금 내역 분석기

## 2.1 도전 과제

* 요즘은 핀테크 분야가 뜨고 있다.
* 입출금 내역을 자동으로 분석해 재정 상태를 더 잘 보여주는 소프트웨어를 개발해달라고 우리에게 부탁했고, 우리는 그 부탁을 수락했다.



## 2.2 목표

* 좋은 소프트웨어 개발의 기반이 무엇인지 배운다.
* 한 개의 클래스로 문제를 구현해보고, 프로젝트를 진행하면서 바뀌는 요구 사항이나 유지보수에 대응하며 기존 구조의 한계가 무엇인지 확인한다.
* 버그가 발생하는 범위를 줄이는 데 도움을 주는 **단일 책임 원칙**(SRP)을 배운다.
* 여러분이 개발하는 코드와 소프트웨어의 품질을 유지하는 데 유용한 **응집도**와 **결합도**의 특징도 소개한다.



## 2.3 입출금 내역 분석기 요구사항

온라인 인터넷 뱅킹 사이트에서 자신의 거래 내역 파일을 내려받았으며, CSV 형식으로 구성되어 있다.

[은행 거래 예시]

---

30-01-2017,-100,Deliveroo
20-01-2017,-50,Tesco
01-02-2017,6000,Salary
02-02-2017,-2000,Royalties
02-02-2017,-4000,Rent
03-02-2017,3000,Tesco
05-02-2017,-30,Cinema

---

마크는 다음 문제의 답을 원한다.

* 은행 입출금 내역의 총 수입과 총 지출은 각각 얼마인가? 결과가 양수인가 음수인가?
* 특정 달엔 몇 건의 입출금 내역이 발생했는가?
* 지출이 가장 높은 상위 10건은 무엇인가?
* 돈을 가장 많이 소비하는 항목은 무엇인가?



## 2.4 KISS 원칙

* KISS(keep it short and simple) 원칙을 이용해 한 개의 클래스로 구현한다. 
* 아직은 존재하지 않거나 파일 내용을 파싱할 때 발생하는 문제를 해결하기 위한 예외 처리에 신경 쓸 필요는 없다. 예외 처리는 3장에서 설명한다.

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

각 행은 모든 행을 가져온 다음, 각 행에 다음 작업을 수행한다.

* 콤마로 열 분리
* 금액 추출
* 금액을 double로 파싱



정상 실행되지만 아래와 같은 문제가 발생할 수 있다. 실제 제품으로 출시되었을 때 발생할 만한 문제를 어떻게 처리할 지 고려하는 것이 좋다.

* 파일이 비어있다면
* 데이터에 문제가 있어서 금액을 파싱하지 못한다면?
* 행의 데이터가 완벽하지 않는다면



'특정 달엔 몇 건의 입출금 내역이 발생했는가?'라는 두 번째 문제를 살펴보자. 주어진 월을 선택하도록 로직을 바꾼다.

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
* final 사용에 따른 장단점이 모두 있으므로 <u>final 사용여부는 팀과 프로젝트에 따라 달라진다.</u>



## 2.5 코드 유지보수성과 안티 패턴

코드를 구현할 때는 **코드 유지보수성**(code maintainability)을 높이기 위해 노력한다. 이게 무슨 의미일까? 구현하는 코드가 가졌으면 하는 속성을 목록으로 만들어보자.

* 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 한다.
* 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 한다.
* 새로운 기능을 쉽게 추가하거나 기존 기능을 쉽게 제거할 수 있어야 한다.
* **캡슐화**(encapsulation)가 잘 되어 있어야 한다. 즉 코드 사용자에게는 세부 구현 내용이 감춰져 있으므로 사용자가 쉽게 코드를 이해하고, 기능을 바꿀 수 있어야 한다.

이를 평가하는 좋은 방법은 <u>여러분이 어떤 코드를 구현한 후 6개월 뒤 다른 회사로 이직했고</u>, <u>여러분의 동료가 그 코드를 이용해야 하는 상황</u>에 닥쳤다고 가정하는 것이다.

궁극적으로 개발자의 목표는 현재 만들고 있는 응용프로그램의 복잡성을 관리하는 것이다. 하지만 새로운 요구사항이 생길 때마다 복사, 붙여넣기로 이를 해결하다면 다음과 같은 문제가 생긴다. 이는 효과적이지 않은 해결방법으로 잘 알려져 있으며, **안티 패턴**(anti-pattern)이라고 부른다.

* 한 개의 거대한 **갓 클래스**(god class) 때문에 코드를 이해하기가 어렵다.
* **코드 중복**(code duplication) 때문에 코드가 불안정하고 변화에 쉽게 망가진다.



### 2.5.1 갓 클래스

한 개의 파일에 모든 코드를 구현하다 보면 결국 하나의 거대한 클래스가 탄생하면서 <u>클래스의 목적이 무엇인지</u> 이해하기 어려워진다. 이 거대한 클래스가 모든 일을 수행하기 때문이다.

이런 문제를 **갓 클래스 안티 패턴**이라 부른다. 한 클래스로 모든 것을 해결하는 패턴이다.



### 2.5.2 코드 중복

* CSV 대신 JSON 파일로 입력 형식이 바뀐다면 어떻게 될까? 
* 또는 다양한 형식의 파일을 지원해야 한다면 어떨까? 
* 현재 구현은 한 가지 문제만 해결하도록 하드코딩 되어있고, 여러 곳에 이 코드가 중복되어 있어 기존의 기능을 바꾸기가 어렵다. 
* 결과적으로 모든 곳의 코드를 다 바꿔야 하며, 새로운 버그가 발생할 가능성이 커진다.

결론적으로 코드를 간결하게 유지하는 것도 중요하지만, KISS 원칙을 남용하지 않아야 한다. 여러분의 전체 응용프로그램의 설계를 되돌아보고, 한 문제를 작은 개별 문제로 분리해 더 쉽게 관리할 수 있는지 파악해야 한다. 이 과정을 통해 <u>더 이해하기 쉽고, 쉽게 유지보수하며, 새로운 요구 사항도 쉽게 적용하는</u> 결과물을 만들 수 있다.



## 2.6 단일 책임 원칙

**단일 책임 원칙**(이하 SRP)은 쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 포괄적인 소프트웨어 개발 지침이다.

다음 두 가지를 보완하기 위해 SRP를 적용한다.

* 한 클래스는 한 기능만 책임진다.
* 클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.

SRP는 일반적으로 클래스와 메서드에 적용한다. SRP는 한 가지 특정 동작, 개념, 카테고리와 관련된다. SRP를 적용하면 코드가 바뀌어야 하는 이유가 한 가지로 제한되므로 더 튼튼한 코드를 만들 수 있다.

그럼 어떻게 SRP를 적용할까? 현재 메인 클래스는 여러 책임을 모두 포함하므로 이를 개별로 분리해야 한다.

* 입력 읽기
* 주어진 형식의 입력 파싱
* 결과 처리
* 결과 요약 리포트

CSV 파싱 로직을 새로운 클래스로 분리한다. 이를 BankStatementCSVParser라고 부른다.

[파싱 로직을 추출해 한 클래스로 만듦]

```java
public class BankStatementCSVParser implements BankStatementParser {

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BankTransaction parseFromCSV(final String line) {
        final String[] columns = line.split(",");

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);
        final String description = columns[2];

        return new BankTransaction(date, amount, description);
    }

    public List<BankTransaction> parseLinesFromCSV(final List<String> lines) {
      final List<BankTransaction> bankTransactions = new ArrayList();
      for (final String line: lines) {
        bankTransactions.add(parseFromCSV(line));
      }
      return bankTransactions;
    }
}
```

BaseStatementCSVParser 클래스는 parseFromCSV()와 parseLinesFromCSV()라는 BankTransaction 객체를 생성하는 두 클래스를 정의한다. BankTransaction은 도메인 클래스로 입출금 내역을 표현한다.



[입출금 내역 도메인 클래스]

```java
public class BankTransaction {
    private final LocalDate date;
    private final double amount;
    private final String description;

    public BankTransaction(final LocalDate date, final double amount, final String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                date.equals(that.date) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description);
    }
}
```



메서드를 구현할 때는 **놀람 최소화 원칙**(principle of least surprise)을 따라야 한다. 그래야 코드를 보고 무슨 일이 일어나는지 명확히 이해할 수 있기 때문이다.

* 메서드가 수행하는 일을 바로 이해할 수 있도록 자체 문서화를 제공하는 메서드명을 사용한다.
* 코드의 다른 부분이 파라미터의 상태에 의존할 수 있으므로 파라미터의 상태를 바꾸지 않는다.



## 2.7 응집도

응집도는 서로 **어떻게 관련되어 있는지**를 가리킨다. 정확히 말하자면 응집도는 클래스나 메서드의 책임이 서로 얼마나 강하게 연결되어 있는지를 측정한다. 즉 어떤 것이 여기저기에 모두 속해 있는지를 말한다.

<u>높은 응집도는 개발자의 목표이고, 누구나 쉽게 코드를 찾고, 이해하고, 사용할 수 있도록 만들고 싶어한다.</u>



### 2.7.1 클래스 수준 응집도

실무에서는 일반적으로 다음과 같은 여섯 가지 방법으로 그룹화한다.

* 기능
* 정보
* 유틸리지
* 논리
* 순차
* 시간

#### 기능

* BankStatementCSVParser를 구현할 때 기능이 비슷한 메서드를 그룹화했다. 
* parseFrom()과 parseLinesFrom()은 CSV 형식의 행을 파싱한다. 
* 이렇게 함께 사용하는 메서드를 그룹화하면 찾기도 쉽고 이해하기도 쉬우므로 응집도를 높인다.
* 간단한 클래스를 과도하게 만들면 그만큼 생각해야 할 클래스가 많아지므로 코드가 장황해지고 복잡해진다.



#### 정보

* 같은 데이터나 도메인 객체를 처리하는 메서드를 그룹화하는 방법도 있다. 
* 예를 들어 BankTransaction 객체를 만들고, 읽고, 갱신하고, 삭제하는 기능(CRUD)이 필요해 이런 기능만 제공하는 클래스를 만들어야 한다.
* 네 개의 다른 메서드를 관련 정보로 응집하는 클래스를 구현한다.
* 정보 응집은 여러 기능을 그룹화하면서, 필요한 일부 기능을 포함하는 클래스 전체를 디펜던시로 추가한다는 약점이 있다.

> 이 유형은 테이블이나 특정 도메인 객체를 저장하는 데이터베이스와 상호작용할 때 흔히 볼수 있다. 이 패턴을 보통 데이터 접근 객체(DAO)라 부르며 객체를 식별하는 일종의 ID가 필요하다. 기본적으로 DAO는 영구 저장 데이터베이스나 인메모리 데이터베이스 같은 데이터 소스로의 접근을 추상화하고 캡슐화한다.



#### 유틸리티

* 때로는 관련성이 없는 메서드를 한 클래스로 포함시켜야 한다. 
* 특히 메서드가 어디에 속해야 할지 결정하기 어려울 때는 만능 스위스 군용 칼과 같은 **유틸리티 클래스**에 추가하기도 한다.
* 유틸리티 클래스 사용은 낮은 응집도로 이어지므로 자제해야 한다.
* 유틸리티 클래스는 관련성이 없는 여러 메서드를 명확하지 않은 기준으로 그룹화하므로 이 원칙을 거스른다.



> utility vs helper
>
> https://stackoverflow.com/questions/12192050/what-are-the-differences-between-helper-and-utility-classes/54396742



#### 논리

* CSV, JSON, XML의 자료를 파싱하는 코드를 구현해보자.

```java
public class BankTransactionParser {
  public BankTransaction parseFromCSV(final String line) {
    // ...
    throw new UnsupportedOperationException();
  }
  
  public BankTransaction parseFromJSON(final String line) {
    // ...
    throw new UnsupportedOperationException();
  }
  
  public BankTransaction parseFromXML(final String line) {
    // ...
    throw new UnsupportedOperationException();
  }
}
```

* 예제에서 네 개의 메서드는 '파싱'이라는 논리로 그룹화되었다. 
* 하지만 이들은 본질적으로 다르며 네 메서드는 서로 관련이 없다. 
* 또한 이렇게 그룹화하면, 클래스는 네 가지 책임을 갖도록 되므로 이전에 배웠던 SRP를 위배한다. 
* 결과적으로 이 방법은 권장하지 않는다.



#### 순차

* 파일을 읽고, 처리하고, 정보를 저장하는 메서드들은 한 클래스로 그룹화한다. 
* 파일을 읽은 결과는 파싱의 입력이 되고, 파싱의 결과는 처리 과정의 입력이 되는 등의 과정이 반복된다.
* 입출력이 순차적으로 흐르는 것을 순차 응집이라 부른다. 
* 안타깝게도 실전에서는 순차 응집을 적용하면 한 클래스를 바꿔야 할 여러 이유가 존재하므로 SRP를 위배한다.
* 더욱이 데이터를 처리, 요약, 저장하는 방법이 다양하므로 결국 이 기법은 클래스의 순식간에 복잡하게 만든다.



#### 시간

* 시간 응집 클래스는 여러 연산 중 시간과 관련된 연산을 그룹화한다. 
* 어떤 처리 작업을 시작하기 전과 뒤에 초기화, 뒷정리 작업(데이터베이스 연결과 종료)을 담당하는 메서드를 포함하는 클래스가 그 예다.
* 초기화 작업은 다른 작업과 관련이 없지만, 다른 작업보다 먼저 실행되어야 한다.



다양한 응집도 수준과 장단점

| 응집도 수준               | 장점                        | 단점                               |
| ------------------------- | --------------------------- | ---------------------------------- |
| **기능(높은 응집도)**     | 이해하기 쉬움               | 너무 단순한 클래스 생성            |
| 정보(중간 응집도)         | 유지보수하기 쉬움           | 불필요한 디펜던시                  |
| 순차(중간 응집도)         | 관련 동작을 찾기 쉬움       | SRP를 위배할 수 있음               |
| 논리(중간 응집도)         | 높은 수준의 카테고리화 제공 | SRP를 위배할 수 있음               |
| **유틸리티(낮은 응집도)** | 간단히 추가 가능            | 클래스의 책임을 파악하기 어려움    |
| **시간(낮은 응집도)**     | 판단 불가                   | 각 동작을 이해하고 사용하기 어려움 |



### 2.7.2 메서드 수준 응집도

* 메서드가 다양한 기능을 수행할수록 메서드가 어떤 동작을 하는지 이해하기가 점점 어려워진다. 
* 즉 메서드가 연관이 없는 여러 일을 처리한다면 응집도가 낮아진다. 
* 응집도가 낮은 메서드는 여러 책임을 포함하기 때문에 각 책임을 테스트하기가 어렵고, 메서드의 책임도 테스트하기가 어렵다. 
* 일반적으로 클래스나 메서드 파라미터의 여러 필드를 바꾸는 if/else 블록이 여러 개 포함되어 있다면 이는 응집도에 문제가 있음을 의미하므로 응집도가 높은 더 작은 조각으로 메서드를 분리해야 한다.



## 2.8 결합도

* 결합도는 한 기능이 다른 클래스에 얼마나 의존하고 있는지를 가늠한다. 
* 결합도는 어떤 클래스를 구현하는 데 얼마나 많은 지식(다른 클래스)을 참조했는가로 설명할 수 있다. 
* 더 많은 클래스를 참조했다면 기능을 변경할 때 그만큼 유연성이 떨어진다. 
* 어떤 클래스의 코드를 바꾸면 이 클래스에 의존하는 모든 클래스가 영향을 받는다.

* 시계를 생각하면 결합도를 쉽게 이해할 수 있다.
    * 시계가 어떻게 동작하는지 몰라도 시간을 알아내는 데 문제가 없다.
    * 즉 사람은 시계 내부 구조에 의존하지 않기 때문이다.
    * 따라서 시계 내부 구조를 바꾸더라도 사람은 시계를 읽는 데 영향을 받지 않는다.
    * 이는 두 가지 임무인 인터페이스와 구현이 서로 결합되지 않았기 때문이다.
* 결합도는 코드가 서로 어떻게 의존하는지와 관련된 척도다.
    * BankStatementAnalyzer는 BankStatementCSVParser 클래스에 의존한다.
    * 만약 JSON 항목으로 거래 내역을 파싱할 수 있도록 구현을 바꾸려면 어떻게 해야 할까?
    * XML 항목을 지원하려면 어떻게 해야 할까?
    * 인터페이스를 이용해 여러 컴포넌트의 결합도를 제거할 수 있으니 걱정하지 말자.
    * 인터페이스를 이용하면 요구사항이 바뀌더라도 유연성을 유지할 수 있다.

입출금 내역을 파싱하는 인터페이스 정의

```java
public interface BankStatementParser {
  BankTransaction parseFrom(String line);
  List<BankTransaction> parseLinesFrom(List<String> lines);
}
```

위에서 정의한 인터페이스를 구현한다.

```java
public class BankStatementCSVParser implements BankStatementParser {
  // ...
}
```



![image-20210624210445970](images/image-20210624210445970.png)



* 보통 코드를 구현할 때는 **결합도를 낮춰야** 한다. 
* 이는 코드의 다양한 컴포넌트가 내부와 세부 구현에 의존하지 않아야 함을 의미한다. 반대로 **높은 결합도**는 무조건 피해야 한다.



## 2.9 테스트

### 2.9.1 테스트 자동화

* 왜 테스트 자동화를 신경 써야 할까?

* 안타깝게도 소프트웨어를 개발할 때, 첫 시도에 소프트웨어가 제대로 동작하는 일은 거의 없다.
* 따라서 시간이 흐르면서 테스트의 필요성은 명확해진다. 
* 만약 새로운 자동 비행 소프트웨어가 실제로 동작하는지를 테스트하지도 않고 출시된다면 상상할 수 있겠는가?
* 수동 테스트에만 의존하면 안 된다.
* 자동화된 테스트에서는 사람의 조작 없이 여러 테스트가 포함된 스위트가 자동으로 실행된다.
* 즉 여러분의 코드가 바뀌었을 때, 지정된 테스트가 빠르게 실행되므로 소프트웨어가 예상하지 못한 문제를 일으키지 않고 제대로 동작할 거라는 확신을 조금 더 가질 수 있다.



### 테스트 자동화의 장점

#### 확신

* 소프트웨어가 규격 사양과 일치하며 동작하는지를 테스트해 고객의 요구 사항을 충족하고 있다는 사실을 더욱 확신할 수 있다.



#### 변화에도 튼튼함 유지

* 코드를 바꿨을 때 소프트웨어의 다른 부분을 실수로 망가뜨리지 않았는지 어떻게 알 수 있을까?
* 코드가 많지 않다면 문제를 쉽게 확인할 수 있다.
* 하지만 코드베이스에 수백만 행의 코드가 있다면 얘기가 달라진다.
* 자동화된 테스트 스위트가 있다면 바꾼 코드로 인해 새로운 버그가 발생하지 않았음을 확인하는 데 큰 도움이 된다.



#### 프로그램 이해도

* 테스트 자동화는 소스코드의 프로젝트에서 다양한 컴포넌트가 어떻게 동작하는지 이해하는 데 도움을 준다.
* 테스트는 다양한 컴포넌트의 디펜던시와 이들이 어떻게 상호작용하는지를 명확하게 드러낸다.
* 따라서 소프트웨어의 전체 개요를 빨리 파악할 수 있다.



### 2.9.2 제이유닛 사용하기

#### 테스트 메서드 정의하기

```java
public class BankStatementCSVParserTest {
  private final BankStatementParser statementParser = new BankStatementParser();
  
  @Test
  public void shouldParseOneCorrectLine() throws Exception {
    Assert.fail("Not yet implemented");
  }
}
```

* 유닛 테스트 클래스는 BankStatementCSVParserTest라는 보통 클래스다. 보통 테스트 클래스명에는 Test라는 접미어를 붙이는 것이 관습이다.
* 클래스는 shouldParseOneCorrectLine()이라는 하나의 메서드를 선언한다. 테스트 메서드의 구현코드를 보지 않고도 무엇을 테스트하는지 쉽게 알 수 있도록 서술적인 이름을 붙이는 것이 좋다.
* @Test를 테스트 메서드에 추가한다.



#### Assert 구문

* 유닛 테스트 설정의 세 단계 패턴을 Given-When-Then 공식이라 부른다.
* 테스트가 무엇을 수행하는지 쉽게 이해할 수 있기 때문이다.



### 2.9.3 코드 커버리지

* 한 개의 테스트 코드로 충분한지 어떻게 알 수 있을까?
* 코드 커버리지는 테스트 집합이 소프트웨어 소스코드를 얼마나 테스트했는가를 가리키는 척도다.
* 커버리지가 높을수록 예상하지 못한 버그가 발생할 확률이 낮아지므로 되도록 커버리지를 높이는 것을 목표로 삼아야 한다.
* 보통 70에서 90퍼센터를 목표로 정할 것을 권한다.
* 코드 커버리지가 높다고 해서 여러분이 소프트웨어를 잘 테스트하고 있음을 의미하는 것은 아니다.
* 코드 커버리지는 여러분이 테스트하지 않은 부분이 남아 있음을 알려주는 역할에 지나지 않기 때문에 테스트의 품질과는 아무 관련이 없다.
* 자바에서는 자코코(JaCoCo), 에마(Emma), 코베르투라(Cobertura) 같은 코드 커버리지 도구를 많이 사용한다.
* 구분 커버리지(line coverage)보다는 각 분기문을 확인하는 분기 커버리지(branch coverage)를 사용하는 것이 좋다.



## 2.10 총정리

* 갓 클래스와 코드 중복은 코드를 추론하고 유지보수하기 어렵게 만드는 요인이다.
* 단일 책임 원칙은 관리하고 유지보수하기 쉬운 코드를 구현하는 데 도움을 준다.
* 응집도는 클래스나 메서드의 책임이 얼마나 강하게 연관되어 있는지를 가리킨다.
* 결합도는 클래스나 다른 코드 부분에 얼마나 의존하고 있는지를 가리킨다.
* 높은 응집도와 낲은 결합도는 유지보수가 가능한 코드가 가져야 할 특징이다.
* 자동화된 테스트 스위트는 소프트웨어가 올바로 동작하며, 코드를 수정해도 잘 동작할 것임을 확실할 수 있고, 프로그램을 쉽게 이해할 수 있도록 도움을 준다.



