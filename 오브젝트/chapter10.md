# 10장 상속과 코드 재사용

* 객체지향 프로그래밍의 장점 중 하나는 코드를 재사용하기가 용이하다는 것이다.

    * 전통적인 패러다임에서 코드를 재사용하는 방법은 코드를 복사한 후 수정하는 것이다.
    * 객체지향은 조금 다른 방법을 취한다.
    * 객체지향에서는 코드를 재사용하기 위해 '새로운' 코드를 추가한다.
    * 객체지향에서 코드는 일반적으로 클래스안에 작성되기 때문에 객체지향에서 클래스를 재사용하는 전통적인 방법은 새로운 클래스를 추가하는 것이다.

* 이번 장에서는 클래스를 재사용하기 위해 새로운 클래스를 추가하는 가장 대표적인 기법인 **상속**에 관해 알아보기로 한다.

* 객체지향에서 상속 외에도 코드를 효과적으로 재사용할 수 있는 방법은 한 가지 더 있다.

    * 새로운 클래스의 인스턴스 안에 기존 클래스의 인스턴스를 포함시키는 방법으로 흔히 **합성**이라고 부른다.

* 코드를 재사용하려는 강력한 동기 이면에는 중복된 코드를 제거하려는 욕망이 숨어 있다.

    * 따라서 상속에 대해 살펴보기 전에 중복 코드가 초래하는 문제점을 살펴보는 것이 유용할 것이다.

    

## 01 상속과 중복 코드

### DRY 원칙

* 중복 코드는 변경을 방해한다.
    * 이것이 중복 코드를 제거해야 하는 가장 큰 이유다.
    * 프로그램의 본질은 비즈니스와 관련된 지식을 코드로 변환하는 것이다.
    * 안타깝게도 이 지식은 항상 변한다.
    * 그에 맞춰 지식을 표현하는 코드 역시 변경해야 한다.
    * 그 이유가 무엇이건 일단 새로운 코드를 추가하고 나면 언젠가는 변경될 것이라고 생각하는 것이 현명하다.
* 중복 코드가 가지는 가장 큰 문제는 코드를 수정하는 데 필요한 노력을 몇 배로 증가시킨다는 것이다.
    * 우선 어떤 코드가 중복인지를 찾아야 한다.
    * 일단 중복 코드의 묶음을 찾았다면 찾아낸 모든 코드를 일관되게 수정해야 한다.
    * 중복 코드는 수저오가 테스트에 드는 비용을 증가시킬뿐만 아니라 시스템과 여려분을 공황상태로 몰아넣을 수도 있다.
* 중복 여부를 판단하는 기준은 변경이다.
    * 요구사항이 변경됐을 때 두 코드를 함께 수정해야 한다면 이 코드는 중복이다.
    * 함께 수정할 필요가 없다면 중복이 아니다.
* 신뢰할 수 있고 수정하기 쉬운 소프트웨어를 만드는 효과적인 방법 중 하나는 중복을 제거하는 것이다.
    * DRY는 '반복하지 마라'라는 뜻의 Don't Repeat Yourself의 첫 글자를 모아 만든 용어로 동일한 지식을 중복하지 말라는 것이다.
    * DRY 원칙은 한 번, 단 한번(Once and Only Once)원칙 또는 단일 지점 제어 원칙이라고도 부른다.



### 중복과 변경

* 중복 코드의 문제점을 이해하기 위해 한 달에 한 번씩 가입자별로 전화 요금을 계산하는 간단한 애플리케이션을 개발해 보자.
    * 전화 요금을 계산하는 규칙은 간단한데 통화 시간을 단위 시간당 요금으로 나눠주면 된다.
    * 10초당 5원의 통화료를 부과하는 요금제에 가입돼 있는 가입자가 100초 동안 통화를 했다면 요금으로 100/10*5 = 50원이 부과된다.
* 먼저 개발 통화 기간을 저장하는 Call 클래스가 필요하다.
    * Call은 통화 시작 시간(from)과 통화 종료 시간(to)을 인스턴스 변수로 포함한다.

