# 14장 일관성 있는 협력

* 객체는 협력을 위해 존재한다.
    * 협력은 객체가 존재하는 이유와 문맥을 제공한다.
    * 잘 설계된 애플리케이션은 이해하기 쉽고, 수정이 용이하며, 재사용 가능한 협력의 모임이다.
    * 객체지향 설계의 목표는 적절한 책임을 수행하는 객체들의 협력을 기반으로 결합도가 낮고 재사용 가능한 코드 구조를 창조하는 것이다.
* 애플리케이션을 개발하다 보면 유사한 요구사항을 반복적으로 추가하거나 수정하게 되는 경우가 있다.
    * 이때 객체들의 협력 구조가 서로 다른 경우에는 코드를 이해하기도 어렵고 코드 수정으로 인해 버그가 발생할 위헙성도 높아진다.
    * 유사한 요구사항을 계속 추가해야 하는 상황에서 각 협력이 서로 다른 패턴을 따를 경우에는 전체적인 설계의 일관성이 서서히 무너지게 된다.
* 객체지향 패러다임의 장점은 설계를 재사용할 수 있다는 것이다.
    * 하지만 재사용은 공짜로 얻어지지 않는다.
    * 재사용을 위해서는 객체들의 협력 방식을 일관성 있게 만들어야 한다.
    * 일관성은 설계에 드는 비용을 감소시킨다.
    * 과거의 해결 방법을 반복적으로 사용해서 유사한 기능을 구현하는 데 드는 시간과 노력을 대폭 줄일 수 있기 때문이다.
    * 일관성 있는 설계가 가져다 주는 더 큰 이익은 코드가 이해하기 쉬워진다는 것이다.
    * 특정한 문제를 유사한 방법으로 해결하고 있다는 사실을 알면 문제를 이해하는 것만으로도 코드의 구조를 예상할 수 있게 된다.
* 가능한 유사한 기능을 구현하기 위해 유사한 협력 패턴을 사용하라.
    * 객체들의 협력이 전체적으로 일관성 있는 유사한 패턴을 따른다면 시스템을 이해하고 확장하기 위해 요구되는 정신적인 부담을 크게 줄일 수 있다.
    * 지금 보고 있는 코드가 얼마 전에 봤던 코드가 유사하다는 사실을 아는 순간 새로운 코드가 직관적인 모습으로 다가오는 것을 느끼게 될 것이다.
    * 유사한 기능을 구현하기 위해 유사한 협력 방식을 따를 경우 코드를 이해하기 위해 필요한 것은 약간의 기억력과 적응력뿐이다.
* 일관성 있는 협력 패턴을 적용하면 여러분의 코드가 이해하기 쉽고 직관적이며 유연해진다는 것이 이번 장의 주제다.



## 01 핸드폰 과금 시스템 변경하기

### 기본 정책 확장

* 11장에서 구현한 핸드폰 과금 시스템의 요금 정책을 수정해야 한다고 가정하자.
    * 지금까지 기본 정책에는 일반 요금제와 심야 할인 요금제의 두 가지 종류가 있었다.
    * 이번 장에서는 기본 정책을 4가지 방식으로 확장할 것이다.
* 기본 정책을 구성하는 4가지 방식에 관해 간단히 살펴보자.
    * 고정요금 방식: 일정 시간 단위로 동일한 요금을 부가하는 방식이다. 모든 통화에 대해 동일하게 10초당 9원을 부과하는 방식이 고정요금 방식의 예에 해당된다. 기존의 '일반 요금제'와 동일하다.
    * 시간대별 방식: 하루 24시간을 특정한 시간 구간으로 나눈 후 각 구간별로 서로 다른 요금을 부과하는 방식이다. 예를 들어, 0시 ~ 19시까지는 10초당 19원을, 19시부터 24시까지는 10초당 15원의 요금을 부과하는 방식이다. 기존의 '심야 할인 요금제'는 밤 10시를 기준으로 요금을 부과한 시간대별 방식이다.
    * 요일별 방식: 요일별로 요금을 차등 부과하는 방식이다. 이 방식을 사용하면 월요일부터 금요일까지는 10초당 38원을, 토요일과 일요일에는 10초당 19원을 부과하는 요금제를 만들 수 있다.
    * 구간별 방식: 전체 통화 시간을 일정한 통화 시간에 따라 나누고 각 구간별로 요금을 차등 부과하는 방식이다. 예를 들어, 통화 구간을 초기 1분과 1분 이후로 나눈 후 초기 1분 동안은 10초당 50원을, 그 이후에는 10초당 20원을 부과하는 방식이 구간별 방식에 해당한다. 만약 어떤 사용자의 전체 통화 시간이 60분이라면 처음 1분에 대해서는 10초당 50원이 부과되고 나머지 59분에 대해서는 10초당 20원의 요금을 부과될 것이다.



