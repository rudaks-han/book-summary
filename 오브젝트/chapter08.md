# 8장 의존성 관리하기

* 잘 설계된 객체지향 애플리케이션은 작고 응집도 높은 객체들로 구성된다.
    * 작고 응집도 높은 객체란 책임의 초점이 명확하고 한 가지 일만 잘 하는 객체를 의미한다.
    * 이런 작은 객체들이 단독으로 수행할 수 있는 작업은 거의 없기 때문에 다른 객체에게 도움을 요청해야 한다.
    * 이런 요청이 객체 사이의 협력을 낳는다.
* 협력은 필수적이지만 과도한 협력은 설계를 곤경에 빠트릴 수 있다.
    * 협력은 객체가 다른 객체에 대해 알것을 강요한다.
    * 이런 지식이 객체 사이의 의존성을 낳는다.
* 협력을 위해서는 의존성이 필요하지만 과도한 의존성은 애플리케이션을 수정하기 어렵게 만든다.
    * 객체지향 설계란 의존성을 관리하는 것이고 객체가 변화를 받아들일 수 있게 의존성을 정리하는 기술이라고 할 수 있다.



## 01 의존성 이해하기

### 변경과 의존성

* 의존성은 실행 시점과 구현 시점에 서로 다른 의미를 가진다.
    * **실행 시점**: 의존하는 객체가 정상적으로 동작하기 위해서는 실행 시에 의존 대상 객체가 반드시 존재해야 한다.
    * **구현 시점**: 의존 대상 객체가 변경될 경우 의존하는 객체도 함께 변경된다.



* PeriodCondition 클래스의 isSatisfiedBy 메서드는 Screening 인스턴스에게 getStartTime 메시지를 전송한다.

```java
public class PeriodCondition implements DiscountCondition {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) &&
                startTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getStartTime().toLocalTime()) >= 0;
    }
}
```

* 실행 시점에 PeriodCondition의 인스턴스가 정상적으로 동작하기 위해서는 Screening의 인스턴스가 존재해야 한다.
* 이처럼 어떤 객체가 예정된 작업을 정상적으로 수행하기 위해 다른 객체를 필요로 하는 경우 두 객체 사이에 의존성이 존재한다고 말한다.
    * 의존성은 방향성을 가지며 항상 단방향이다.
    * Screening이 변경될 때 PeriodCondition이 영향을 받게 되지만 그 역은 성립하지 않는다.
* 설계와 관련된 대부분의 용어들이 변경과 관련이 있다.
    * 의존성 역시 마찬가지다.
    * 두 요소 사이의 의존성은 의존되는 요소가 변경될 때 의존하는 요소도 함께 변경될 수 있다는 것을 의미한다.



### 의존성 전이

* 의존성은 전이될 수 있다.
    * Screening의 코드를 살펴보면 Screening이 Movie, LocalDateTime, Customer에 의존한다는 사실을 알 수 있다.
    * 의존성 전이가 의미하는 것은 PeriodCondition이 Screening에 의존할 경우 PeriodCondition은 Screening이 의존하는 대상에 대해서도 자동적으로 의존하게 된다는 것이다.
* 의존성은 전이될 수 있기 때문에 의존성의 종류를 직접 의존성(direct dependency)과 간접 의존성(indirect dependency)으로 나누기도 한다.
    * 직접 의존성이란 말 그대로 한 요소가 다른 요소에 직접 의존하는 경우를 가리킨다.
    * PeriodCondition이 Screening에 의존하는 경우가 여기에 속하며, 이 경우 의존성은 PeriodCondition의 코드에 명시적으로 드러난다.
    * 간접 의존성이란 직접적인 관계는 존재하지 않지만 의존성 전이에 의해 영향이 전파되는 경우를 가리킨다.



### 런타임 의존성과 컴파일타임 의존성