```java
public class Call {
    private LocalDateTime from;
    private LocalDateTime to;

    public Call(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public LocalDateTime getFrom() {
        return from;
    }
}
```

* 이제 통화 요금을 계산할 객체가 필요하다.
    * Call의 목록을 관리할 정보 전문가는 Phone이다.

```java
public class Phone {
    private Money amount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public List<Call> getCalls() {
        return calls;
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getSeconds() {
        return seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result;
    }
}
```

* 다음은 Phone을 이요해 '10초당 5원'씩 부과되는 요금제에 가입한 사용자가 각각 1분 동안 두 번 통화를 한 경우의 통화요금을 계산하는 방법을 코드로 나타낸 것이다.

```java
Phone phone = new Phone(Money.wons(5), Duration.ofSeconds(10));
phone.call(new Call(LocalDateTime.of(2018, 1, 1, 12, 10, 0),
                    LocalDateTime.of(2018, 1, 1, 12, 11, 0)));
phone.call(new Call(LocalDateTime.of(2018, 1, 2, 12, 10, 0),
                    LocalDateTime.of(2018, 1, 2, 12, 11, 0)));

phone.calculateFee(); // => Money.wons(60)
```

* 심야 할인 요금제라는 새로운 요금방식의 요구사항이 접수됐다.
* 이 요구사항을 해결할 수 있는 쉽고도 빠른 방법은 Phone의 코드를 복사해서 NightlyDiscountPhone이라는 새로운 클래스를 만든 후 수정하는 것이다.

```java
public class NightlyDiscountPhone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;
    private Money regularAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.nightlyAmount = nightlyAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public List<Call> getCalls() {
        return calls;
    }

    public Duration getSeconds() {
        return seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            }
        }

        return result;
    }
}
```

* 하지만 구현 시간을 절약한 대가로 지불해야 하는 비용은 예상보다 크다.
    * Phone과 NightlyDiscountPhone 사이에는 중복 코드가 존재하기 때문에 언제 터질지 모르는 시한폭탄을 안고 있는 것과 같다.



#### 중복 코드 수정하기

* 이번에 추가할 기능은 통화 요금에 부과할 세금을 계산하는 것이다.

```java
public class Phone {
    ...
    private double taxRate;

    public Phone(Money amount, Duration seconds, double taxRate) {
   	...
      this.taxRate = taxRate;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result.plus(result.times(taxRate));
    }
}
```

* NightlyDiscountPhone도 동일한 방식으로 수정하자.

```java
public class NightlyDiscountPhone {
    ...
    private double taxRate;

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds, double taxRate) {
 	   ...
      this.taxRate = taxRate;
    }
  
    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            }
        }

        return result.minus(result.times(taxRate));
    }
}
```

* 이 예제는 중복 코드가 가지는 단점을 잘 보여준다.
    * 중복 코드는 항상 함께 수정돼야 하기 때문에 수정할 때 하나라도 빠트린다면 버그로 이어질 것이다.



#### 타입 코드 사용하기

* 두 클래스 사이의 중복 코드를 제거하는 한 가지 방법은 클래스를 하나로 합치는 것이다.
    * 요금제를 구분하는 타입 코드를 추가하고 타입 코드의 값에 따라 로직을 분기시켜 Phone과 NightlyDiscountPhone을 하나로 합칠 수 있다.
    * 하지만 타입 코드를 사용하는 클래스는 낮은 응집도와 높은 결합도라는 문제에 시달리게 된다.

```java
public class Phone {
    private static final int LATE_NIGHT_HOUR = 22;
    enum PhoneType { REGULAR, NIGHTLY }

    private PhoneType type;

    private Money amount;
    private Money nightlyAmount;
    private Money regularAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this(PhoneType.REGULAR, amount, Money.ZERO, Money.ZERO, seconds);
    }

    public Phone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this(PhoneType.NIGHTLY, Money.ZERO, nightlyAmount, regularAmount, seconds);
    }

    public Phone(PhoneType type, Money amount, Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.type = type;
        this.amount = amount;
        this.nightlyAmount = nightlyAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            if (type == PhoneType.REGULAR) {
                result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                if (call.getFrom().getHour() > LATE_NIGHT_HOUR) {
                    result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
                } else {
                    result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
                }

            }
        }

        return result;
    }
}
```

