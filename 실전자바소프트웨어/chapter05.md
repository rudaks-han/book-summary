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



























