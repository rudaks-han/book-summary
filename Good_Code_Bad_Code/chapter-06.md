# chapter 6. 예측 가능한 코드를 작성하라

## 6.1 매직값을 반환하지 말아야 한다

매지값의 일반적인 예는 값이 없거나 오류가 발생했음을 나타내기 위해 -1을 반환하는 것이다.

매지값은 함수의 정상적인 반환 유형에 들어맞기 때문에 이 갑이 갖는 특별한 의미를 인지하지 못하고, 이에 대해 적극적으로 경계하지 않으면 정상적인 반환값으로 오인하기 쉽다.



### 6.1.1 매직값은 버그를 유발할 수 있다

값이 없음을 나타내기 위해 함수에서 -1을 반환하는 것을 접한 적이 있을 것이다. 일부 레거시 코드와 언어의 내장된 함수들(예: 자바스크립트에서 배열에 대해 indexOf()를 호출할 때) 중에 이런 경우가 있다.



User 클래스 및 getAge() 함수

```java
public class User {
  ...
    Integer getAge() { ...} // 사용자의 나이를 반환하는데 널값은 변환되지 않는다.
}
```



사용자 평균 연령 계산

```java
double getMeanAge(List<User> users) {
  if (users.isEmpty()) {
    return null;
  }
  double sumOfAges = 0.0;
  for (User user : users) {
    sumOfAges += user.getAge().toDouble();
  }
  return sumOfAges / users.size().toDouble();
}
```


User 클래스의 상세 내용

```java
public class User {
  private final Integer age;
  Integer getAge() {
    if (age == null) {
      return -1; // 나이가 주어지지 않으면 -1을 반환한다.
    }
    return age;
  }
}
```



### 6.1.2 해결책: 널, 옵셔널 또는 오류를 반환하라



## 6.4 입력 매개변수를 수정하는 것에 주의하라

### 6.4.1 입력 매개변수를 수정하면 버그를 초래할 수 있다

입력 매개변수 변경

```java
private List<Invoice> getBillableInvoices(Map<User, Invoice> userInvoices, Set<User> usersWithFreeTrial) {
  userInvoices.removeAll(usersWithFreeTrial);
  return userInvoices.values();
}

private void processOrders(OrderBatch orderBatch) {
  Map<User, Invoice> userInvoices = orderBatch.getUserInvoices();
  Set<User> usersWithFreeTrial = orderBatch.getFreeTrialUsers();
  
  sendInvoices(getBillableInvoices(userInvoices, usersWithFreeTrial)); // getBillableInvoices는 예상과 다르게 userInvocies를 변경한다.
  endableOrderedServices(userInvoices); // 무료 평가판을 사용할 수 있는 유저는 해당 서비스를 사용할 수 있게끔 설정되지 않는다.
}

private void endableOrderedServices(Map<User, Invoice> userInvoices) {
  ...
}
```



### 6.4.2 해결책: 변경하기 전에 복사하라

입력 매개변수를 변경하지 않음

```java
private List<Invoice> getBillableInvoices(Map<User, Invoice> userInvoices, Set<User> usersWithFreeTrial) {
  return userInvoices.
    .entries()
    .filter(entry -> !usersWithFreeTrial.contains(entry.getKey())) // filter()는 조건에 맞는 값을 새로운 리스트에 복사한다.
    .map(entry -> entry.getValue());
}
```

값을 복사하면 메모리나 CPU, 혹은 두 가지 모두와 관련해 성능에 영향을 미칠 수 있다. 하지만 입력 매개변수의 변경으로 인해 발생할 수 있는 예기치 못한 동작이나 버그와 비교하면 성능이 크게 문제되지 않는 경우가 많다.



## 6.6 미래를 대비한 열거형 처리

### 6.6.1 미래에 추가될 수 있는 열것값을 암묵적으로 처리하는 것은 문제가 될 수 있다

열것값의 암시적 처리

```java
public enum PredictedOutcome {
  COMPANY_WILL_GO_BUST,
  COMPANY_WILL_MAKE_A_PROFIT
}

private boolean isOutcomeSafe(PredictedOutcome prediction) {
  if (prediction == PredictedOutcome.COMPANY_WILL_GO_BUST) {
    return false;
  }
  
  return true;
}
```

열거형의 새로운 값

```java
public enum PredictedOutcome {
  COMPANY_WILL_GO_BUST,
  COMPANY_WILL_MAKE_A_PROFIT,
  WORLD_WILL_END
}
```



### 6.6.2 해결책: 모든 경우를 처리하는 스위치 문을 사용하라

```java
public enum PredictedOutcome {
  COMPANY_WILL_GO_BUST,
  COMPANY_WILL_MAKE_A_PROFIT
}

private boolean isOutcomeSafe(PredictedOutcome prediction) {
  switch (prediction) {
    case COMPANY_WILL_GO_BUST:
      return false;
    case COMPANY_WILL_MAKE_A_PROFIT:
      return true;
  }
  
  throw new UncheckedException("Unhandled prediction: " + prediction);
}
```