* 객체지향 프로그래밍 언어는 타입 코드를 사용하지 않고도 중복 코드를 관리할 수 있는 효과적인 방법을 제공한다.
    * 상속이 바로 그것이다.



### 상속을 이용해서 중복 코드 제거하기

* 상속의 기본 아이디어는 매우 간단하다.
    * 이미 존재하는 클래스와 유사한 클래스가 필요하다면 코드를 복사하지 않고 상속을 이용해 코드를 재사용하라는 것이다.

```java
public class NightlyDiscountPhone extends Phone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        super(regularAmount, seconds);
        this.nightlyAmount = nightlyAmount;
    }

    @Override
    public Money calculateFee() {
      // 부모 클래스의 calculateFee 호출
        Money result = super.calculateFee();

        Money nightlyFee = Money.ZERO;
        for (Call call: getCalls()) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                nightlyFee = nightlyFee.plus(getAmount().minus(nightlyAmount).times(
                        call.getDuration().getSeconds() / getSeconds().getSeconds()
                ));
            }
        }

        return result.minus(nightlyFee);
    }
}
```

* 상속은 결합도를 높인다.
    * 그리고 상속이 초래하는 부모 클래스와 자식 클래스 사이의 강한 결합이 코드를 수정하기 어렵게 만든다.



### 강하게 결합된 Phone과 NightlyDiscountPhone

* 부모 클래스와 자식 클래스 사이의 결합이 문제인 이유를 살펴보자.
    * NightlyDiscountPhone은 부모 클래스인 Phone의 calculateFee 메서드를 오버라이딩한다.
    * 또한 메서드 안에서 super 참조를 이용해 부모 클래스의 메서드를 호출한다.
    * NightlyDiscountPhone의 calculateFee 메서드는 자신이 오버라이딩한 Phone의 calculateFee 메서드가 모든 통화에 대한 요금의 총합을 반환한다는 사실에 기반하고 있다.



...



## 02 취약한 기반 클래스 문제

* 상속은 자식 클래스와 부모 클래스의 결합도를 높인다.
    * 이 강한 결합도로 인해 자식 클래스는 부모 클래스의 불필요한 세부사항에 엮이게 된다.
    * 부모 클래스의 작은 변경에도 자식 클래스는 컴파일 오류와 실행에러라는 고통에 시달려야 할 수도 있다.
* 이처럼 부모 클래스의 변경에 의해 자식 클래스가 영향을 받는 현상을 **취약한 기반 클래스 문제(Fragile Base Class Problem, Brittle Base Class Problem)**라고 부른다.
    * 이 문제는 상속을 사용한다면 피할 수 없는 객체지향 프로그래밍의 근본적인 취약성이다.
    * 상속 관계를 추가할수록 시스템의 결합도가 높아진다는 사실을 알고 있어야 한다.
    * 상속은 자식 클래스를 점진적으로 추가해서 기능을 확장하는 데는 용이하지만 높은 결합도로 인해 부모 클래스를 점진적으로 개선하는 것은 어렵게 만든다.
* 취약한 기반 클래스 문제는 캡슐화를 약화시키고 결합도를 높인다.
    * 상속은 자식 클래스가 부모 클래스의 구현 세부사항에 의존하도록 만들기 때문에 캡슐화를 약화시킨다.
    * 이것이 상속이 위험한 이유인 동시에 우리가 상속을 피해야 하는 첫 번째 이유다.
* 객체를 사용하는 이유는 구현과 관련된 세부사항을 퍼블릭 인터페이스 뒤로 캡슐화할 수 있기 때문이다.
    * 캡슐화는 변경에 의한 파급효과를 제어할 수 있기 때문에 가치가 있다.
    * 객체는 변경될지도 모르는 불안정한 요소를 캡슐화함으로써 파급효과를 제어할 수 있기 때문에 가치가 있다.
