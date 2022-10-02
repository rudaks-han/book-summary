# 2장 객체지향 프로그래밍

## 01 영화 예매 시스템

### 요구사항 살펴보기

* `'영화'`는 영화에 대한 기본 정보를 표현한다.
    * 제목, 상영시간, 가격 정보와 같이 영화가 가지고 있는 기본적인 정보를 가리킬 때는 '영화'라는 단어를 사용할 것이다.
* `'상영'`은 실제로 관객들이 영화를 관람하는 사건을 표현한다.
    * 상영 일자, 시간, 순번 등을 가리키기 위해 '상영'이라는 용어를 사용할 것이다.
* `'할인 조건은'`은 가격의 할인 여부를 결정하며 '순서 조건'과 '기간 조건'의 두 종류로 나눌 수 있다.
* `'순서 조건'`은 상영 순번을 이용해 할인 여부를 결정하는 규칙이다.
* `'기간 조건'`은 영화 상영 시간을 이용해 할인 여부를 결정한다.
* `'할인 정책'`은 할인 요금을 결정한다.
* 할인 정책에는 `'금액 할인 정책'`과 `'비율 할인 정책'`이 있다.
* '금액 할인 정책'은 예매 요금에서 일정 금액을 할인해주는 방식이며 '비율 할인 정책'은 정가에서 일정 비율의 요금을 할인해 주는 방식이다.
* 영화별로 하나의 할인 정책만 할당할 수 있다.



## 02 객체지향 프로그래밍을 향해

### 협력, 객체, 클래스

* 객체지향 프로그램을 작성할 때 가장 먼저 고려하는 것은 무엇인가?
    - 객체지향 언어에 익숙한 사람이라면 가장 먼저 어떤 **클래스**가 필요한지 고민할 것이다.

* 안타깝게도 이것은 객체지향의 본질과는 거리가 멀다. 객체지향은 말 그대도 객체를 지향하는 것이다.

* 진정한 객체지향 패러다임으로의 전환은 클래스가 아닌 객체에 초점을 맞출 때에만 얻을 수 있다. 이를 위해서는 다음의 두 가지에 집중해야 한다.
    * 첫째, 어떤 클래스가 필요한지 고민하기 전에 어떤 객체들이 필요한지 고민하라.
        * 클래스는 공통적인 상태와 행동을 공유하는 객체들을 추상화한 것이다.
        * 클래스의 윤곽을 잡기 위해서는 어떤 객체들이 어떤 상태와 행동을 가지는지를 먼저 결정해야 한다.

    * 둘째, 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 일원으로 봐야 한다.
        * 훌륭한 협력이 훌륭한 객체를 낳고 훌륭한 객체가 훌륭한 클래스를 낳는다.



### 도메인의 구조에 따르는 프로그램 구조

* 소프트웨어는 사용자가 원하는 어떤 문제를 해결하기 위해 만들어진다.

* 영화 예매 시스템의 목적은 영화를 좀 더 쉽고 빠르게 예매하려는 사용자의 문제를 해결하는 것이다.

* 이처럼 문제를 해결하기 위해 사용자가 프로그램을 사용하는 분야를 **도메인**이라고 부른다.

* 일반적으로 클래스의 이름은 대응되는 도메인 개념의 이름과 동일하거나 적어도 유사하게 지어야 한다.

* 이 원칙에 따라

    * 영화라는 개념은 Movie 클래스
    * 상영이라는 개념은 Screening 클래스
    * 할인 정책은 DiscountPolicy
    * 금액 할인 정책은 AmountDiscountPolicy
    * 비율 할인 정책은 PercentDiscountPolicy
    * 기간 조건은 PeriodCondition
    * 예매라는 개념은 Reservation

    

### 클래스 구현하기

* Screening 클래스는 사용자들이 예매하는 대상인 '상영'을 구현한다.
* Screening은 상영할 영화(movie), 순번(sequence), 상영 시작 시간(whenScreened)을 인스턴스 변수로 포함한다.

