# 5장 책임 할당하기

* 데이터 중심 접근법의 문제점
    * 행동보다 데이터를 먼저 결정하고 협력이라는 문맥을 벗어나 고립된 객체의 상태에 초점을 맞추기 때문에 캡슐화를 위반하고, 요소들 사이의 결합도가 높아지며, 코드를 변경하기 어려워진다.
* 데이터 중심 설계로 인해 발생하는 문제점을 해결할 수 있는 가장 기본적인 방법은 데이터가 아닌 책임에 초점을 맞추는 것이다.
* 책임에 초점을 맞춰서 설계할 때 직면하는 가장 큰 어려움은 어떤 객체에게 어떤 책임을 할당할지를 결정하기가 쉽지 않다는 것이다.
* 이번 장에서 살펴볼 GRASP 패턴은 책임 할당의 어려움을 해결하기 위한 답을 제시해 줄 것이다.



## 01 책임 주도 설계를 향해

* 데이터 중심의 설계에서 책임 중심의 설계로 전환하기 위해서는 다음의 두 가지 원칙을 따라야 한다.

    * 데이터보다 행동을 먼저 결정하라.
    * 협력이라는 문맥 안에서 책임을 결정하라.

    

### 데이터보다 행동을 먼저 결정하라

* 객체에게 중요한 것은 데이터가 아니라 외부에 제공하는 행동이다.
    * 클라이언트의 관점에서 객체가 수행하는 행동이란 곧 객체의 책임을 의미한다.
* 데이터는 객체가 책임을 수행하는 데 필요한 재료를 제공할 뿐이다.
    * 객체지향에 갓 입문한 사람들이 가장 많이 저지르는 실수가 바로 객체의 행동이 아니라 데이터에 초점을 맞추는 것이다.
* 우리에게 필요한 것은 객체의 데이터에서 행동으로 무게 중심을 옮기기 위한 기법이다.
    * 가장 기본적인 해결 방법은 객체를 설계하기 위한 질문의 순서를 바꾸는 것이다.
    * 데이터 중심 설계에서는 "이 객체가 포함해야 하는 데이터가 무엇인가"를 결정한 후에 "데이터를 처리하는 데 필요한 오퍼레이션은 무엇인가"를 결정한다.
    * 책임 중심 설계에서는 "이 객체가 수행해야 하는 책임은 무엇인가"를 결정한 후에 "이 책임을 수행하는 데 필요한 데이터는 무엇인가"를 결정한다.
* 그렇다면 객체에게 어떤 책임을 할당해야 하는가?
    * 해결의 실마리를 협력에서 찾을 수 있다.



### 협력이라는 문맥 안에서 책임을 결정하라

* 객체에게 할당된 책임의 품질은 협력에 적합한 정도로 결정된다.
* 이 사실은 객체의 책임을 어떻게 식별해야 하는가에 대한 힌트를 제공한다.
    * 협력을 시작하는 주체는 메시지 전송자이기 때문에 협력에 적합한 행동이란 메시지 수신자가 아니라 메시지 전송자에게 적합한 책임을 의미한다.
    * 다시 말해서 메시지를 전송하는 클라이언트의 의도에 적합한 책임을 할당해야 한다는 것이다.
* 협력에 적합한 책임을 수확하기 위해서는 객체를 결정한 후에 메시지를 선택하는 것이 아니라 메시지를 결정한 후에 객체를 선택해야 한다.
    * 메시지가 존재하기 때문에 그 메시지를 처리할 객체가 필요한 것이다.
    * 객체가 메시지를 선택하는 것이 아니라 메시지가 객체를 선택하게 해야 한다.
* 메시지가 클라이언트의 의도를 표현한다는 사실에 주목하라.
    * 객체를 결정하기 전에 객체가 수신할 메시지를 먼저 결정한다.
    * 클라이언트는 어떤 객체가 메시지를 수신할지 알지 못한다.
    * 클라이언트는 단지 메시지를 수신할 것이라는 사실을 믿고 자신의 의도를 표현한 메시지를 전송할 뿐이다.
* 메시지를 먼저 결정하기 때문에 메시지 송신자는 메시지 수신자에 대한 어떠한 가정도 할 수 없다.
    * 메시지 전송자의 관점에서 메시지 수신자가 깔끔하게 캡슐화되는 것이다.
* 정리해보자.
    * 객체에게 적절한 책임을 할당하기 위해서는 협력이라는 문맥을 고려해야 한다.
    * 협력이라는 문맥에서 적절한 책임이란 곧 클라이언트의 관점에서 적절한 책임을 의미한다.
    * 올바른 객체지향 설계는 클라이언트가 전송할 메시지를 결정한 후에야 비로소 객체의 상태를 저장하는 데 필요한 내부 데이터에 관해 고민하기 시작한다.



### 책임 주도 설계

* 책임 주도 설계의 핵심은 책임을 결정한 후에 책임을 수행할 객체를 결정하는 것이다.
* 협력에 참여하는 객체들의 <u>책임이 어느 정도 정리될 때까지는 객체의 내부 상태에 관심을 가지지 않는 것이다.</u>



## 02 책임 할당을 위한 GRASP 패턴

* 크레이그 라만(Craig Larman)이 패턴 형식으로 제안한 GRASP 패턴

* GRASP은 "General Responsibility Assignment Software Pattern"의 약자로 객체에게 책임을 할당할 때 지침으로 삼을 수 있는 원칙들의 집합을 패턴 형식으로 정리한 것이다.



### 도메인 개념에서 출발하기

