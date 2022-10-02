# 11장 합성과 유연한 설계

* 상속과 합성은 객체지향 프로그래밍에서 가장 널리 사용되는 코드 재사용 기법이다.
    * 상속이 부모 클래스와 자식 클래스를 연결해서 부모 클래스의 코드를 재사용하는 데 비해 합성은 전체를 표현하는 객체가 부분을 표현하는 객체를 포함해서 부분 객체의 코드를 재사용한다.
    * 상속에서 부모 클래스와 자식 클래스 사이의 의존성은 컴파일타임에 해결되지만 합성에서 두 객체 사이의 의존성은 런타임에 해결된다.
    * 상속관계는 is-a 관계라고 부르고 합성 관계는 has-a 관계라고 부른다.
* 상속을 제대로 활용하기 위해서는 부모 클래스의 내부 구현에 대해 상세하게 알아야 하기 때문에 자식 클래스와 부모 클래스 사이의 결합도가 높아질 수밖에 없다.
* 합성은 구현에 의존하지 않는다는 점에서 상속과 다르다.
    * 합성은 내부에 포함되는 객체의 구현이 아닌 퍼블릭 인터페이스에 의존한다.
    * 따라서 합성을 이용하면 포하된 객체의 내부 구현이 변경되더라도 영향을 최소화할 수 있기 때문에 더 안정적인 코드를 얻을 수 있게 된다.
* 상속 관계는 클래스 사이의 정적인 관계인 데 비해 합성 관계는 객체 사이의 동적인 관계다.
    * 코드 작성 시점에 결정한 상속 관계는 변경이 불가능하지만 합성 관계는 실행 시점에 동적으로 변경할 수 있기 때문이다.

> [코드 재사용을 위해서는] 객체 합성이 클래스 상속보다 더 좋은 방법이다.

* 상속은 합성과 재사용의 대상이 다르다.
    * 상속은 부모 클래스 안에 구현된 코드 자체를 재사용하지만 합성은 포함되는 객체의 인터페이스를 재사용한다.



## 01 상속을 합성으로 변경하기

### 불필요한 인터페이스 상속 문제: java.util.Properties와 java.util.Stack





### 메서드 오버라이딩의 오작용 문제: InstrumentedHashSet





### 부모 클래스와 자식 클래스의 동시 수정 문제: PersonalPlaylist





## 02 상속으로 인한 조합의 폭발적인 증가

* 상속으로 인해 결합도가 높아지면 코드를 수정하는 데 필요한 작업의 양이 과도하게 늘어나는 경향이 있다.
    * 가장 일반적인 상황은 작은 기능들을 조합해서 더 큰 기능을 수행하는 객체를 만들어야 하는 경우다.
    * 일반적으로 다음과 같은 두 가지 문제점이 발생한다.
        * 하나의 기능을 추가하거나 수정하기 위해 불필요하게 많은 수의 클래스를 추가하거나 수정해야 한다.
        * 단일 상속만 지원하는 언어에서는 상속으로 인해 오히려 중복 코드의 양이 늘어날 수 있다.



### 기본 정책과 부가 정책 조합하기

* 현재 시스템에는 일반 요금제와 심야 요금제라는 두 가지 종류의 요금제가 존재한다.
    * 새로운 요구사항은 이 두 요금제에 부가 정책을 추가하는 것이다.
    * 지금부터는 핸드폰 요금제가 '기본 정책'과 '부가 정책'을 조합해서 구성된다고 가정할 것이다.
* 기본 정책은 가입자의 통화 정보를 기반으로 한다.
    * 기본 정책은 가입자의 한달 통화량을 기준으로 부과할 요금을 계산한다.
* 부가 정책은 통화량과 무관하게 기본 정책에 선택적으로 추가할 수 있는 요금 방식을 의미한다.



### 상속을 이용해서 기본 정책 구현하기

```java
public abstract class Phone {
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

public class RegularPhone extends Phone {
    private Money amount;
    private Duration seconds;

    public RegularPhone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}

public class NightlyDiscountPhone extends Phone {
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



### 기본 정책에 세금 정책 조합하기

* 만약 일반 요금제에 세금 정책을 조합해야 한다면 어떻게 해야 할까?
    * 가장 간단한 방법은 RegularPhone 클래스를 상속받은 TexableRegularPhone 클래스를 추가하는 것이다.

```java
public class TaxableRegularPhone extends RegularPhone {
    private double taxRate;

    public TaxableRegularPhone(Money amount, Duration seconds, double taxRate) {
        super(amount, seconds);
        this.taxRate = taxRate;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        Money fee = super.calculateFee();
        return fee.plus(fee.times(taxRate));
    }
}
```



```java
public abstract class Phone {
    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call: calls) {
            result = result.plus(calculateCallFee(call));
        }

        return afterCalculated(result);
    }

    abstract protected Money calculateCallFee(Call call);
    abstract protected Money afterCalculated(Money fee);
}
```

```java
public class RegularPhone extends Phone {
    private Money amount;
    private Duration seconds;

    public RegularPhone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }

    @Override
    protected Money afterCalculated(Money fee) {
        return fee;
    }
}
```

```java
public class NightlyDiscountPhone extends Phone {
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

    @Override
    protected Money afterCalculated(Money fee) {
        return fee;
    }
}
```



### 기본 정책에 기본 요금 할인 정책 조합하기





### 중복 코드와 덫에 걸리다





## 03 합성 관계로 변경하기





### 기본 정책 합성하기





### 부가 정책 적용하기





### 기본 정책과 부가 정책 합성하기





### 새로운 정책 추가하기





### 객체 합성이 클래스 상속보다 더 좋은 방법이다





## 04 믹스인





### 기본 정책 구현하기





### 트레이트로 부가 정책 구현하기





### 부가 정책 트레이트 믹스인하기





### 쌓을 수 있는 변경