* 의존성과 관련해서 다뤄야 하는 또 다른 주제는 **런타임 의존성(run-time dependency)**과 **컴파일타임 의존성(compile-time dependency)**의 차이다.
* 런타임은 간단하다.
    * 말 그대로 애플리케이션이 실행되는 시점을 가리킨다.
    * 컴파일타임 의존성이라는 용어가 중요하게 생각하는 것은 시간이 아니라 우리가 작성한 코드의 구조이기 때문이다.
* 객체지향 애플리케이션에서 런타임의 주인공은 객체다.
    * 따라서 런타임 의존성이 다루는 주제는 객체 사이의 의존성이다.
    * 반면 코드 관점에서 주인공은 클래스다. 
    * 따라서 컴파일 타임 의존성이 다루는 주제는 클래스 사이의 의존성이다.
* 여기서 다를 수 있는 것은 런타임 의존성과 컴파일타임 의존성이 다를 수 있다는 것이다.
    * 사실 유연하고 재사용 가능한 코드를 설계하기 위해서는 두 종류의 의존성을 서로 다르게 만들어야 한다.
* 영화 예매 시스템을 예로 들어보자.
    * Movie는 가격을 계산하기 위해 비율 할인 정책과 금액 할인 정책 모두를 적용할 수 있게 설계해야 한다.
    * 다시 말해서 Movie는 AmountDiscountPolicy와 PercentDiscountPolicy 모두와 협력할 수 있어야 한다.
* 여기서 중요한 것은 Movie 클래스에서 AmountDiscountPolicy 클래스와 PercentDiscountPolicy 클래스로 향하는 어떤 의존성도 존재하지 않는다는 것이다.
    * Movie 클래스는 오직 추상 클래스인 DiscountPolicy 클래스에만 의존한다.

```java
public class Movie {]
    ...
    private DiscountPolicy discountPolicy;
                    
    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        ...
        this.discountPolicy = discountPolicy;
    }
                    
    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
}
```

* 하지만 런타임 의존성을 살펴보면 상황이 완전히 달라진다.
    * 금액 할인 정책을 적용하기 위해서는 AmountDiscountPolicy의 인스턴스와 협력해야 한다.
    * 비율 할인 정책을 적용하기 위해서는 PercentDiscountPolicy의 인스턴스와 협력해야 한다.
    * 만약 Movie 클래스가 AmountDiscountPolicy 클래스에 대해서만 의존한다면 PercentDiscountPolicy 인스턴스와 협력하는 것은 불가능할 것이다.
* Movie의 인스턴스가 이 두 클래스의 인스턴스와 함께 협력할 수 있게 만드는 더 나은 방법은 Movie가 두 클래스 중 어떤 것도 알지 못하게 만드는 것이다.
    * 대신 두 클래스 모두를 포괄하는 DiscountPolicy라는 추상 클래스에 의존하도록 만들고 이 컴파일타임 의존성을 실행 시에 PercentDiscountPolicy 인스턴스나 AmountDiscountPolicy 인스턴스에 대한 런타임 의존성으로 대체해야 한다.



### 컨텍스트 독립성

* 구체 클래스에 의존하는 것은 클래스의 인스턴스가 어떤 문맥에서 사용될 것인지를 구체적으로 명시하는 것과 같다.
    * Movie 클래스 안에 PercentDiscountPolicy 클래스에 대한 컴파일타임 의존성을 명시적으로 표현하는 것은 Movie가 비율 할인 정책이 적용된 영화의 요금을 계산하는 문맥에서 사용될 것이라는 것을 가정하는 것이다.
    * 이와 달리 Movie 클래스에 추상 클래스인 DiscountPolicy에 대한 컴파일 타임 의존성을 명시하는 것은 Movie가 할인 정책에 따라 요금을 계산하지만 구체적으로 어떤 정책을 따르는지는 결정하지 않겠다고 선언하는 것이다.
* 클래스가 특정한 문맥에 강하게 결합될수록 다른 문맥에서 사용하기는 더 어려워진다.
    * 클래스가 사용될 특정한 문맥에 대해 최소한의 가정만으로 이뤄져 있다면 다른 문맥에서 재사용하기가 더 수월해진다. 
    * 이를 **컨텍스트 독립성**이라고 부른다.
