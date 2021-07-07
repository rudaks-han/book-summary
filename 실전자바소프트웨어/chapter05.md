# 5장 비즈니스 규칙 엔진

## 5.2 목표

* 유닛 테스트를 구현하는 데 유용한 모킹 기법도 전반적으로 배운다.
* 지역 변수 형식 추론, switch 문 등 몇 가지 최신 자바 기능도 살펴본다.
* 마지막으로 빌더 패턴과 인터페이스 분리 원칙으로 사용자 친화적인 API 개발 장법도 배운다.



## 5.3 비즈니스 규칙 엔진 요구사항

* 프로그래머가 아닌 사람도 자신의 워크플로에 비즈니스 로직을 추가하거나 바꿀 수 있는 기능을 만들려 한다.
* 예를 들어 마케팅 부서장은 한 제품에 관심이 쏠리면서 어떤 조건을 만족할 때 특별 할인을 제공하고 싶어하고 회계 부서장은 지출이 평소보다 높을 때 알람을 만들고 싶어 한다.
* 비즈니스 규칙 엔진으로 이런 기능을 제공하려 한다.
    * **팩트**: 규칙이 **액션**: 수행하려는 동작
    * **조건**: 액션을 언제 발생시킬지 결정
    * **규칙**: 실행하려는 비즈니스 규칙을 지정, 보통 팩트, 액션, 조건을 한 그룹으로 묶어 규칙으로 만듦



## 5.4 테스트 주도 개발

* 아직 요구사항이 확정되지 않은 유동적인 상태이므로 사용자가 수행할 기본 기능부터 나열해보자.
    * 액션 추가
    * 액션 실행
    * 기본 보고

```java
public class BusinessRuleEngine {
    public void addAction(final Action action) {
        throw new UnsupportedOperationException();
    }
    
    public int count() {
        throw new UnsupportedOperationException();
    }
    
    public void run() {
        throw new UnsupportedOperationException();
    }
}
```

Action 인터페이스는 한 개의 추상 메서드만 선언하므로 함수형 인터페이스 애너테이션을 추가할 수 있다.

```java
@FunctionalInterface
public Interface Action {
    void execute();
}
```



### 5.4.1 TDD를 사용하는 이유

* 테스트를 따로 구현하므로 테스트에 대응하는 요구 사항을 한 개씩 구현할 때마다 필요한 요구사항에 집중하고 개선할 수 있다.
* 코드를 올바르게 조직할 수 있다. 예를 들어 먼저 테스트를 구현하면서 코드에 어떤 공개 인터페이스를 만들어야 하는지 신중히 검토하게 된다.
* TDD 주기에 따라 요구 사항 구현을 반복하면서 종합적인 테스트 스위트를 완성할 수 있으므로 요구 사항을 만족시켰다는 사실을 조금 더 확신할 수 있으며 버그 발생 범위도 줄일 수 있다.
* 테스트를 통과하기 위한 코드를 구현하기 때문에 필요하지 않은 테스트를 구현하는 일을 줄일 수 있다.



### 5.4.2 TDD 주기

1. 실패하는 테스트 구현
2. 모든 테스트 실행
3. 기능이 동작하도록 코드 구현
4. 모든 테스트 실행



```java
@Test
void shouldHaveNoRulesInitially() {
    final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
    
    assertEquals(0, businessRuleEngine.count());
}

@Test
void shouldAddTwoActions() {
    final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
    
    businessRuleEngine.addAction(() -> {});
    businessRuleEngine.addAction(() -> {});
    
    assertEquals(2, businessRuleEngine.count());
}
```

구현 코드를 추가한다.

```java
public class BusinessRuleEngine {
    private final List<Action> actions;
    
    public BusinessRuleEngine() {
        this.actions = new ArrayList<>();
    }
    
    public void addAction(final Action action) {
        this.actions.add(action);
    }
    
    public int count() {
        return this.actions.size();
    }
    
    public void run() {
        throw new UnsupportedOperationException();
    }
}
```



## 5.5 모킹

* 다음과 같은 방법으로 모킹을 사용한다.
    1. 목 생성
    2. 메서드가 호출되었는지 확인

```java
@Test
void shouldExecuteOneAction() {
    final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
    final Action mockAction = mock(Action.class);
    
    businessRuleEngine.addAction(mockAction);
    businessRuleEngine.run();
    
    verify(mockAction).perform();
}
```



## 5.6 조건 추가하기

* 실제 업무에서는 비즈니스 규칙 엔진으로 특정 조건을 만족하면 액션을 수행하도록 설정할 수 있어야 한다.
* 이 조건은 어떤 팩트에 의존한다.
* 예를 들어 '잠재 고개그이 직함이 CEO'면 알림 같은 상황이다.



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

이 방식에는 다음과 같은 문제가 있다.

1. 액션을 어떻게 테스트할까? customer 객체가 하드코딩된 디펜던시를 가지기 때문에 기능 코드가 독립적이지 않다.
2. customer 객체는 액션과 그룹화되어 있지 않다. customer 객체는 여러 곳에 공유된 외부 상태이므로 의무가 혼란스럽게 엉킨다.

* 어떻게 이 문제를 해결할 수 있을까?
* 비즈니스 규칙 엔진 내의 액션에서 사용할 수 있는 상태로 캡슐화해야 한다.
* 이 요구 사항을 Facts라는 새 클래스로 만들자.
* Facts는 비즈니스 규칙 엔진 일부의 필요한 상태를 가리키며 Action 인터페이스도 Facts에 근거해 동작하도록 수정한다.