* 설계를 시작하기 전에 도메인에 대한 개략적인 모습을 그려보는 것이 유용하다.
* 설계를 시작하는 단계에서는 개념들의 의미와 관계가 정확하거나 완벽할 필요가 없다.



### 정보 전문가에게 책임을 할당하라

* 책임 주도 설계 방식의 첫 단계는 애플리케이션이 제공해야 하는 기능을 애플리케이션의 책임으로 생각하는 것이다.
    * 이 책임을 애플리케이션에 대해 전송된 메시지로 간주하고 이 메시지를 책임질 첫 번째 객체를 선택하는 것으로 설계를 시작한다.
* 사용자에게 제공해야 하는 기능은 영화를 예매하는 것이다.
    * 이를 책임으로 간주하면 애플리케이션은 영화를 예매할 책임이 있다고 말할 수 있다.
    * 이 책임을 수행하는 데 필요한 메시지를 결정해야 한다.
* 따라서 첫 번째 질문은 다음과 같다.
    * 메시지를 전송할 객체는 무엇을 원하는가?
        * 1: 예매하라
    * 메시지를 수신할 적합한 객체는 누구인가?
* 이 질문에 답하기 위해서는 객체가 상태와 행동을 통합한 캡슐화의 단위라는 사실에 집중해야 한다.
    * 객체의 책임과 책임을 수행하는 데 필요한 상태는 동일한 객체 안에 존재해야 한다.
    * 따라서 객체에게 책임을 할당하는 첫 번째 원칙은 책임을 수행할 정보를 알고 있는 객체에게 책임을 할당하는 것이다.
    * GRASP에서는 이를 **INFORMATION EXPERT(정보 전문가)** 패턴이라고 부른다.
* INFORMATION EXPERT 패턴은 객체가 자신이 소유하고 있는 정보와 관련된 작업을 수행한다는 일반적인 직관을 표현한 것이다.
    * 여기서 이야기하는 정보는 데이터와 다르다.
    * 책임을 수행하는 객체가 정보를 알고 있다고 해서 그 정보를 저장하고 있을 필요는 없다.
    * 해당 정보를 제공할 수 있는 다른 객체를 알고 있거나 필요한 정보를 계산해서 제공할 수도 있다.
    * 어떤 방식이건 정보 전문가가 데이터를 반드시 저장하고 있을 필요는 없다는 사실을 이해하는 것이 중요하다.

* INFORMATION EXPERT 패턴에 따르면 예매하는 데 필요한 정보를 가장 많이 알고 있는 객체에게 예매하라 메시지를 처리할 책임을 할당해야 한다.
* 아마 '상영'이라는 도메인 개념이 적합할 것이다.
* 예매하라 메시지를 완료하기 위해서는 예매 가격을 계산하는 작업이 필요하다.
    * 안타깝게도 Screening은 가격을 계산하는 데 필요한 정보를 모르기 때문에 외부의 객체에게 도움을 요청해서 가격을 얻어야 한다.
* 영화 가격을 계산하는 데 필요한 정보를 알고 있는 전문가는 무엇인가? 당연히 영화다.
    * 영화가 스스로 처리할 수 없는 일이 한 가지 있다.
    * 할인 조건에 따라 영화가 할인 가능한지를 판단하는 것이다.
    * 따라서 Movie는 할인 여부를 판단하라 메시지를 전송해서 외부의 도움을 요청해야 한다.
* 할인 여부를 판단하는 데 필요한 정보를 가장 많이 알고 있는 객체는 무엇인가?
    * 이 정보에 대한 전문가는 바로 할인 조건(DiscountCondition)이다.
* 지금까지 살펴본 것처럼 INFORMATION EXPERT 패턴은 객체에게 책임을 할당할 때 가장 기본이 되는 책임 할당 원칙이다.



### 높은 응집도와 낮은 결합도

* 방금 전에 설계한 영화 예매 시스템에서는 할인 요금을 계산하기 위해 Movie가 DiscountCondition에 할인 여부를 판단하라 메시지를 전송한다.
    * 그렇다면 Movie 대신 Screening이 직접 DiscountCondition과 협력하게 하는 것은 어떨까?
    * 위 설계는 기능적인 측면에서만 놓고 보면 Movie와 DiscountCondition이 직접 상호작용하는 앞의 설계와 동일하다.
    * 그렇다면 왜 우리는 이 설계 대신 Movie가 DiscountCondition과 협력하는 방법을 선택한 것일까?
* 그 이유는 응집도와 결합도에 있다.
    * 책임을 할당할 수 있는 다양한 대안들이 존재한다면 응집도와 결합도 측면에서 더 나은 대안을 선택하는 것이 좋다.
* GRASP에서는 이를 LOW COUPLING(낮은 결합도) 패턴과 HIGH COHESION(높은 응집도) 패턴이라고 부른다.

> **LOW COUPLING 패턴**
>
> 어떻게 하면 의존성을 낮추고 변화의 영향을 줄이며 재사용성을 증가시킬 수 있을까? 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라.
>
> 낮은 결합도는 모든 설계 결정에서 염두에 둬야 하는 원리다. 다시 말해 설계 결정을 평가할 때 적용할 수 있는 평가 원리다.

* DiscountCondition이 Movie와 협력하는 것이 좋을까, 아니면 Screening과 협력하는 것이 좋을까?
    * 해답의 실마리는 결합도에 있다.
    * 도메인 상으로 Movie는 DiscountCondition의 목록을 속성으로 포함하고 있다.
    * Movie와 DiscountCondition은 이미 결합돼 있기 때문에 Movie를 DiscountCondition과 협력하게 하면 설계 전체적으로 결합도를 추가하지 않고도 협력을 완성할 수있다.
    * 하지만 Screening이 DiscountCondition과 협력하는 경우에는 Screening과 DiscountCondition 사이에 새로운 결합도가 추가된다. 
    * 따라서 LOW COUPLING 패턴의 관점에서는 Screening이 DiscountCondition과 협력하는 것보다는 Movie가 DiscountCondition과 협력하는 것이 더 나은 설계 대안인 것이다.