```java
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenScreened = whenScreened;
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getStartTime() {
        return whenScreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }
}
```



* 클래스를 사용할 때 가장 중요한 것은 클래스의 경계를 구분 짓는 것이다.
* 클래스는 내부와 외부로 구분되며 훌륭한 클래스를 설계하기 위한 핵심은 어떤 부분을 외부로 공개하고 어떤 부분을 감출지를 결정하는 것이다.
* 그렇다면 클래스의 내부와 외부를 구분해야 하는 이유는 무엇일까? 
* 그 이유는 경계의 명확성이 객체의 자율성을 보장하기 때문이다.
* 그리고 더 중요한 이유로 프로그래머에게 구현의 자유를 제공하기 때문이다.



#### 자율적인 객체

* 먼저 두 가지 중요한 사실을 알아야 한다.
    * 첫 번째 사실은 객체가 **상태(state)**와 **행동(behavior)**을 함께 가지는 복합적인 존재라는 것이다.
    * 두 번째 사실은 객체가 스스로 판단하고 행동하는 **자율적인 존재**라는 것이다.

* 데이터와 기능을 객체 내부로 함께 묶는 것을 **캡슐화**라고 부른다.

* 캡슐화와 접근 제어는 객체를 두 부분으로 나눈다.
    * 하나는 외부에서 접근이 가능한 부분으로 이를 퍼블릭 <u>인터페이스</u>라고 부른다.
    * 다른 하나는 외부에서 접근이 불가능하고 오직 내부에서만 접근 가능한 부분으로 이를 <u>구현</u>이라고 부른다.

* 일반적으로 객체의 상태는 숨기고 행동만 외부에 공개해야 한다.



#### 프로그래머의 자유

* 클라이언트 프로그래머의 목표는 필요한 클래스들을 엮어서 애플리케이션을 빠르고 안정적으로 구축하는 것이다.
* 클라이언트 프로그래머가 숨겨 놓은 부분에 마음대로 접근할 수 없도록 방지함으로써 클라이언트 프로그래머에 대한 영향을 걱정하지 않고도 내부 구현을 마음대로 변경할 수 있다. 이를 **구현 은닉(implementation hiding)**이라고 부른다.
* 구현 은닉은 클래스 작성자와 클라이언트 프로그래머 모두에게 유용한 개념이다.
* 클라이언트 프로그래머는 내부의 구현은 무시한 채 인터페이스만 알고 있어도 클래스를 사용할 수 있기 때문에 머릿속에 담아둬야 하는 지식의 양을 줄일 수 있다.
* 클래스 작성자는 인터페이스를 바꾸지 않는 한 외부에 미치는 영향을 걱정하지 않고도 내부 구현을 마음대로 변경할 수 있다.
* 설계가 필요한 이유는 변경을 관리하기 위해서라는 것을 기억하라.



### 협력하는 객체들의 공동체

* Screening의 reserve 메서드는 영화를 예매한 후 예매 정보를 담고 있는 Reservation 인스턴스를 생성해서 반환한다.

```java
public class Screening {
  public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }
}
```

* Screening의 reserve 메서드를 보면 calculateFee라는 private 메서드를 호출해서 요금을 계산한 후 그 결과를 Reservation의 생성자에 전달하는 것을 알 수 있다.
* Screening은 전체 예매 요금을 구하기 위해 calculateMovieFee 메서드의 반환 값에 인원 수인 audienceCount를 곱한다.

```java
public class Screening {
  private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
```

* Money는 금액과 관련된 다양한 계산을 구현하는 간단한 클래스다.

```java
public class Money {
    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }
}
```

* 1장에서는 금액을 구현하기 위해 Long 타입을 사용했던 것을 기억하라.
* Long 타입은 변수의 크기나 연산자의 종류와 관련된 구현 관점의 제약은 표현할 수 있지만 Money 타입처럼 저장하는 값이 금액과 관련돼 있다는 의미를 전달할 수는 없다.
* 개념이 비록 하나의 인스턴스 변수만 포함하더라도 개념을 명시적으로 표현하는 것은 전체적인 설계의 명확성과 유연성을 높이는 첫걸음이다.



