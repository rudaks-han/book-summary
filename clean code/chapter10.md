# 10장 클래스

##### 클래스 체계

* 정적 공개(static public) 상수가 있다면 맨 처음에 나온다.
* 다음으로 정적 비공개(private) 변수가 나오며
* 이어서 비공개 인스턴스 변수가 나온다.
* 변수 목록 다음에 공개 함수가 나온다.
* 비공개 함수는 자신을 호출하는 공개 함수 직후에 넣는다.

추상화 단계가 순차적으로 내려간다. 그래서 프로그램은 신문 기사처럼 읽힌다.



###### 캡슐화

변수와 유틸리티 함수는 가능한 공개하지 않는 편이 낫지만 반드시 숨겨야 한다는 법칙도 없다. 때로는 변수나 유틸리티 함수를 protected로 선언해 테스트 코드에 접근을 허용하기도 한다.

캡슐화를 풀어주는 결정은 언제나 최후의 수단이다.



##### 클래스는 작아야 한다

클래스를 만들 때 첫 번째 규칙은 크기다. 클래스는 작아야 한다.
두 번째 규칙도 크기다. 더 작아야 한다. 

얼마나 작아야 하나?

목록 10-1은 SuperDashboard라는 클래스로, 공개 메서드 수가 대략 70개 정도다. 대다수 개발자는 클래스가 엄청나게 크다는 사실에 동의하리라. SuperDashboard 클래스를 '만능 클래스'라 부르는 개발자가 있을지도 모르겠다.

클래스 이름은 해당 클래스 책임을 기술해야 한다.
실제로 작명은 클래스 크기를 줄이는 첫 번째 관문이다. 간결한 이름이 떠오르지 않는다면 필경 클래스 크기가 너무 커서 그렇다.
예를 들어, 클래스 이름에 Processor, Manager, Super 등와 같이 모호한 단어가 있다면 클래스에다 여러 책임을 떠안겼다는 증거다.

또한 클래스 설명은 if, and, or, but을 사용하지 않고 25단어 내외로 가능해야 한다.



###### 단일 책임 원칙

단일 책임 원칙은 클래스나 모듈을 변경할 이유가 하나, 단 하나뿐이어야 한다는 원칙이다. SRP는 '책임'이라는 개념을 정의하면 적절한 클래스 크기를 제시한다. 클래스는 책임, 즉 변경할 이유가 하나여야 한다는 의미다.

책임, 즉 변경할 이유를 파악하려 애쓰다 보면 코드를 추상화하기도 쉬워진다. 더 좋은 추상화가 더 쉽게 떠오른다.

SRP는 객체 지향 설계에서 더욱 중요한 개념이다. 또한 이해하고 지키기 수월한 개념이기도 하다. 하지만 이상하게도 SRP는 클래스 설계자가 가장 무시하는 규칙 중 하나다. 우리는 수많은 책임을 떠안은 클래스를 꾸준하게 접한다. 왜일까?

소프트웨어를 돌아가게 만드는 활동과 소프트웨어를 깨끗하게 만드는 활동은 완전히 별개다.
<u>우리들 대다수는 두뇌 용량에 한계가 있어 '깨끗하고 체계적인 소프트웨어'보다 '돌아가는 소프트웨어'에 초점을 맞춘다.</u> 전적으로 올바른 태도다.

문제는 우리들 대다수가 프로그램이 돌아가면 일이 끝났다고 여기는 데 있다. '깨끗하고 체계적인 소프트웨어'라는 다음 관심사로 전환하지 않는다. 프로그램으로 되돌아가 만능 클래스를 단일 책임 클래스 여럿으로 분리하는 대신 다음 문제로 넘어가버린다.

게다가 <u>많은 개발자는 자질한 단일 책임 클래스가 많아지면 큰 그림을 이해하기 어려워진다고 우려한다.</u> 큰 그림을 이해하려면 이 클래스 저 클래스를 수없이 넘나들어야 한다고 걱정한다.

작은 서랍은 많이 두고 기능과 이름이 명확한 컴포넌트를 나눠 넣고 싶은가? 아니면 큰 서랍 몇 개를 두고 모두를 던저 넣고 싶은가?

강조하는 차원에서 한 번 더 말하겠다. 큰 클래스 몇 개가 아니라 작은 클래스 여럿으로 이뤄진 시스템이 더 바람직하다. 작은 클래스는 각자 맡은 책임이 하나며, 변경할 이유가 하나며, 다른 작은 클래스와 협력해 시스템에 필요한 동작을 수행한다.



###### 응집도

* 클래스는 인스턴스 변수 수가 작아야 한다. 
* 각 클래스 메서드는 클래스 인스턴스 변수를 하나 이상 사용해야 한다. 
* 일반적으로 메서드가 변수를 더 많이 사용할수록 메서드와 클래스는 응집도가 더 높다. 
* 모든 인스턴스 변수를 메서드마다 사용하는 클래스는 응집도가 가장 높다.

응집도가 높다는 말은 클래스에 속한 메서드와 변수가 서로 의존하며 논리적인 단위로 묶인다는 의미기 때문이다.

'함수를 작게, 매개변수 목록을 짧게'라는 전략을 따르다 보면 때로는 몇몇 메서드만이 사용하는 인스턴스 변수가 아주 많아진다. 이는 십중팔구 새로운 클래스로 쪼개야 한다는 신호다. 응집도가 높아지도록 변수와 메서드를 적절히 분리해 새로운 클래스 두세 개로 쪼개준다.



###### 응집도를 유지하면 작은 클래스가 여럿이 나온다