* HIGH COHESION 패턴의 관점에서도 설계 대안들을 평가할 수 있다.

> **HIGH COHESION** 패턴
>
> 어떻게 복잡성을 관리할 수 있는 수준으로 유지할 것인가? 높은 응집도를 유지할 수 있게 책임을 할당하라.
>
> 설계 결정을 평가할 때 적용할 수 있는 평가 원리다. 현재의 책임 할당을 검토하고 있거나 여러 설계 대안 중 하나를 선택해야 한다면 높은 응집도를 유지할 수 잇는 설계를 선택하라.

* Screening의 가장 중요한 책임은 예매를 생성하는 것이다.
* 만약 Screening이 DiscountCondition과 협력해야 한다면 Screening은 영화 요금 계산에 관련된 책임 일부를 떠안아야 할 것이다.
* 다시 말해서 예매 요금을 계산하는 방식이 변경될 경우 Screening도 함께 변경해야 하는 것이다.
    * 결과적으로 Screening과 DiscountCondition이 협력하게 되면 Screening은 서로 다른 이유로 변경되는 책임을 짊어지게 되고 응집도가 낮아질 수밖에 없다.



### 창조자에게 객체 생성 책임을 할당하라

* 영화 예매 협력의 최종 결과물은 Reservation 인스턴스를 생성하는 것이다.
    * 이것은 협력에 참여하는 어떤 객체에게는 Reservation 인스턴스를 생성할 책임을 할당해야 한다는 것을 의미한다.
    * **CREATOR(창조자)** 패턴은 객체를 생성할 책임을 어떤 객체에게 할당할지에 대한 지침을 제공한다.

> **CREATOR 패턴**
>
> 객체 A를 생성해야 할 때 어떤 객체에게 객체 생성 책임을 할당해야 하는가? 아래 조건을 최대한 많이 만족하는 B에게 객체 생성책임을 할당하라.
>
> * B가 A 객체를 포함하거나 참조한다.
> * B가 A 객체를 기록한다.
> * B가 A 객체를 긴밀하게 사용한다.
> * B가 A 객체를 초기화하는 데 필요한 데이터를 가지고 있다.

* Screening은 예매 정보를 생성하는 데 필요한 영화, 상영 시간, 상영 순번 등의 정보에 대한 전문가이며, 예매 요금을 계산하는 데 필수적인 Movie도 알고 있다.
* Screening을 Reservation의 CREATOR로 선택하는 것이 적절해 보인다.



## 03 구현을 통한 검증

* Screening은 영화를 예매할 책임을 맡으며 그 결과로 Reservation 인스턴스를 생성할 책임을 수행해야 한다.
* 다시 말해 Screening은 예매에 대한 정보 전문가인 동시에 Reservation의 창조자다.

```java
public class Screening {
  public Reservation reserve(Customer customer, int audienceCount) {
    
  }
}
```

* 책임이 결정됐으므로 책임을 수행하는 데 필요한 인스턴스 변수를 결정해야 한다.
* Screening은 상영시간(whenScreened)과 상영 순번(sequence)을 인스턴스 변수로 포함한다.
* 또한 Movie에 가격을 계산하라 메시지를 전송해야 하기 때문에 영화(movie)에 대한 참조도 포함해야 한다.

```java
public class Screening {
  private Movie movie;
  private int sequence;
  private LocalDateTime whenScreened;
  
  public Reservation reserve(Customer customer, int audienceCount) {
  }
}
```

* 영화를 예매하기 위해서는 movie에게 가격을 계산하라 메시지를 전송해서 계산된 영화 요금을 반환받아야 한다.
* calculateFee 메서드는 이렇게 반환된 요금에 예매 인원 수를 곱해서 전체 예매 요금을 계산한 후 Reservation을 생성해서 반환한다.

```java
public class Screening {
  private Movie movie;
  private int sequence;
  private LocalDateTime whenScreened;
  
  public Reservation reserve(Customer customer, int audienceCount) {
    new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
  }
  
  private Money calculateFee(int audienceCount) {
    return movie.calculateMovieFee(this).times(audienceCount);
  }
}
```

* Screening이 Movie의 내부 구현에 대해 어떤 지식도 없이 전송할 메시지를 결정했다.
* Screening과 Movie를 연결하는 유일한 연결 고리는 메시지 뿐이다.
* 따라서 메시지가 변경되지 않는 한 Movie의 어떤 수정을 가하더라도 Screening에는 영향을 미치지 않는다.



* Screening은 Movie와 협력하기 위해 calculateMovieFee 메시지를 전송한다.
* Movie는 이 메시지에 응답하기 위해 calculateMovieFee 메서드를 구현해야 한다.

```java
public class Movie {
  public Money calculateMovieFee(Screening screening)
}
```

* 요금을 계산하기 위해 Movie는 기본 금액(fee), 할인 조건(discountCondition), 할인 정책 등의 정보를 알아야 한다.
* 현재의 설계에서는 할인 정책을 구성하는 할인 금액(discountAmount)과 할인 비율(discountPercent)을 Movie의 인스턴스 변수로 선언했다.
* 그리고 현재의 Movie가 어떤 할인 정책이 적용된 영화인지를 나타내기 위해 영화 종류를 인스턴스 변수로 포함한다.