* 설계가 유연해지기 위해서는 가능한 한 자신이 실행될 컨텍스에 대한 구체적인 정보를 최대한 적게 알아야 한다.
    * 컨텍스트에 대한 정보가 적으면 적을수록 더 다양한 컨텍스트에서 재사용될 수 있기 때문이다.



### 의존성 해결하기

* 컴파일타임 의존성은 구체적인 런타임 의존성으로 대체돼야 한다.
    * Movie 클래스는 DiscountPolicy 클래스에 의존한다.
    * 이것은 컴파일 타임 의존성이다.
* 이처럼 컴파일타임 의존성을 실행 컨텍스트에 맞는 적절한 런타임 의존성으로 교체하는 것을 **의존성 해결**이라고 부른다. 
    * 의존성을 해결하기 위해서는 일반적으로 다음과 같은 세 가지 방법을 사용한다.
        * 객체를 생성하는 시점에 생성자를 통해 의존성 해결
        * 객체 생성 후 setter 메서드를 통해 의존성 해결
        * 메서드 실행 시 인자를 이용해 의존성 해결

```java
// 생성자 이용
Movie avatar = new Movie("아바타",
                       Duration.ofMinutes(120),
                       Money.wons(10000),
                       new AmountDiscountPolicy(...));
```

* Movie의 생성자에 PercentDiscountPolicy의 인스턴스를 전달하면 비율 할인 정책에 따라 요금을 계산하게 될 것이다.

```java
// 생성자 이용
Movie avata = new Movie("스타워즈",
                       Duration.ofMinutes(180),
                       Money.wons(11000),
                       new PercentDiscountPolicy(...));
```

* 부모 클래스의 DiscountPolicy 타입의 인자를 받는 생성자를 정의한다.

```java
public Movie {
    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        ...
        this.discountPolicy = discountPolicy;
    } 
}
```

* Movie의 인스턴스를 생성한 후 메서드를 이용해 의존성을 해결하는 방법도 있다.

```java
Movie avatar = new Movie(...);
avatar.setDiscountPolicy(new AmountDiscountPolicy(...));
```

* 이 경우 Movie 인스턴스가 생성된 후에도 DiscountPolicy를 설정할 수 있는 setter 메서드를 제공해야 한다.

```java
public class Movie {
  public void setDiscountPolicy(DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;
  }
}
```

* setter 메서드를 이용하면 금액 할인 정책으로 설정된 Movie의 인스턴스를 중간에 비율 할인 정책으로 변경할 수 있다.

```java
Movie avatar = new Movie(...);
avatar.setDiscountPolicy(new AmountDiscountPolicy(...));
...
avatar.setDiscountPolicy(new PercentDiscountPolicy(...))l
```

* setter 메서드를 이용하는 방법은 실행 시점에 의존 대상을 변경할 수 있기 때문에 설계를 좀 더 유연하게 만들 수 있다.
    * 단점은 객체가 생성된 후에 협력에 필요한 의존 대상을 설정하기 때문에 객체를 생성하고 의존 대상을 설정하기 전까지 객체의 상태가 불완전할 수 있다는 점이다.
    * 아래 코드에서처럼 setter 메서드를 이용해 인스턴스 변수를 설정하기 위해 내부적으로 해당 인스턴스 변수를 사용하는 코드를 실행하면 NullPointerException 예외가 발생할 것이다.

```java
Movie avatar = new Movie(...);
avatar.calculateFee(...); // 예외 발생
avatar.setDiscountPolicy(new AmountDiscountPolicy(...));
```

* 더 좋은 방법은 생성자 방식과 setter 방식을 혼합하는 것이다.

```java
Movie avata = new Movie(..., new PercentDiscountPolicy(...));
...
avatar.setDiscountPolicy(new AmountDiscountPolicy(...));
```



## 유연한 설계

### 의존성과 결합도