Reservation 클래스는 고객(customer), 상영 정보(Screening), 예매 요금(fee), 인원 수(audienceCount)를 속성으로 포함한다.

```java
public class Reservation {
    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screening screening, Money fee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
```



### 협력에 관한 짧은 이야기

* 객체가 다른 객체와 상호작용할 수 있는 유일한 방법은 **메시지를 전송(send a message)**하는 것뿐이다. 
* 다른 객체에게 요청이 도착할 때 해당 객체가 **메시지를 수신(receive a message)**했다고 이야기한다.
* 이처럼 수신된 메시지를 처리하기 위한 자신만의 방법을 **메서드(method)**라고 부른다.



## 03 할인 요금 구하기

### 할인 요금 계산을 위한 협력 시작하기

Movie는 제목(title), 상영시간(runningTime), 기본 요금(fee), 할인 정책(discountPolicy)을 속성으로 가진다.

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
}
```

* 이 메서드 안에는 한 가지 이상한 점이 있다.
* 어떤 할인 정책을 사용할 것인지 결정하는 코드가 어디에도 존재하지 않는다는 것이다.
* 단지 discountPolicy에게 메시지를 전송할 뿐이다.
* 이 코드에는 객체지향에서 중요하다고 여겨지는 두 가지 개념이 숨겨져 있다.
* 하나는 상속(inheritance)이고 다른 하나는 다형성이다.



### 할인 정책과 할인 조건

* 할인 정책은 금액 할인 정책과 비율 할인 정책으로 구분된다.
* 여기서는 부모 클래스인 DiscountPolicy 안에 중복 코드를 두고 AmountdiscountPolicy와 PercentDiscountPolicy가 이 클래스를 상속받게 할 것이다.

```java
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for (DiscountCondition each: conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening screening);
}
```

* 할인 조건을 만족하는 DiscountCondition이 하나라도 존재하는 경우에는 추상 메서드인 getDiscountAmount 메서드를 호출해 할인 요금을 계산한다.

* DiscountPolicy는 할인 여부와 요금 계산에 필요한 전체적인 흐름은 정의하지만 실제로 요금을 계산하는 부분은 추상 메서드인 getDiscountAmount 메서드에게 위임한다.
* 실제로는 DiscountPolicy를 상속받은 자식 클래스에서 오버라이딩한 메서드가 실행될 것이다.
* 이처럼 부모 클래스에 기본적인 알고리즘의 흐름을 구현하고 중간에 필요한 처리를 자식 클래스에게 위임하는 디자인 패턴을 **TEMPLATE METHOD** 패턴이라고 부른다.

DiscountCondition은 자바의 인터페이스를 이용해 선언돼 있다.

```java
public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
```

SequenceCondition은 할인 여부를 판단하기 위해 사용한 순번(sequence)을 인스턴스 변수로 포함한다.

```java
public class SequenceCondition implements DiscountCondition {
    private int sequence;

    public SequenceCondition(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.isSequence(sequence);
    }
}
```

PeriodCondition은 상영 시작 시간이 특정한 기간 안에 포함되는지 여부를 판단해 할인 여부를 결정한다.

```java
public class PeriodCondition implements DiscountCondition {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) &&
                startTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getStartTime().toLocalTime()) >= 0;
    }
}
```

이제 할인 정책을 구현하자. AmountDiscountPolicy는 DiscountPolicy의 자식 클래스로서 할인 조건을 만족할 경우 일정한 금액을 할인해주는 금액 할인 정책을 구현한다.

```java
public class AmountDiscountPolicy extends DiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition ... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
```

PercentDiscountPolicy 역시 DiscountPolicy의 자식 클래스로서 getDiscountAmount 메서드를 오버라이딩한다.

```java
public class PercentDiscountPolicy extends DiscountPolicy {
    private double percent;