```java
public class Movie {
  private String title;
  private Duration runnintTime;
  private Money fee;
  private List<DiscountCondition> discountConditions;
  
  private MovieType movieType;
  private Money discountAmount;
  private Money discountPercent;
  
  public Money calculateMovieFee(Screening screening)
}
```

* MovieType은 할인 정책의 종류를 나열하는 단순한 열거형 타입이다.

```java
public enum MovieType {
  AMOUNT_DISCOUNT, // 금액 할인 정책
  PERCENT_DISCOUNT, // 비율 할인 정책
  NONE_DISCOUNT // 미적용
}
```

```java
public class Movie {
  public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
            .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }
}
```

* 실제로 할인 요금을 계산하는 calculateDiscountAmount 메서드는 movieType의 값에 따라 적절한 메서드를 호출한다.

```java
public class Movie {
  private Money calculateDiscountAmount() {
        switch (movieType) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount();
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount();
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount();
        }

        throw new IllegalArgumentException();
    }

    private Money calculateAmountDiscountAmount() {
        return discountAmount;
    }

    private Money calculatePercentDiscountAmount() {
        return fee.times(discountPercent);
    }

    private Money calculateNoneDiscountAmount() {
        return Money.ZERO;
    }
}
```

* Movie는 각 DiscountCondition에 할인 여부를 판단하라 메시지를 전송한다.
* DiscountCondition은 이 메시지를 처리하기 위해 isSatisfiedBy 메서드를 구현해야 한다.

```java
public class DiscountCondition {
  public boolean isSatisfiedBy(Screening screen) {
    
  }
}
```

```java
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean isSatisfiedBy(Screening screening) {
        if (type == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(screening);
        }

        return isSatisfiedBySequence(screening);
    }

    private boolean isSatisfiedByPeriod(Screening screening) {
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
            startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
            endTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0;
    }

    private boolean isSatisfiedBySequence(Screening screening) {
        return sequence == screening.getSequence();
    }
}
```

* DiscountCondition은 할인 조건을 판단하기 위해 Screening의 상영 시간과 상영 순번을 알아야 한다.

```java
public class Screening {
  public LocalDateTime getWhenScreened() {
        return this.whenScreened;
    }

    public int getSequence() {
        return sequence;
    }
}
```

* DiscountConditionType은 할인 조건의 종류를 나열하는 단순한 열거형 타입이다.

```java
public enum DiscountConditionType {
    SEQUENCE,
    PERIOD
}
```



### DiscountCondition 개선하기

* 가장 큰 문제는 변경에 취약한 클래스를 포함하고 있다는 것이다.
* DiscountCondition은 다음과 같이 서로 다른 세 가지 이유로 변경될 수 있다.
    * 새로운 할인 조건 추가
        isSatisfiedBy 메서드 안의 if ~ else 구문을 수정해야 한다.
    * 순번 조건을 판단하는 로직 변경
        isSatisfiedBySequence 메서드의 내부 구현을 수정해야 한다.
    * 기간 조건을 판단하는 로직이 변경되는 경우
        isSatisfiedByPeriod 메서드의 내부 구현을 수정해야 한다.
* DiscountCondition은 하나 이상의 변경 이유를 가지기 때문에 응집도가 낮다.
    * 응집도가 낮다는 것은 서로 연관성이 없는 기능이나 데이터가 하나의 클래스 안에 뭉쳐져 있다는 것을 의미한다.
    * 따라서 낮은 응집도가 초래하는 문제를 해결하기 위해서는 변경의 이유에 따라 클래스를 분리해야 한다.
* 앞에서 살펴본 것처럼 DiscountCondition 안에 구현된 isSatisifiedBySequence 메서드와 isSatisifiedByPeriod 메서드는 서로 다른 이유로 변경된다.
* 두 가지 변경이 코드에 영향을 미치는 시점은 서로 다를 수있다.
    * DiscountCondition은 서로 다른 이유로, 서로 다른 시점에 변경될 확률이 높다.
    * 서로 다른 이유로 변경되는 두 개의 메서드를 가지는 DiscountCondition 클래스의 응집도는 낮아질 수밖에 없는 것이다.
* 지금까지 살펴본 것처럼 일반적으로 설계를 개선하는 작업은 변경의 이유가 하나 이상인 클래스를 찾는 것으로부터 시작하는 것이 좋다.
    * 변경의 이유가 하나 이상인 클래스에는 위험 징후를 또렷하게 드러내는 몇 가지 패턴이 존재한다는 점이다.
* 코드를 통해 변경의 이유를 파악할 수 있는 첫 번째 방법은 **인스턴스 변수가 초기화되는 시점**을 살펴보는 것이다.
    * 응집도가 높은 클래스는 인스턴스를 생성할 때 모든 속성을 함께 초기화한다.
    * 반면 응집도가 낮은 클래스는 객체의 속성 중 일부만 초기화하고 일부는 초기화되지 않은 상태로 남겨진다.
    * 따라서 **함께 초기화되는 속성을 기준으로 코드를 분리해야 한다.**
* 코드를 통해 변경의 이유를 파악할 수 있는 두 번째 방법은 **메서드들이 인스턴스 변수를 사용하는 방식**을 살펴보는 것이다.
    * 모든 메서드가 객체의 모든 속성을 사용한다면 클래스의 응집도는 높다고 볼 수 있다.
    * 메서들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도는 낮다고 볼 수 있다.
    * **속성 그룹과 해당 그룹에 접근하는 메서드 그룹을 기준으로 코드를 분리해야 한다.**