* 객체지향 패러다임의 근간은 협력이다.
    * 객체들은 협력을 통해 애플리케이션에 생명력을 불어 넣는다.
    * 객체들이 협력하기 위해서는 서로의 존재와 수행 가능한 책임을 알아야 한다.
    * 이런 지식들이 객체 사이의 의존성을 낳는다.
    * 따라서 모든 의존서잉 나쁜 것은 아니다.
    * 의존성은 객체들의 협력을 가능하게 만드는 매개체라는 관점에서 바람직한것이다.
    * 하지만 의존성이 과하면 문제가 될 수 있다.
* Movie가 비율 할인 정책을 구현하는 PecentDiscountPolicy에 직접 의존한다고 가정해보자.

```java
public class Movie {
  ...
  private PercentDiscountPolicy percentDiscountPolicy;
  
  public Movie(String title, Duration runningTime, Money fee, PercentDiscountPolicy percentDiscountPolicy) {
    this.percentDiscountPolicy = percentDiscountPolicy;
  }
  
  public Money calculateMovieFee(Screening screening) {
    return fee.minus(percentDiscountPolicy.calculateDiscountAmount(screening));
  }
}
```

* 이 코드는 비율 할인 정책을 적용하기 위해 Movie가 PercentDiscountPolicy에 의존하고 있다는 사실을 코드를 통해 명시적으로 드러낸다.

* 만약 Movie가 PercentDiscountCondition 뿐만 아니라 AmountDiscountCondition과도 협력해야 한다면 어떻게 해야 할까?
* 해결 방법은 의존성을 바람직하게 만드는 것이다.
    * Movie가 협력하고 싶은 대상의 반드시 PercentDiscountCondition의 인스턴스일 필요는 없다는 사실을 주목하라.
    * 자신이 전송하는 calculateDiscountAmount 메시지를 이해할 수 있고 할인된 요금을 계산할 수 있다면 상관없다.
* 추상 클래스인 DiscountPolicy는 calculateDiscountAmount 메시지를 이해할 수 있는 타입을 정의함으로써 이 문제를 해결한다.
* 이 예는 의존성 자체가 나쁜 것이 아니라는 사실을 보여준다.
    * 의존성은 협력을 위해 반드시 필요한 것이다. 단지 바람직하지 못한 의존성이 문제일 뿐이다.
* 그렇다면 바람직한 의존성이란 무엇인가?
    * 바람직한 의존성은 재사용성과 관련이 있다.
    * 어떤 의존성이 다양한 환경에서 클래스를 재사용할 수 없도록 제한한다면 그 의존성은 바람직하지 못한 것이다.
    * 어떤 의존성이 다양한 환경에서 재사용할 수 있다면 그 의존성은 바람직한 것이다.
    * 특정 컨텍스트에 강하게 결합된 의존성은 바람직하지 않은 의존성이다.
* 바람직한 의존성과 바람직하지 못한 의존성을 가리키는 좀 더 세련된 용어가 존재한다.
    * 결합도가 바로 그것이다.
    * 어떤 두 요소 사이에 존재하는 의존성이 바람직할 때 두 요소가 **느슨한 결합도(loose coupling)** 또는 **약한 결합도(weak coupling)**를 가진다고 말한다.
    * 반대로 두 요소 사이의 의존성이 바람직하지 못할 때 **단단한 결합도(tight coupling)** 또는 **강한 결합도(strong coupling)** 를 가진다고 말한다.



### 지식이 결합을 낳는다

* 앞에서 Movie가 PercentDiscountPolicy에 의존하는 경우에는 결합도가 강하다고 표현했다.
    * 반면 Movie가 DiscountPolicy에 의존하는 경우에는 결합도가 느슨하다고 표현했다.
* 서로에 대해 알고 있는 지식의 양이 결합도를 결정한다.
    * Movie 클래스가 PercentDiscountPolicy 클래스에 직접 의존한다고 가정하자.
    * 이 경우 Movie는 협력할 객체가 비율 할인 정책에 따라 할인 요금을 계산할 것이라는 사실을 알고 있다.
* 반면 Movie 클래스가 추상 클래스인 DiscountPolicy 클래스에 의존하는 경우에는 구체적인 계산 방법은 알 필요가 없다.
    * 그저 할인 요금을 계산한다는 사실만 알고 있을 뿐이다.