Facts로 액션 테스트

```java
@Test
public void shouldPerformAnActionWithFacts() {
    final Action mockAction = mock(Action.class);
    final Facts mockFacts = mock(Facts.class);
    final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(mockedFacts);
    
    businessRuleEngine.addAction(mockAction);
    businessRuleEngine.run();
    
    verify(mockAction).perform(mockFacts);
}
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



### 5.6.2 지역 변수 형식 추론

* 형식 추론(type inference)이란 컴파일러가 정적 형식을 자동으로 추론해 결정하는 기능으로 사용자는 더 이상 명시적으로 명식적으로 형식을 지정할 필요가 없다.

```java
Map<String, String> facts = new HashMap<>();
```

아래 코드 대신 위 코드면 충분하다.

```java
Map<String, String> facts = new HashMap<String, String>();
```

* 이 기능은 자바 7에서 추가된 다이아몬드 연산자라는 기능이다.



명시적 형식으로 지역 변수 선언

```java
Facts env = new Facts();
BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(env);
```

지역 변수 형식 추론

```java
var env = new Facts();
var businessRuleEngine = new BusinessRuleEngine(env);
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

* 플루언트 API란 특정 문제를 더 직관적으로 해결할 수 있도록 특정 도메인에 맞춰진 API를 가리킨다. 
* 플루언트 API의 메서드 체이닝을 이용하면 더 복잡한 연산도 지정할 수 있다.



> 빌더 패턴 vs 플루언트 API
>
> http://www.javabyexamples.com/builder-vs-fluent-interface



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

* 규칙의 개념을 어떻게 모델링할 수 있을까?
* perform()이라는 연산을 수행하는 Rule 인터페이스를 정의한다.



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

* 빌더 패턴으로 Rule 객체와 필요한 조건, 액션을 만드는 과정을 개선해보자.
* 빌더 패턴은 단순하게 객체를 만드는 방법을 제공한다.
* 빌더 패턴은 생성자의 파라미터를 분해해서 각각의 파라미터를 받는 여러 메서드로 분리한다.



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

* 클래스를 이용해 RuleBuilder를 만들고 when(), then(), createRule()로 rule을 설정한다.
* 플루언트 API 설계의 핵심이 메서드 체이닝이다.



RuleBuilder 사용

```java
Rule rule = new RuleBuilder()
  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
  .then(facts -> {
    var name = facts.getFact("name");
    Mailer.sendEmail("sales@company.com", "Relavant customer: " + name);
  })
    .createRule();
```

* 하지만 여전히 API 사용자를 당황하게 만드는 두 가지 문제가 남아 있다.
    * 빈 RuleBuinder 인스턴스화
    * createRule() 메서드 호출
* API를 조금 개선해 이 문제를 해결해보자.
    * 사용자가 명시적으로 생성자를 호출하지 못하도록 생성자를 비공개로 설정한다. 그러려면 API에 다른 진입점을 만들어야 한다.
    * when() 메서드를 정적 메서드로 만들어 이 메서드를 사용자가 직접 호출하면 예전 생성자를 호출하도록 한다. 게다가 정적 메서드를 제공하므로 Rule 객체를 설정하려면 어떤 메서드를 이용해야 하는지 쉽게 알 수 있으므로 발견성도 개선된다.
    * then() 메서드가 DefaultRule 객체의 최종 생성을 책임진다.

RuleBuilder를 개선한 코드다.

```java
public class RuleBuilder {
  private Condition condition;
  
  private RuleBuilder(final Condition condition) {
      this.condition = condition;
  }
    
  private RuleBuilder when(final Condition condition) {
      return new RuleBuilder(condition);
  }
  
  public Rule then(final Action action) {
      return new DefaultRule(condition, action);
  }
}
```

개선된 RuleBuilder 사용

```java
final Rule ruleSendEmailToSalesWhenCEO = RuleBuilder
    .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
    .then(facts -> {
        var name = facts.getFact("name");
        Mailer.sendEmail("sales@company.com", "Relevant customer!!!: " + name);
    });
```

리팩터링된 비즈니스 규칙 엔진

```java
public class BusinessRuleEngine {
  private final List<Rule> rules;
  private final Facts facts;
  
  public BusinessRuleEngine(final Facts facts) {
    this.facts = facts;
    this.rules = new ArrayList();
  }
  
  public void addRule(final Rule rule) {
    this.rules.add(rule);
  }
  
  public void run() {
    this.rules.forEach(rule -> rule.perform(facts));
  }
}
```



## 5.8 총정리

* 테스트 주도 개발 철학에 따르면 먼저 테스트를 구현하고 이를 가이드 삼아 코드를 구현한다.
* 모킹으로 유닛테스트에서 어떤 동작이 실행되었는지 확인한다.
* 자바는 지역 변수 형식 추론과 switch문을 지원한다.
* 빌더 패턴은 복잡한 객체를 사용자 친화적인 API로 인스턴스화할 수 있도록 돕는다.
* 인터페이스 분리 원칙은 불필요한 메서드의 디펜던시를 감소시켜 높은 응집도를 촉진한다. 큰 인터페이스를 응집력 있는 작은 인터페이스로 분리해 사용자는 필요한 기능만 사용할 수 있다.