> **클래스 응집도 판단하기**
>
> 다음과 같은 징후로 몸살을 앓고 있다면 클래스의 응집도는 낮은 것이다.
>
> * 클래스가 하나 이상의 이유로 변경돼야 한다면 응집도가 낮은 것이다. 변경의 이유를 기준으로 클래스를 분리하라.
> * 클래스의 인스턴스를 초기화하는 시점에 경우에 따라 서로 다른 속성들을 초기화하고 있다면 응집도가 낮은 것이다. 초기화되는 속성의 그룹을 기준으로 클래스를 분리하라.
> * 메서드 그룹이 속성 그룹을 사용하는지 여부로 나뉜다면 응집도가 낮은 것이다. 이들 그룹을 기준으로 클래스를 분리하라.



### 타입 분리하기

* DiscountCondition의 가장 큰 문제는 순번 조건과 기간 조건이라는 두 개의 독립적인 타입이 하나의 클래스 안에 공존하고 있다는 점이다.
* 가장 먼저 떠오르는 해결 방법은 두 타입을 SequenceCondition과 PeriodCondition이라는 두 개의 클래스로 분리하는 것이다.

```java
public class PeriodCondition {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isSatisfiedBy(Screening screening) {
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
            startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
            endTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0;
    }
}
```

SequenceCondition은 하나의 인스턴스 변수만을 포함하는 간단한 클래스로 분리될 수 있다.

```java
public class SequenceCondition {

    private int sequence;

    public SequenceCondition(int sequence) {
        this.sequence = sequence;
    }

    public boolean isSatisfiedBy(Screening screening) {
        return screening.getSequence() == sequence;
    }
}
```

* 클래스를 분리하면 앞에서 언급했던 문제점들이 모두 해결된다.
* 클래스를 분리함으로써 코드의 품질을 높이는 데 성공한 것이다.
* 하지만 안타깝게도 클래스를 분리한 후에 새로운 문제가 나타났다.
    * 수정 전에는 Movie와 협력하는 클래스는 DiscountCondition 하나뿐이었다.
    * 그러나 수정 후에 Movie의 인스턴스는 SequenceCondition과 PeriodCondition이라는 두 개의 서로 다른 클래스의 인스턴스 모두와 협력할 수 있어야 한다.
* 이 문제를 해결하기 위해 생각할 수 있는 첫 번째 방법은 Movie 클래스 안에서 SequenceCondition의 목록과 PeriodCondition의 목록을 따로 유지하는 것이다.

```java
public class Movie {
    private List<PeriodCondition> periodConditions;
    private List<SequenceCondition> sequenceConditions;
  
    private boolean isDiscountable(Screening screening) {
        return checkPeriodCondition(screening) || checkSequenceCondition(screening);
    }

    private boolean checkPeriodCondition(Screening screening) {
        return periodConditions.stream().anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    private boolean checkSequenceCondition(Screening screening) {
        return periodConditions.stream().anyMatch(condition -> condition.isSatisfiedBy(screening));
    }
}
```

* 하지만 이 방법은 새로운 문제를 야기한다.
    * 첫 번째 문제는 Movie 클래스가 PeriodCondition과 SequenceCondition 클래스 양쪽 모두에게 결합된다는 것이다.
    * 두 번째 문제는 수정 후에 새로운 할인 조건을 추가하기가 더 어려워졌다는 것이다.
* 클래스를 분리하기 전에는 DiscountCondition의 내부 구현만 수정하면 Movie에는 아무런 영향도 미치지 않았다.
* 하지만 수정 후에는 할인 조건을 추가하려면 Movie도 함께 수정해야 한다.



### 다형성을 통해 분리하기

* Movie 입장에서 보면 SequenceCondition과 PeriodCondition은 아무 차이도 없다.
    * 둘 모두 할인 여부를 판단하는 동일한 책임을 수행하고 있을 뿐이다.
* 이 시점이 되면 자연스럽게 역할의 개념이 무대 위로 등장한다.
    * Movie 입장에서 SequenceCondition과 PeriodCondition이 동일한 책임을 수행한다는 것은 동일한 역할을 수행한다는 것을 의미한다.
* 역할을 사용하면 객체의 구체적인 타입을 추상화할 수 있다.
    * 역할을 대체할 클래스들 사이에서 구현을 공유해야 할 필요가 있다면 추상 클래스를 사용하면 된다.
    * 구현을 공유할 필요 없이 역할을 대체하는 객체들의 책임만 정의하고 싶다면 인터페이스를 사용하면 된다.

```java
public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
```

```java
public class PeriodCondition implements DiscountCondition {
  ...
}

public class SequenceCondition implements DiscountCondition {
  ...
}
```

* 이제 Movie는 협력하는 객체의 구체적인 타입을 몰라도 상관없다.
* 협력하는 객체가 DiscountCondition 역할을 수행하고 있고 isSatisfiedBy 메시지를 이해할 수 있다는 사실만 알고 있어도 충분하다.

```java
public class Movie {
  private List<DiscountCondition> discountConditions;
  
  public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }
}
```

* DiscountCondition의 경우에서 알 수 있듯이 객체의 암시적인 타입에 따라 행동을 분기해야 한다면 암시적인 타입을 명시적인 클래스로 정의하고 행동을 나눔으로써 응집도 문제를 해결할 수 있다.
* 다시 말해 객체의 타입에 따라 변하는 행동이 있다면 타입을 분리하고 변화하는 행동을 각 타입의 책임으로 할당하라는 것이다.
* GRASP에서는 이를 POLOMORPHISM(다형성) 패턴이라고 부른다.



### 변경으로부터 보호하기