* 더 많이 알수록 더 많이 결합된다.
    * 결합도를 느슨하게 만들기 위해서는 협력하는 대상에 대해 필요한 정보 외에는 최대한 감추는 것이 중요하다.
* 이 목적을 달성할 수 있는 가장 효과적인 방법에 대해 이미 알고 있다는 것이다.
    * 추상화가 바로 그것이다.



### 추상화에 의존하라

* 추상화란 어떤 양상, 세부사항, 구조를 좀 더 명확하게 이해하기 위해서 특정 절차나 물체를 의도적으로 생략하거나 감춤으로써 복잡도를 극복하는 방법이다.
    * 추상화를 사용하면 현재 다루고 있는 문제를 해결하는 데 불필요한 정보를 감출 수있다.
    * 따라서 대상에 대해 알아야 하는 지식의 양을 줄일 수 있기 때문에 결합도를 느슨하게 유지할 수 있다.
* 구체 클래스에 비해 추상 클래스는 메서드의 내부 구현과 자식 클래스의 종류에 대해 지식을 클라이언트에게 숨길 수있다.
    * 따라서 클라이언트가 알아야 하는 지식의 양이 더 적기 때문에 구체 클래스보다 추상 클래스에 의존하는 것이 결합도가 더 낮다.
    * 하지만 추상 클래스의 클라이언트는 여전히 협력하는 대상이 클래스 상속 계층이 무엇인지에 대해서는 알고 있어야 한다.
* 인터페이스에 의존하면 상속 계층을 모르더라도 협력이 가능해진다.
    * 인터페이스 의존성은 협력하는 객체가 어떤 메시지를 수신할 수 있는지에 대해 지식만을 남기기 때문에 추상 클래스 의존성보다 결합도가 낮다.
    * 이것은 다양한 클래스 상속 계층에 속한 객체들이 동일한 메시지를 수신할 수 있도록 컨텍스트를 확장하는 것을 가능하게 한다.
* 여기서 중요한 것은 실행 컨텍스트에 대해 알아야 하는 정보를 줄일수록 결합도가 낮아진다는 것이다.
    * 결합도를 느슨하게 만들기 위해서는 구체적인 클래스보다 추상 클래스에, 추상 클래스보다 인터페이스에 의존하도록 만드는 것이 더 효과적이다.
    * 다시 말해 의존하는 대상이 더 추상적일수록 결합도는 더 낮아진다는 것이다. 이것이 핵심이다.



### 명시적인 의존성

* 아래 코드느 한 가지 실수로인해 결합도가 불필요하게 높아졌다. 그 실수는 무엇일까?

```java
public class Movie {
  ...
  private DiscountPolicy discountPolicy;
  
  public Movie(String title, Duration runningTime, Money fee) {
    ...
    this.discountPolicy = new AmountDiscountPolicy(...);
  }
}
```

* Movie의 인스턴스 변수인 discountPolicy는 추상 클래스인 DiscountPolicy 타입으로 선언돼 있다.
    * Movie는 추상화에 의존하기 때문에 이 코드는 유연하고 재사용 가능할 것처럼 보인다.
    * 하지만 안타깝게도 생성자를 보면 그렇지 않다는 사실을 알 수있다.
    * discountPolicy는 DiscountPolicy 타입으로 선언돼 있지만 생성자에서 구체 클래스인 AmountDiscountPolicy의 인스턴스를 직접 생성해서 대입하고 있다.
    * 따라서 Movie는 추상 클래스인 DiscountPolicy 뿐만 아니라 구체 클래스인 AmountDiscountPolicy에도 의존하게 된다.
* 앞에서 설명했던 것처럼 의존성을 해결하는 방법에는 생성자, setter 메서드, 메서드 인자를 사용하는 세 가지 방식이 존재한다.
* 다음은 생성자를 사용해 의존성을 해결하는 경우를 나타낸 것이다.