* 안타깝게도 상속을 사용하면 부모 클래스의 퍼블릭 인터페이스가 아닌 구현을 변경하더라도 자식 클래스가 영향을 받기 쉬워진다.
    * 상속 계층의 상위에 위치한 클래스에 가해지는 작은 변경만으로도 상속 계층에 속한 모든 자손들이 급격하게 요동칠 수 있다.
* 객체지향의 기반은 캡슐화를 통한 변경의 통제다.
    * 상속은 코드의 재사용을 위해 캡슐화의 장점을 희석시키고 구현에 대한 결합도를 높임으로써 객체지향이 가진 강력함을 반감시키다.



### 불필요한 인터페이스 상속 문제

...



### 메서드 오버라이딩의 오작용 문제

...



### 부모 클래스와 자식 클래스의 동시 수정 문제

...



## 03 Phone 다시 살펴보기

### 추상화에 의존하자

* NightlyDiscountPhone의 가장 큰 문제점은 Phone에 강하게 결합돼 있기 때문에 Phone이 변경될 경우 함께 변경될 가능성이 높다는 것이다.
    * 이 문제를 해결하는 가장 일반적인 방법은 자식 클래스가 부모 클래스의 구현이 아닌 추상화에 의존하도록 만드는 것이다.
    * 정확하게 말하면 부모 클래스와 자식 클래스 모두 추상화에 의존하도록 수정해야 한다.
* 개인적으로 코드 중복을 제거하기 위해 상속을 도입할 때 따르는 두 가지 원칙이 있다.
    * 두 메서드가 유사하게 보인다면 차이점을 메서드로 추출하라. 메서드 추출을 통해 두 메서드를 동일한 형태로 보이도록 만들 수 있다.
    * 부모 클래스의 코드를 하위로 내리지 말고 자식 클래스의 코드를 상위로 올려라. 



### 차이를 메서드로 추출하라

* 가장 먼저 할 일은 중복 코드 안에서 차이점을 별도의 메서드로 추출하는 것이다.
    * 이것은 흔히 말하는 "변하는 것으로부터 변하지 않는 것을 분리하라", 또는 "변하는 부분을 찾고 이를 캡슐화하라"라는 조언을 메서드 수준에서 적용한 것이다.

```java
public class Phone {
    private Money amount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result;
    }
}
```

```java
public class NightlyDiscountPhone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;
    private Money regularAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.nightlyAmount = nightlyAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            }
        }

        return result;
    }
}
```

* 먼저 할 일은 두 클래스의 메서드에서 다른 부분을 별도의 메서드로 추출하는 것이다.
    * 이 경우에는 calculateFee의 for 문 안에 구현된 요금 계산 로직이 서로 다르다는 사실을 알 수 있다.

```java
public class Phone {
    ...

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result;
    }
  
    private Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
```

```java
public class NightlyDiscountPhone {
    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            }
        }

        return result;
    }
  
    private Money calculateCallFee(Call call) {
        if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
            return nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        } else {
            return regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        }
    }
}
```

* 두 클래스의 calculateFee 메서드는 완전히 동일해졌고 추출한 calculateCallFee 메서드 안에 서로 다른 부분을 격리시켜 놓았다.
    * 이제 같은 코드를 부모 클래스로 올리는 일만 남았다.



### 중복 코드를 부모 클래스로 올려라

* 부모 클래스를 추가하자.
    * 새로운 부모 클래스의 이름은 AbstractPhone으로 하고 Phone과 NightlyDiscountPhone이 AbstractPhone을 상속받도록 수정하자.

```java
public abstract class AbstractPhone {}

public class Phone extends AbstractPhone { ... }

public class NightlyDiscountPhone extends AbstractPhone { ... }
```

```java
public abstract class AbstractPhone {
    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(calculateCallFee(call));
        }

        return result;
    }

    abstract protected Money calculateCallFee(Call call);
}
```

```java
public class Phone extends AbstractPhone {
    private Money amount;
    private Duration seconds;

    public Phone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
```