* 새로운 할인 조건을 추가하는 경우에는 어떻게 될까?
    * DiscountCondition이라는 역할이 Movie로부터 PeriodCondition과 SequenceCondition의 존재를 감춘다는 사실에 주목하라.
    * Movie 관점에서는 DiscountCondition의 타입이 캡슐화된다는 것은 새로운 DiscountCondition 타입을 추가하더라도 Movie가 영향을 받지 않는다는 것을 의미한다.
* 이처럼 변경을 캡슐화하도록 책임을 할당하는 것을 GRASP에서는 **PROTECTED VARIATIONS(변경 보호)** 패턴이라고 부른다.
* 클래스를 변경에 따라 분리하고 인터페이스를 이용해 변경을 캡슐화하는 것은 설계의 결합도와 응집도를 향상시키는 매우 강력한 방법이다.
    * 하나의 클래스가 여러 타입의 행동을 구현하고 있는 것처럼 보인다면 클래스를 분해하고 POLYMORPHISM 패턴에 따라 책임을 분산시켜라.
    * 예측 가능한 변경으로 인해 여러 클래스들이 불안정해진다면 PROTECTED VARIATIONS 패턴에 따라 안정적인 인터페이스 뒤로 변경을 캡슐화하라.



### Movie 클래스 개선하기

* Movie 역시 DiscountCondition과 동일한 문제로 몸살을 앓고 있다.
* 금액 할인 정책 영화와 비율 할인 정책 영화라는 두 가지 타입을 하나의 클래스 안에 구현하고 있기 때문에 하나 이상의 이유로 변경될 수 있다.

```java
public abstract class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;

    private List<DiscountCondition> discountConditions;

    public Movie(String title, Duration runningTime, Money fee, DiscountCondition ... discountConditions) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountConditions = Arrays.asList(discountConditions);
    }

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    protected abstract Money calculateDiscountAmount();
}
```

* 할인 정책의 종류에 따라 할인 금액을 계산하는 로직이 달라져야 한다.
    * 이를 위해 calculateDiscountAmount 메서드를 추상 메서드로 선언함으로써 서브클래스들이 할인 금액을 계산하는 방식을 원하는대로 오버라딩할 수 있게 했다.

```java
public class AmountDiscountMovie extends Movie {
    private Money discountAmount;

    public AmountDiscountMovie(String title, Duration runningTime, Money fee, Money discountAmount, DiscountCondition ... discountConditions) {
        super(title, runningTime, fee, discountConditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return discountAmount;
    }
}
```

```java
public class PercentDiscountMovie extends Movie {

    private double percent;

    public PercentDiscountMovie(String title, Duration runningTime, Money fee, double percent, DiscountCondition ... discountConditions) {
        super(title, runningTime, fee, discountConditions);
        this.percent = percent;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return getFee().times(percent);
    }
}
```

* 할인 요금을 게산하기 위해서는 영화의 기본 금액이 필요하다.
* 이 메서드는 서브클래스에서만 사용해야 하므로 기시성을 public이 아닌 protected로 제한해야 한다.

```java
public abstract class Movie {
  protected Money getFee() {
        return fee;
    }
}
```

할인 정책을 적용하지 않기 위해서는 NoneDiscountMovie 클래스를 사용하면 된다.

```java
public class NoneDiscountMovie extends Movie {

    public NoneDiscountMovie(String title, Duration runningTime, Money fee, DiscountCondition ... discountConditions) {
        super(title, runningTime, fee, discountConditions);
    }

    @Override
    protected Money calculateDiscountAmount() {
        return Money.ZERO;
    }
}
```

* 데이터 중심 설계는 정반대의 길을 걷는다.
    * 데이터 중심의 설계는 데이터와 관련된 클래스의 내부 구현이 인터페이스에 여과 없이 노출되기 때문에 캡슐화를 지키기 어렵다.
    * 이로 인해 응집도가 낮고 결합도가 높으며 변경에 취약한 코드가 만들어질 가능성이 높다.
* 결론은 데이터가 아닌 책임을 중심으로 설계하라는 것이다.
    * 객체에게 중요한 것은 상태가 아니라 행동이다.



### 변경과 유연성

* 설계를 주도하는 것은 변경이다.
    * 개발자로서 변경에 대비할 수 있는 두 가지 방법이 있다.
    * 하나는 코드를 이해하고 수정하기 쉽도록 최대한 단순하게 설계하는 것이다.
    * 다른 하나는 코드를 수정하지 않고도 변경을 수용할 수 있도록 코드를 더 유연하게 만드는 것이다.
    * 대부분의 경우에 전자가 더 좋은 방법이지만 유사한 변경이 반복적으로 발생하고 있다면 복잡성이 상승하더라도 유연성을 추가하는 두 번째 방법이 더 좋다.
* 예를 들어, 영화에 설정된 할인 정책을 실행 중에 변경할 수 있어야 한다는 요구사항이 추가됐다고 가정해 보자.
* 새로운 할인 정책이 추가될 때마다 인스턴스를 생성하고, 상태를 복사하고, 식별자를 관리하는 코드를 추가하는 일은 번거로울뿐만 아니라 오류가 발생하기도 쉽다.
    * 이 경우 코드의 복잡성이 높아지더라도 할인 정책의 변경을 쉽게 수용할 수 있게 코드를 유연하게 만드는 것이 더 좋은 방법이다.
* 해결 방법은 상속 대신 합성을 사용하는 것이다.
    * Movie의 상속 계층 안에 구현된 할인 정책을 독립적인 DiscountPolicy로 분리한 후 Movie에 합성시키면 유연한 설계가 완성된다.