큰 함수를 작은 함수 여럿으로 나누기만 해도 클래스 수가 많아진다. 예를 들어, 변수가 아주 많은 큰 함수가 있다. 큰 함수 일부를 작은 함수 하나로 빼내고 싶은데, 빼내려는 코드가 큰 함수에 정의된 변수 넷을 사용한다. 그렇다면 변수 네 개를 새 함수에 인수로  넘겨야 옳을까?

전혀 아니다. 만약 네 변수를 클래스 인스턴스 변수로 승격한다면 새 함수는 인수가 필요없다. 그 만큼 함수를 쪼기기 쉬워진다.

불행히도 이렇게 하면 클래스가 응집력을 잃는다. 몇몇 함수만 사용하는 인스턴스 변수가 점점 더 늘어나기 때문이다. 그런데 잠깐만! 몇몇 함수가 몇몇 변수만 사용한다면 독자적인 클래스로 분리해도 되지 않는가? 당연하다. 클래스가 응집력을 잃는다면 쪼개라!



##### 변경하기 쉬운 클래스

대다수 시스템은 지속적인 변경이 가해진다. 뭔가 변경할 때마다 시스템이 의도대로 동작하지 않을 위험이 따른다. 깨끗한 시스템은 클래스를 체계적으로 정리해 변경에 수반하는 위험을 낮춘다.

경험에 의하면 클래스 일부에서만 사용되는 비공개 메서드는 코드를 개선할 잠재적인 여지를 시사한다. 

[목록 10-9] 변경이 필요해 '손대야'하는 클래스

```java
public class Sql {
    public Sql(String table, Column[] columns)
    public String create();
    public String insert(Object[] fields)
    public String selectAll()
    public String findByKey(String keyColumn, String keyValue)
    public String select(Column column, STring pattern)
    public String select(Criteria criteria)
    public String preparedInsert()
    private String columnList(Column[] columns)
    private String valuesList(Object[] fields, final Column[] columns)
    private String selectWithCriteria(String criteria)
    private String placeholderList(Colum[] columns)
}
```

* 새로운 SQL문을 지원하려면 반드시 Sql 클래스에 손대야 한다.
* 기존 SQL문 하나를 수정할 때도 반드시 Sql 클래스에 손대야 한다. 예를 들어 select문에 내장된 select 문을 지원하려면 Sql 클래스를 고쳐야 한다.
* 이렇듯 변경할 이유가 두 가지이므로 Sql클래스는 SRP를 위반한다.



[목록 10-10] 닫힌 클래스 집합

```java
abstract public class Sql {
    public Sql(String table, Column[] columns)
    abstract public String generate();
}

public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class SelectSql extends Sql {
    public SelectSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class InsertSql extends Sql {
    public InsertSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class SelectWithCriteriaSql extends Sql {
    public SelectWithCriteriaSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class SelectWithMatchSql extends Sql {
    public SelectWithMatchSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class FindByKeySql extends Sql {
    public FindByKeySql(String table, Column[] columns)
    @Override
    public String generate()
}

public class PreparedInsertSql extends Sql {
    public PreparedInsertSql(String table, Column[] columns)
    @Override
    public String generate()
}

public class Where {
    public Where(String criteria)
    public String generate()
}

public class ColumnList {
    public ColumnList(Column[] columns)
    public String generate()
}
```

* 각 클래스는 극도로 단순하다.
* 함수 하나를 수정했다고 다른 함수가 망가질 위험도 사실상 사라졌다.
* update 문을 추가할 때 기존 클래스를 변경할 필요가 전혀 없다는 사실 역시 중요하다.
* SRP를 지원한다.
* OCP도 지원한다.
* 새 기능을 수정하거나 기존 기능을 변경할 때 건드릴 코드가 최소인 시스템 구조가 바람직하다.



###### 변경으로부터 격리

요구사항은 변하기 마련이다. 따라서 코드도 변하기 마련이다.

상세한 구현에 의존하는 코드는 테스트가 어렵다. 예를 들어, PortFolio 클래스를 만든다고 가정하자. 그런데 Portfolio 클래스는 외부 TokyoStockExchange API를 사용해 포트폴리오 값을 계산한다. 따라서 우리 테스트 코드는 시세 변화에 영향을 받는다.

Portfolio 클래스에서 TokyoStockExchange API를 직접 호출하는 대신 StockExchange라는 인터페이스를 생성한 후 메서드 하나를 선언한다.

```java
public interface StockExchange {
    Money currentPrice(String symbol);
}
```

다음으로 TokyoStockExchange 클래스를 구현한다.

```java
public Portfolio {
    private StockExchange exchage;
    public Portfolio(StockExchange exchange) {
        this.exchange = exchange;
    }
}
```

이제 TokyoStockExchange 클래스를 흉내내는 테스트용 클래스를 만들 수 있다.

```java
public class PortfolioTest {
    private FixedStockExchangeStub exchange;
    private Portfolio portfolio;
    
    @Before
    protected void setUp() throws Exception {
        exchange = new FixedStockExchangeStub();
        exchange.fix("MSFT", 100);
        portfolio = new Portfolio(exchange);
    }
    
    @Test
    public void GevenFiveMSFTTotalShouldBe500() throws Exception {
        portfolio.add(5, "MSFT");
        Assert.assertEquals(500, portfolio.value());
    }
}
```

이렇게 결합도를 최소로 줄이면 자연스럽게 또 다른 클래스 설계 원칙인 DIP를 따르는 클래스가 나온다. 본질적으로 DIP는 클래스가 상세한 구현이 아니라 추상화에 의존해야 한다는 원칙이다.