    public PercentDiscountPolicy(double percent, DiscountCondition ... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
```



### 할인 정책 구성하기

* 할인 정책은 하나만 설정할 수 있지만 할인 조건의 경우에는 여러 개를 적용할 수 있다.

```java
public class Movie {
    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
      ...
        this.discountPolicy = discountPolicy;
    }
}
```

반면 DiscountPolicy의 생성자는 여러 개의 DiscountCondition 인스턴스를 허용한다.

```java
public abstract class DiscountPolicy {
    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }
}
```

다음은 아바타에 대한 할인 정책과 할인 조건을 설정한 것이다.

```java
Movie avatar = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(
                        Money.wons(800),
                        new SequenceCondition(1),
                        new SequenceCondition(10),
                        new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))));
```

'타이타닉'에 대한 할인 정책은 다음과 같이 설정할 수 있다. 10%의 비율 할인 정책이 적용되고 두 개의 기간 조건과 한 개의 순서 조건을 이용해 할인 여부를 판단한다는 것을 알 수 있다.

```java
Movie titanic = new Movie("타이타닉",
                Duration.ofMinutes(180),
                Money.wons(11000),
                new PercentDiscountPolicy(
                        0.1,
                        new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14,0), LocalTime.of(16, 59)),
                        new SequenceCondition(2),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10,0), LocalTime.of(13, 59))
                )
        );
```



## 04 상속과 다형성

* Movie 클래스 어디에서도 할인 정책이 금액 할인 정책인지, 비율 할인 정책인지를 판단하지 않는다. 
* 이 질문에 답하기 위해서는 상속과 다형성에 대해 알아봐야 한다.



### 컴파일 시간 의존성과 실행 시간 의존성

* Movie 클래스는 DiscountPolicy 클래스와 연결돼 있다는 것이다.
* 문제는 영화 요금을 계산하기 위해서는 추상 클래스인 DiscountPolicy가 아니라 AmountDiscountPolicy와 PercentDiscountPolicy 인스턴스가 필요하다.
* 그렇다면 Movie의 인스턴스가 코드 작성 시점에는 그 존재조차 알지 못했던 AmountDiscountPolicy와 PercentDiscountPolicy의 인스턴스와 실행 시점에 협력 가능한 이유는 무엇일까?
* Movie의 인스턴스를 생성할 때 인자로 AmountDiscountPolicy의 인스턴스를 전달하면 된다.

```java
Movie avatar = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(
                        Money.wons(800), ...));