* 이제 금액 할인 정책이 적용된 영화를 비율 할인 정책으로 바꾸는 일은 Movie에 연결된 DiscountPolicy의 인스턴스를 교체하는 단순한 작업으로 바뀐다.

```java
Movie movie = new Movie("타이타닉",
                       Duration.ofMinutes(120),
                       Money.wons(10000),
                       new AmountDiscountPolicy(...));
movie.changeDiscountPolicy(new PercentDiscountMovie(...));
```

* 합성을 사용한 예제의 경우 새로운 할인 정책이 추가되더라도 할인 정책을 변경하는 데 필요한 추가적인 코드를 작성할 필요가 없다.
* 이 예는 유연성에 대한 압박이 설계에 어떤 영향을 미치는지를 잘 보여준다.
    * 실제로 유연성은 의존성 관리의 문제다.
    * 요소들 사이의 읜존성의 정도가 유연성의 정도를 결정한다.
    * 유연성의 정도에 따라 결합도를 조절할 수 있는 능력은 객체지향 개발자가 갖춰야 하는 중요한 기술 중 하나다.



## 04 책임 주도 설계의 대안

* 개인적으로 책임과 객체 사이에서 방황할 때 돌파구를 찾기 위해 선택하는 방법은 최대한 빠르게 목적한 기능을 수행하는 코드를 작성하는 것이다.
* 주로 객체지향 설계에 대한 경험이 부족한 개발자들과 페어 프로그래밍을 할 때나 설계의 실마리가 풀리지 않을 때 이런 방법을 사용하는데 생각보다 훌륭한 설계를 얻게 되는 경우가 종종 있다.
* 주의할 점은 코드를 수정한 후에 겉으로 드러나는 동작이 바뀌어서는 안 된다는 것이다. 캡슐화를 향상시키고, 응집도를 높이고, 결합도를 낮춰야 하지만 동작은 그대로 유지해야 한다.
* 이처럼 이해하기 쉽고 수정하기 쉬운 소프트웨어로 개선하기 위해 겉으로 보이는 동작은 바꾸지 않은 채 내부 구조를 변경하는 것을 **리팩터링(refactoring)**이라고 부른다.



### 메서드 응집도

* 영화 예매를 처리하는 모든 절차는 ReservationAgency에 집중돼 있었다.

```java
public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Movie movie = screening.getMovie();

        boolean discountable = false;
        for (DiscountCondition condition: movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek())
                        && condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0
                        && condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }

        Money fee;
        if (discountable) {
            Money discountAmount = Money.ZERO;
            switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }

            fee = movie.getFee().minus(discountAmount).times(audienceCount);
        } else {
            fee = movie.getFee();
        }

        return new Reservation(customer, screening, fee, audienceCount);
    }
}
```

* reserve 메서드는 너무 길고 이해하기도 어렵다.
* 긴 메서드는 다양한 측면에서 코드의 유지보수에 부정적인 영향을 미친다.
    * 어떤 일을 수행하는지 한눈에 파악하기 어렵기 때문에 코드를 전체적으로 이해하는 데 너무 많은 시간이 걸린다.
    * 하나의 메서드 안에서 너무 많은 작업을 처리하기 때문에 변경이 필요할 때 수정해야 할 부분을 찾기 어렵다.
    * 메서드 내부의 일부 로직만 수정하더라도 메서드의 나머지 부분에서 버그가 발생할 확률이 높다.
    * 로직의 일부만 재사용하는 것이 불가능하다.
    * 코드를 재사용하는 유일한 방법은 원하는 코드를 복사해서 붙여는 것뿐이므로 코드 중복을 초래하기 쉽다.
* 한마디로 말해서 긴 메서드는 응집도가 낮기 때문에 이해하기도 어렵고 재사용하기도 어려우며 변경하기도 어렵다.
    * 마이클 페더스는 이런 메서드를 몬스터 메서드(monster method)라고 부른다.
* 응집도가 낮은 메서드는 로직의 흐름을 이해하기 위해 주석이 필요한 경우가 대부분이다.
    * 메서드가 명령문들의 그룹으로 구성되고 각 그룹에 주석을 달아야 할 필요가 있다면 그 메서드의 응집도는 낮은 것이다.
    * 주석을 추가하는 대신 메서드를 작게 분해해서 각 메서드의 응집도를 높여라.
* 클래스의 응집도와 마찬가지로 메서드의 응집도를 높이는 이유는 변경과 관련이 깊다.
    * 응집도 높은 메서드는 변경되는 이유가 단 하나여야 한다.
* 객체로 책임을 분배할 때 가장 먼저 할 일은 메서드를 응집도 있는 수준으로 분해하는 것이다. 
* 긴 메서드를 작고 응집도 높은 메서드로 분리하면 각 메서드를 적절한 클래스로 이동하기가 더 수월해지기 때문이다.

다음은 ReservationAgency를 응집도 높은 메서드들로 잘게 분해한 것이다.

```java
public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer,
                               int audienceCount) {
        boolean discountable = checkDiscountable(screening);
        Money fee = calculateFee(screening, discountable, audienceCount);
        return createReservation(screening, customer, audienceCount, fee);
    }

    private boolean checkDiscountable(Screening screening) {
        return screening.getMovie().getDiscountConditions().stream()
            .anyMatch(condition -> condition.isDiscountable(screening));
    }

    private Money calculateFee(Screening screening, boolean discountable,
                               int audienceCount) {
        if (discountable) {
            return screening.getMovie().getFee()
                .minus(calculateDiscountedFee(screening.getMovie()))
                .times(audienceCount);
        }

        return  screening.getMovie().getFee();
    }

    private Money calculateDiscountedFee(Movie movie) {
        switch(movie.getMovieType()) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountedFee(movie);
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountedFee(movie);
            case NONE_DISCOUNT:
                return calculateNoneDiscountedFee(movie);
        }

        throw new IllegalArgumentException();
    }

    private Money calculateAmountDiscountedFee(Movie movie) {
        return movie.getDiscountAmount();
    }

    private Money calculatePercentDiscountedFee(Movie movie) {
        return movie.getFee().times(movie.getDiscountPercent());
    }

    private Money calculateNoneDiscountedFee(Movie movie) {
        return movie.getFee();
    }

    private Reservation createReservation(Screening screening,
                                          Customer customer, int audienceCount, Money fee) {
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
```