```java
public class Movie {
  ...
  private DiscountPolicy discountPolicy;
  
  public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
    ...
    this.discountPolicy = discountPolicy;
  }
}
```

* 의존성의 대상을 생성자의 인자로 전달받는 방법과 생성자 안에서 직접 생성하는 방법 사이의 가장 큰 차이점은 퍼블릭 인터페이스를 통해 할인 정책을 설정할 수 있는 방법을 제공하는지 여부다.
    * 생성자의 인자로 선언하는 방법은 Movie가 DiscountPolicy에 의존한다는 사실을 Movie의 퍼블릭 인터페이스에 드러내는 것이다.
    * 이것은 setter 메서드를 사용하는 방식과 메서드 인자를 사용하는 방식의 경우에도 동일하다.
    * 모든 경우에 의존성은 명시적으로 퍼블릭 인터페이스에 노출된다.
    * 이를 **명시적인 의존성(explicit dependency)**이라고 부른다.
* 반면 Movie의 내부에서 AmountDiscountPolicy의 인스턴스를 직접 생성하는 방식은 Movie가 DiscountPolicy에 의존한다는 사실을 감춘다.
    * 다시 말해 의존성이 퍼블릭 인터페이스에 표현되지 않는다.
    * 이를 **숨겨진 의존성(hidden dependency)**이라고 부른다.
* 의존성은 명시적으로 표현돼야 한다.
    * 의존성을 구현 내부에 숨겨두지 마라.
    * 명시적인 의존성을 사용해야만 퍼블릭 인터페이스를 통해 컴파일타임 의존성을 적절한 런타임 의존성으로 교체할 수있다.
* 클래스가 다른 클래스를 의존하는 것은 부끄러운 일이 아니다. 
    * 의존성은 다른 객체와의 협력을 가능하게 해주기 때문에 바람직한 것이다.
    * 경계해야 할 것은 의존성 자체가 아니라 의존성을 감추는 것이다.



### new는 해롭다

* 결합도 측면에서 new가 해로운 이유는 크게 두 가지다.
    * new 연산자를 사용하기 위해서는 구체 클래스의 이름을 직접 기술해야 한다. 따라서 new를 사용하는 클라이언트는 추상화가 아닌 구체 클래스에 의존할 수밖에 없기 때문에 결합도가 높아진다.
    * new 연산자는 생성하려는 구체 클래스뿐만 아니라 어떤 인자를 이용해 클래스의 생성자를 호출해야 할는지도 알아야 한다. 따라서 new를 사용하면 클라이언트가 알아야 하는 지식의 양이 늘어나기 때문에 결합도가 높아진다.
* 해결 방법은 인스턴스를 생성하는 로직과 생성된 인스턴스를 사용하는 로직을 부닐하는 것이다.
    * AmountDiscountPolicy를 사용하는 Movie는 인스턴스를 생성해서는 안 된다. 단지 해당하는 인스턴스를 사용하기만 해야 한다.
    * 이를 위해 Movie는 외부로부터 이미 생성된 AmountDiscountPolicy의 인스턴스를 전달받아야 한다.



### 가끔은 생성해도 무방하다





### 표준 클래스에 대한 의존은 해롭지 않다





### 컨텍스트 확장하기

* 첫 번째는 할인 혜택을 제공하지 않는 영화의 예매 요금을 계산하는 경우다.
    * 쉽게 생각할 수 있는 방법은 discountPolicy에 null을 할당
    * calculateMovieFee에 null인지 체크한다.
    * 해결책은 할인 정책이 존재하지 않는다는 사실을 예외 케이스로 처리하지 말고 기존 Movie와 DiscountPolicy가 협력하던 방식을 따르도록 만드는 것이다.
* 두 번째 예는 중복 적용이 가능한 할인 정책을 구현하는 것이다.
    * public class OverlappedDiscountPolicy extends DiscountPolicy
    * Movie를 수정하지 않고도 할인 정책을 적용하지 않는 새로운 기능을 추가하는 것이 얼마나 간단한지를 잘 보여준다.



### 조합 가능한 행동