### 고정 요금 방식 구현하기

```java
public class FixedFeePolicy extends BasicRatePolicy {
    private Money amount;
    private Duration seconds;

    public FixedFeePolicy(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
```



### 시간대별 방식 구현하기

* 시간대별 방식에 따라 요금을 계산하기 위해서는 통화 기간을 정해진 시간대별로 나눈 후 각 시간대별로 서로 다른 계산 규칙을 적용해야 한다.
* 여기서 한 가지 고려해야 할 조건이 있다.
    * 만약 통화가 여러 날에 걸쳐서 이뤄진다면 어떻게 될까?
* 이 경우 시간대별 방식에 따라 요금을 구현하려면 규칙에 정의된 구간별로 통화를 구분해야 한다.
* 여기서 이야기하고 싶은 것은 시간대별 방식의 통화 요금을 계산하기 위해서는 통화의 시작 시간과 종료 시간뿐만 아니라 시작 일자와 종료 일자도 함께 고려해야 한다는 것이다.
* 시간대별 방식을 구현하는 데 있어 핵심은 규칙에 따라 통화 시간을 분할하는 방법을 결정하는 것이다.
    * 이를 위해 기간을 편하게 관리할 수 있는 DateTimeInterval 클래스를 추가하자.
    * DateTimeInterval은 시작 시간(from)과 종료 시간(to)을 인스턴스 변수로 포함하며, 객체 생성을 위한 정적 메서드인 of, toMidnight, fromMidnight, during을 제공한다.

```java
public class DateTimeInterval {
    private LocalDateTime from;
    private LocalDateTime to;

    public static DateTimeInterval of(LocalDateTime from, LocalDateTime to) {
        return new DateTimeInterval(from, to);
    }

    public static DateTimeInterval toMidnight(LocalDateTime from) {
        return new DateTimeInterval(from, LocalDateTime.of(from.toLocalDate(), LocalTime.of(23, 59, 59, 999_999_999)));
    }

    public static DateTimeInterval fromMidnight(LocalDateTime to) {
        return new DateTimeInterval(LocalDateTime.of(to.toLocalDate(), LocalTime.of(0, 0)), to);
    }

    public static DateTimeInterval during(LocalDate date) {
        return new DateTimeInterval(
            LocalDateTime.of(date, LocalTime.of(0, 0)),
            LocalDateTime.of(date, LocalTime.of(23, 59, 59, 999_999_999)));
    }

    private DateTimeInterval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public Duration duration() {
        return Duration.between(from, to);
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }
}
```

* 기존의 Call 클래스는 통화 기간을 저장하기 위해 from, to라는 두 개의 LocalDateTime 타입의 인스턴스 변수를 포함하고 있었다.

```java
public class Call {
  private LocalDateTime from;
  private LocalDateTime to;
}
```

* 이제 기간을 하나의 단위로 표현할 수 있는 DateTimeInterval 타입을 사용할 수 있으므로 from과 to를 interval이라는 하나의 인스턴스 변수로 묶을 수 있다.

```java
public class Call {
    private DateTimeInterval interval;

    public Call(LocalDateTime from, LocalDateTime to) {
        this.interval = DateTimeInterval.of(from, to);
    }

    public Duration getDuration() {
        return interval.duration();
    }

    public LocalDateTime getFrom() {
        return interval.getFrom();
    }

    public LocalDateTime getTo() {
        return interval.getTo();
    }

    public DateTimeInterval getInterval() {
        return interval;
    }
}
```





### 요일별 방식 구현하기





### 구간별 방식 구현하기





## 02 설계에 일관성 부여하기





### 조건 로직 대 객체 탐색





### 캡슐화 다시 살펴보기





## 03 일관성 있는 기본 정책 구현하기

### 변경 분리하기





### 변경 캡슐화하기





### 협력 패턴 설계하기





### 추상화 수준에서 협력 패턴 구현하기





### 구체적인 협력 구현하기





### 협력 패턴에 맞추기





### 패턴을 찾아라