* 일단 메서드를 분리하고 나면 public 메서드는 상위 수준의 명세를 읽는 것 같은 느낌이든다.
* 수정 후에 reserve 메서드가 어떻게 수정됐는지 보자.

```java
    public Reservation reserve(Screening screening, Customer customer,
                               int audienceCount) {
        boolean discountable = checkDiscountable(screening);
        Money fee = calculateFee(screening, discountable, audienceCount);
        return createReservation(screening, customer, audienceCount, fee);
    }
```

* 수정 전과 수정 후의 차이를 느낄 수 있겠는가?
    * 수정 전에는 메서드를 처음부터 끝까지 읽어봐도 목적을 알기 어려웠지만 수정 후에는 메서드가 어떤 일을 하는지를 한눈에 알아볼 수 있다.
* 코드를 작은 메서드들로 분해하면 전체적인 흐름을 이해하기도 쉬워진다.
    * 동시에 너무 많은 세부사항을 기억하도록 강요하는 코드는 이해하기도 어렵다.
* 수정 후의 코드는 변경하기도 더 쉽다.
    * 각 메서드는 단 하나의 이유에 의해 변경된다.
* 작고, 명확하며, 한 가지 일에 집중하는 응집도 높은 메서드는 변경 가능한 설계를 이끌어 내는 기반이 된다.
* 안타깝게도 메서드들의 응집도 자체는 높아졌지만 이 메서드들을 담고 있는 ReservationAgency의 응집도는 여전히 낮다.



### 객체를 자율적으로 만들자

* 어떤 메서드를 어떤 클래스로 이동시켜야 할까?
    * 객체가 자율적인 존재여야 한다는 사실을 떠올리면 쉽게 답할 수 있을 것이다.
    * 자신이 소유하고 있는 데이터를 자기 스스로 처리하도록 만드는 것이 자율적인 객체를 만드는 지름길이다.
    * 따라서 메서드가 사용하는 데이터를 저장하고 있는 클래스로 메서드를 이동시키면 된다.
* 어떤 데이터를 사용하는지를 가장 쉽게 알 수 있는 방법은 메서드 안에서 어떤 클래스의 접근자 메서드를 사용하는지 파악하는 것이다.

```java
public class ReservationAgency {
  private boolean isDiscountable(DiscountCondition condition, Screening screening) {
    if (condition.getType == DiscountConditionType.PERIOD) {
      return isSatisfiedByPeriod(condition, screening);
    }
    
    return isSatisifiedBySequence(condition, screening);
  }
  
  private boolean isSatisfiedByPeriod(DiscountCondition condition, Screening screening) {
    return screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) & 
      condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
      condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
  }
  
  private boolean isSatisfiedBySequence(DiscountCondition condition, Screening screening) {
    return condition.getSequence() == screening.getSequence();
  }
}
```

* ReservationAgency의 isDiscountable 메서드는 DiscountCondition의 getType 메서드를 호출해서 할인 조건의 타입을 알아낸 후 타입에 따라 isSatisfiedBySequence 메서드나 isSatisfiedByPeriod 메서드를 호출한다.
* 두 메서드를 데이터가 존재하는 DiscountCondition으로 이동하고 ReservationAgency에서 삭제하자.

```java
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

  public boolean isDiscountable(Screening screening) {
        if (type == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(screening);
        }

        return isSatisfiedBySequence(screening);
    }

    private boolean isSatisfiedByPeriod(Screening screening) {
        return screening.getWhenScreened().getDayOfWeek().equals(dayOfWeek) &&
            startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
            endTime.compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
    }

    private boolean isSatisfiedBySequence(Screening screening) {
        return sequence == screening.getSequence();
    }
}
```

* 이제 ReservationAgency는 할인 여부를 판단하기 위해 DiscountCondition의 isDiscountable 메서드를 호출하도록 변경된다.

```java
public class ReservationAgency {
    private boolean checkDiscountable(Screening screening) {
        return screening.getMovie().getDiscountConditions().stream()
            .anyMatch(condition -> condition.isDiscountable(screening));
    }
}
```

* 변경 후의 코드는 책임 주도 설계 방법을 적용해서 구현했던 DiscountCondition 클래스의 초기 모습과 유사해졌다는 사실을 알 수 있다.
    * 여기서 POLYMORPHISM 패턴과 PROTECTED VARIATIONS 패턴을 차례로 적용하면 우리의 최종 설계와 유사한 모스브이 코드를 얻게 될 것이다.
* 여기서 하고 싶은 말은 책임 주도 설계 방법에 익숙하지 않다면 일단 데이터 중심으로 구현한 후 이를 리팩터링하더라도 유사한 결과를 얻을 수 있다는 것이다.
    * 처음부터 책임 주도 설계 방법을 따르는 것보다 동작하는 코드를 작성한 후에 리팩터링 하는 것이 더 훌륭한 결과물을 낳을 수도 있다.