```java
public class NightlyDiscountPhone extends AbstractPhone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money regularAmount;
    private Money nightlyAmount;
    private Duration seconds;

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.nightlyAmount = nightlyAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    protected Money calculateCallFee(Call call) {
        if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
            return nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        } else {
            return regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds());
        }
    }
}
```



### 추상화가 핵심이다

* 공통 코드를 이동시킨 후에 각 클래스는 서로 다른 변경의 이유를 가진다는 것에 주목하라.
    * AbstractPhone은 전체 통화 목록을 계산하는 방법이 바뀔 경우에만 변경된다.
    * Phone은 일반 요금제의 통화 한 건을 계산하는 방식이 바뀔 경우에만 변경된다.
    * NightlyDiscountPhone은 심야 할인 요금제의 통화 한 건을 계산하는 방식이 바뀔 경우에만 변경된다.
    * 세 클래스는 각각 하나의 변경 이유만을 가진다.
    * 이 클래스들은 단일 책임 원칙을 준수하기 때문에 응집도가 높다.
* 사실 부모 클래스 역시 자신의 내부에 구현된 추상 메서드를 호출하기 때문에 추상화에 의존한다고 말할 수 있다.
    * 의존성 역전 원칙도 준수하는데, 요금 계산과 관련된 상위 수준의 정책을 구현하는 AbstractPhone이 세부적인 요금 계산 로직을 구현하는 Phone과 NightDiscountPhone에 의존하지 않고 그 반대로 Phone과 NightDiscountPhone이 추상화인 AbstractPhone에 의존하기 때문이다.
* 새로운 요금제를 추가하기도 쉽다는 사실 역시 주목하라.
    * 새로운 요금제가 필요하다면 AbstractPhone을 상속받는 새로운 클래스를 추가한 후 calculateCallFee 메서드만 오버라이딩하면 된다.
    * 현재의 설계는 확장에는 열려있고 수정에는 닫혀있기 때문에 개방-폐쇄 원칙 역시 준수한다.



### 의도를 드러내는 이름 개선하기

* NightlyDiscountPhone이라는 이름은 심야 할인 요금제와 관련된 내용을 구현한다는 사실을 명확하게 전달한다.
    * 그에 반해 Phone은 일반 요금제와 관련된 내용을 구현한다는 사실을 명시적으로 전달하지 못한다.
    * AbstractPhone이라는 이름은 전화기를 포괄한다는 의미를 명확하게 전달하지 못한다.
    * 따라서 AbstractPhone은 Phone으로, Phone은 RegularPhone으로 변경하는 것이 적절할 것이다.



### 세금 추가하기

...



## 04 차이에 의한 프로그래밍

* 상속을 사용하면 이미 존재하는 클래스의 코드를 기반으로 다른 부분을 구현함으로써 새로운 기능을 쉽고 빠르게 추가할 수 있다.
* 기존 코드와 다른 부분만을 추가함으로써 애플리케이션의 기능을 확장하는 방법을 **차이에 의한 프로그래밍(programming by difference)**이라고 부른다.
* 차이에 의한 프로그래밍의 목표는 중복 코드를 제거하고 코드를 재사용하는 것이다.
    * 중복 코드를 제거하기 위해 최대한 코드를 재사용해야 한다.
* 코드를 재사용하는 것은 단순히 문자를 타이핑하는 수고를 덜어주는 수준의 문제가 아니다.
    * 재사용 가능한 코드란 심각한 버그가 존재하지 않는 코드다.
* 객체지향 세계에서 중복 코드를 제거하고 코드를 재사용할 수 있는 가장 유명한 방법은 상속이다.
* 상속은 강력한 도구다.
* 상속이 코드 재사용이라는 측면에서 매우 강력한 도구인 것은 사실이지만 강력한 만큼 잘못 사용할 경우에 돌아오는 피해 역시 크다는 사실을 뼈저리게 경험한 것이다.
    * 상속의 오용과 남용은 애플리케이션을 이해하고 확장하기 어렵게 만든다.
    * 정말로 필요한 경우에만 상속을 사용하라.