```

영화 요금을 계산하기 위해 비율 할인 정책을 적용하고 싶다면 AmountDiscountPolicy 대신 PercentDiscountPolicy의 인스턴스를 전달하면 된다.

```java
Movie avatar = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new PercentDiscountPolicy(0.1, ...));
```

여기서 이야기하고 싶은 것은 코드의 의존성과 실행 시점의 의존성이 서로 다를 수 있다는 것이다.

* 코드의 의존성과 실행 시점의 의존성이 다르면 다를수록 코드를 이해하기 어려워진다는 것이다.
* 반면 코드의 의존성과 실행 시점의 의존성이 다르면 다를수록 코드는 더 유연해지고 확장 가능해진다.

무조건 유연한 설계도, 무조건 읽기 쉬운 코드도 정답이 아니다.

* 설계가 유연해질수록 코드를 이해하고 디버깅하기는 점점 더 어려워진다.
* 반면 유연성을 억제하면 코드를 이해하고 디버깅하기는 쉬워지지만 재사용성과 확장 가능성은 낮아진다.



### 차이에 의한 프로그래밍

* 상속은 기존 클래스를 기반으로 새로운 클래스를 쉽고 빠르게 추가할 수 있는 간편한 방법을 제공한다.
* AmountDiscountPolicy와 PercentDiscountPolicy의 경우 DiscountPolicy에서 정의한 추상 메서드인 getDiscountAmount 메서드를 오버라이딩해서 DiscountPolicy의 행동을 수정한다는 것을 알 수 있다.

* 이처럼 부모 클래스와 다른 부분만을 추가해서 새로운 클래스를 쉽고 빠르게 만드는 방법을 **차이에 의한 프로그래밍(programming by difference)**이라고 부른다.



### 상속과 인터페이스

* 상속이 가치 있는 이유는 부모 클래스가 제공하는 모든 인터페이스를 자식 클래스가 물려받을 수 있기 때문이다.
* 이것은 상속을 바라보는 일반적인 인식과는 거리가 있는데 대부분의 사람들은 상속의 목적이 메서드나 인스턴스 변수를 재사용하는 것이라고 생각하기 때문이다.
* 인터페이스는 객체가 이해할 수 있는 메시지의 목록을 정의한다. 상속을 통해 자식 클래스는 자신의 인터페이스에 부모 클래스의 인터페이스를 포함하게 된다.
* Movie가 DiscountPolicy의 인터페이스에 정의된 calculateDiscountAmount 메시지를 전송하고 있다.
* DiscountPolicy를 상속받는 AmountDiscountPolicy와 PercentDiscountPolicy의 인터페이스에도 이 오퍼레이션이 포함돼 있다는 사실에 주목하라.

* 자식 클래스가 부모 클래스를 대신하는 것을 **업캐스팅(upcasting)**이라고 부른다.
* 업캐스팅이라고 부르는 이유는 클래스 다이어그램을 작성할 때 부모 클래스를 자식클래스의 위에 위치시키기 때문이다.
* 아래에 위치한 자식 클래스가 위에 위치한 부모 클래스로 자동적으로 타입 캐스팅되는 것처럼 보이기 때문에 업캐스팅이라는 용어를 사용한다.



### 다형성

* Movie는 동일한 메시지를 전송하지만 실제로 어떤 메서드가 실행될 것인지는 메시지를 수신하는 클래스의 클래스가 무엇이냐에 따라 달라진다. 이를 **다형성**이라고 부른다.

* 다형성은 객체지향 프로그램의 컴파일 시간 의존성과 실행 시간 의존성이 다를 수 있다는 사실을 기반으로 한다.
* 프로그램을 작성할 때 Movie 클래스는 추상 클래스인 DiscountPolicy에 의존한다.
* 반면 실행 시점에 Movie의 인스턴스와 실제로 상호작용하는 객체는 AmountDiscountPolicy나 PercentDiscountPolicy의 인스턴스다.
* 이처럼 다형성은 컴파일 시간 의존성과 실행 시간 의존성을 다르게 만들 수 있는 객체지향의 특성을 이용해 서로 다른 메서드를 실행할 수 있게 한다.

* 다형성을 구현하는 방법은 매우 다양하지만 메시지에 응답하기 위해 실행될 메서드를 <u>컴파일 시점이 아닌 실행 시점에 결정</u>한다는 공통점이 있다. 이를 **지연 바인딩(lazy binding)** 또는 **동적 바인딩(dynamic  binding)**이라고 부른다.

* 전통적인 함수 호출처럼 컴파일 시점에 실행될 함수나 프로시저를 결정하는 것을 **초기 바인딩(early binding)** 또는 **정적 바인딩(static binding)**이라고 부른다.



###  인터페이스와 다형성

* 구현은 공유할 필요가 없고 순수하게 인터페이스만 공유하고 싶을 때가 있다. 자바에서는 인터페이스라는 프로그래밍 요소를 제공한다.



## 05 추상화와 유연성

### 추상화의 힘

* 추상화를 사용하면 세부적인 내용을 무시한 채 상위 정책을 쉽고 간단하게 표현할 수 있다. 추상화의 이런 특징은 세부사항에 억눌리지 않고 상위 개념만으로도 <u>도메인의 중요한 개념을 설명</u>할 수 있게 한다.

* 두 번째 특징은 첫 번째 특징으로부터 유추할 수 있다. 추상화를 이용해 상위 정책을 표현하면 기존 구조를 수정하지 않고도 새로운 기능을 쉽게 추가하고 확장할 수 있다. 다시 말해 <u>유연하게 만들 수 있다</u>.



### 유연한 설계

'스타워즈'에는 할인 정책이 적용돼 있지 않다.

```java
public class Movie {
  public Money calculateMovieFee(Screening screening) {
        if (discountPolicy == null) {
          return fee;
        }
    	return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
}
```

* 이 방식의 문제점은 할인 정책이 없는 경우를 예외 케이스로 취급하기 때문에 지금까지 일관성 있던 협력이 무너지게 된다.
* 책임의 위치를 결정하기 위해 조건문을 사용하는 것은 협력의 설계 측면에서 대부분의 경우 좋지 않은 선택이다.

NoneDiscountPolicy를 추가하자.

```java
public class NoneDiscountPolicy extends DiscountPolicy{
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
```

이제 할인되지 않는 영화를 생성할 수 있다.

```java
Movie starwars = new Movie("스타워즈",
                Duration.ofMinutes(210),
                Money.wons(10000),
                new NoneDiscountPolicy());
```

중요한 것은 기존의 Movie와 DiscountPolicy는 수정하지 않고 NoneDiscountPolicy라는 새로운 클래스를 추가하는 것만으로 애플리케이션의 기능을 확장했다는 것이다.



### 추상 클래스와 인터페이스 트레이드 오프



### 코드 재사용

* 상속은 코드를 재사용하기 위해 널리 사용되는 방법이다.
* 객체지향 설계와 관련된 자료를 조금이라도 본 사람들은 코드 재사용을 위해서는 상속보다는 **합성(composition)**이 더 좋은 방법이라는 이야기를 많이 들었을 것이다. 합성은 다른 객체의 인스턴스를 자신의 인스턴스 변수로 포함해서 재사용하는 방법을 말한다.



### 상속

* 상속은 객체지향에서 코드를 재사용하기 위해 널리 사용되는 기법이다. 
* 하지만 두 가지 관점에서 설계에 안 좋은 영향을 미친다.
* 하나는 상속이 캡슐화를 위반하는 것이고, 다른 하나는 설계를 유연하지 못하게 만든다는 것이다.
* 상속의 가장 큰 문제는 캡슐화를 위반하는 것이다.
    * 상속을 이용하기 위해서는 부모 클래스의 내부 구조를 잘 알고 있어야 한다.
        * AmountDiscountMovie와 PercentDiscountMovie를 구현하는 개발자는 부모 클래스인 Movie의 calculateFee 메서드 안에서 추상 메서드인 getDiscountAmount 메서드를 호출한다는 사실을 알고 있어야 한다.
    * 결과적으로 부모 클래스의 구현이 자식 클래스에게 노출되기 때문에 캡슐화가 약화된다.
        * 캡슐화의 약화는 자식 클래스가 부모 클래스에 강하게 결합되도록 만들기 때문에 부모 클래스를 변경할 때 자식 클래스도 함께 변경될 확률을 높인다.
        * 결과적으로 상속을 과도하게 사용한 코드는 변경하기도 어려워진다.
* 상속의 두 번째 단점은 설계가 유연하지 않다는 점이다.
    * 상속은 부모 클래스와 자식 클래스 사이의 관계를 컴파일 시점에 결정한다. 
    * 따라서 실행 시점에 객체의 종류를 변경하는 것이 불가능하다.



### 합성

* 인터페이스에 정의된 메시지를 통해서만 코드를 재사용하는 방법을 **합성**이라고 부른다.
* 합성은 상속이 가지는 두 가지 문제점을 모두 해결한다.
* 인터페이스에 정의된 메시지를 통해서만 재사용이 가능하기 때문에 구현을 효과적으로 캡슐화할 수 있다.
* 또한 의존하는 인스턴스를 교체하는 것이 비교적 쉽기 때문에 설계를 유연하게 만든다.
* 상속은 클래스를 통해 강하게 결합되는 데 비해 합성은 메시지를 통해 느슨하게 결합된다.



