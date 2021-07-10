# 6장 메시지와 인터페이스

* 객체지향 애플리케이션의 가장 중욯나 재료는 클래스가 아니라 객체들이 주고 받는 메시지다.
* 애플리케이션은 클래스로 구성되지만 메시지를 통해 정의된다는 사실을 기억하라.
* 객체가 수신하는 메시지들이 객체의 퍼블릭 인터페이스를 구성한다.
    * 훌륭한 퍼블릭 인터페이스를 얻기 위해서는 책임 주도 설계 방법을 따르는 것만으로는 부족하다.



## 01 협력과 메시지

### 클라이언트-서버 모델

* 협력은 어떤 객체가 다른 객체에게 무언가를 요청할 때 시작된다.
    * 메시지는 객체 사이의 협력을 가능하게 하는 매개체다.
    * 객체가 다른 객체에게 접근할 수 있는 유일한 방법은 메시지를 전송하는 것뿐이다.
    * 객체는 자신의 희망을 메시지라는 형태로 전송하고 메시지를 수신한 객체는 요청을 적절히 처리한 후 응답한다.
* 두 객체 사이의 협력 관계를 설명하기 위해 사용하는 전통적인 메타포는 **클라이언트-서버(Client-Server) 모델**이다.
    * Screening은 클라이언트의 역할을 수행하고 Movie는 서버의 역할을 수행한다.
    * Screening은 가격을 계산하라 메시지를 전송하고 Movie는 가격을 계산하는 서비스를 제공함으로써 메시지에 응답한다.
* Movie는 할인 요금 계산하라 메시지를 DiscountPolicy의 인스턴스에 전송해서 할인 요금을 반환받는다.



### 메시지와 메시지 전송

* **메시지(message)**는 객체들이 협력하기 위해 사용할 수 있는 유일한 의사소통 수단이다.
    * 한 객체가 다른 객체에게 도움을 요청하는 것을 **메시지 전송(message sending)** 또는 **메시지 패싱(message passing)**이라고 부른다.
    * 이때 메시지를 전송하는 객체를 **메시지 전송자(message sender)**라고 부르고 메시지를 수신하는 객체를 **메시지 수신자(message receiver)**라고 부른다.
    * 메시지는 **오퍼레이션명(operation name)**과 **인자(argument)**로 구성되며 메시지 전송은 여기에 메시지 수신자를 추가한 것이다.
    * isSatisfiedBy(screening)이 '메시지'이고, 여기에 메시지 수신자인 condition을 추가한 condition.isSatisfiedBy(screening)이 '메시지 전송'이다.



### 메시지와 메서드

* 메시지를 수신했을 때 실제로 어떤 코드가 실행되는지는 메시지 수신자의 실제 타입이 무엇인가에 달려있다.
    * condition.isSatisfiedBy(screening)이라는 메시지 전송 구문에서 메시지 수신자인 condition은 DiscountCondition이라는 인터페이스 타입으로 정의되어 있지만 실제로 실행되는 코드는 인터페이스를 실체화한 클래스의 종류에 따라 달라진다.
* 이처럼 메시지를 실제로 실행되는 함수 또는 프로시저를 **메서드**라고 부른다.
    * 중요한 것은 코드 상에서 동일한 이름의 변수에게 동일한 메시지를 전송하더라도 객체의 타입에 따라 실행되는 메서드가 달라질 수 있다는 것이다.
    * 전통적인 방식의 개발자는 어떤 코드가 실행될지를 정확하게 알고 있는 상황에서 함수 호출이나 프로시저 호출 구문을 작성한다.
    * 다시 말해 코드의 의미가 컴파일 시점과 실행 시점에 동일하다는 것이다.
* 객체지향이 메시지 전송과 메서드 호출을 명확하게 구분한다는 사실이 여러분을 모호함의 덫으로 밀어넣을 수도 있다.
    * 메시지 전송을 코드 상에 표기하는 시점에는 어떤 코드가 실행될 것인지를 정확하게 알 수 없다.
* 메시지와 메서드의 구분은 메시지 전송자와 메시지 수신자가 느슨하게 결합될 수 있게 한다.
    * 메시지 전송자는 자신이 어떤 메시지를 전송해야 하는지만 알면 된다.
    * 수신자가 어떤 클래스의 인스턴스인지, 어떤 방식으로 요청을 처리하는지 모르더라도 원활한 협력이 가능하다.
    * 메시지 수신자 역시 누가 메시지를 전송하는지 알 필요가 없다. 단시 메시지가 도착했다는 사실만 알면 된다.



### 퍼블릭 인터페이스와 오퍼레이션

* 객체는 안과 밖을 구분하는 뚜렷한 경계를 가진다.
    * 외부에서 볼 때 객체의 안쪽은 검은 장막으로 가려진 미지의 영역이다.
    * 외부의 객체는 오직 객체가 공개하는 메시지를 통해서만 객체와 상호작용할 수 있다.
    * 객체가 의사소통을 위해 외부에 공개하는 메시지의 집합을 **퍼블릭 인터페이스**라고 부른다.
* 프로그래밍 언어의 관점에서 퍼블릭 인터페이스에 포함된 메시지를 **오퍼레이션(operation)**이라고 부른다.
    * 오퍼레이션은 수행 가능한 어떤 행동에 대한 추상화다.
    * 흔히 오퍼레이션이라고 부를 때는 내부의 구현 코드는 제외하고 단순히 메시지와 관련된 시그니처를 가리키는 경우가 대부분이다.
    * DiscountCondition 인터페이스에 정의된 isSatisfiedBy가 오퍼레이션이다.
* 그에 반해 메시지를 수신했을 때 실제로 실행되는 코드를 메서드라고 부른다.
    * SequenceCondition과 PeriodCondition에 정의된 각각의 isSatisfiedBy는 실제 구현을 포함하기 때문에 메서드라고 부른다.
* 퍼블릭 인터페이스와 메시지의 관점에서 보면 '메서드 호출'보다는 '오퍼레이션 호출'이라는 용어를 사용하는 것이 더 적절하다.



### 시그니처

* 오퍼레이션(또는 메서드)의 이름과 파라미터 목록을 합쳐 **시그니처(signature)**라고 부른다.
    * 오퍼레이션은 실행 코드 없이 시그니처만을 정의한 것이다.
    * 메서드는 시그니처에 구현을 더한 것이다.
* 하나의 오퍼레이션에 대해 오직 하나의 메서드만 존재하는 경우 세상은 꽤나 단순해진다.
    * 이런 경우에는 굳이 오퍼레이션과 메서드를 구분할 필요가 없다.

> 용어 정리
>
> * 메시지: 객체가 다른 객체와 협력하기 위해 사용하는 의사소통 메커니즘. 일반적으로 객체의 오퍼레이션이 실행되도록 요청하는 것을 '메시지 전송'이라고 부른다. 메시지는 협력에 참여하는 전송자와 수신자 양쪽 모두를 포함하는 개념이다.
> * 오퍼레이션: 객체가 다른 객체에게 제공하는 서비스다. 메시지가 전송자와 수신자 사이의 협력 관게를 강조하는 데 비해 오퍼레이션은 메시지를 수신하는 객체의 인터페이스를 강조한다. 다시 말해 메시지 전송자는 고려하지 않은 채 메시지 수신자의 관점만을 다룬다. 메시지 수신이란 메시지에 대응되는 객체의 오퍼레이션을 호출하는 것을 의미한다.
> * 메서드: 메시지에 응답하기 위해 실행되는 코드 블록을 메서드라고 부른다. 메서드는 오퍼레이션의 구현이다. 동일한 오퍼레이션이라고 해도 메서드는 다를 수 있다. 오퍼레이션과 메서드의 구분은 다형성의 개념과 연결된다.
> * 퍼블릭 인터페이스: 객체가 협력에 참여하기 위해 외부에서 수신할 수 있는 메시지의 묶음. 클래스의 퍼블릭 메서드들의 집합이나 메시지의 집합을 가리키는 데 사용된다. 객체를 설계할 때 가장 중요한 것은 훌륭한 퍼블릭 인터페이스를 설계하는 것이다.
> * 시그니처: 시그니처는 오퍼레이션이나 메서드의 명세를 나타낸 것으로, 이름과 인자의 목록을 포함한다. 대부분의 언어는 시그니처의 일부를 반환 타입을 포함하지 않지만 반환타입을 시그니처의 일부로 포함하는 언어도 존재한다.



## 02 인터페이스와 설계 품질

* 좋은 인터페이스는 **최소한의 인터페이스와 추상적인 인터페이스**라는 조건을 만족해야 한다.
    * 추상적인 인터페이스는 어떻게 수행하는지가 아니라 무엇을 하는지를 표현한다.
* 추상적인 인터페이스를 설계할 수 있는 가장 좋은 방법은 책임 주도 설계 방법을 따르는 것이다.
    * 책임 주도 설계 방법은 메시지를 먼저 선택함으로써 협력과는 무관한 오퍼레이션이 인터페이스에 스며드는 것을 방지한다.
    * 따라서 인터페이스는 최소의 오퍼레이션만 포함하게 된다.

* 퍼블릭 인터페이스의 품질에 영향을 미치는 원칙과 기법에 관해 살펴보겠다.
    * 디미터 법칙
    * 묻지 말고 시켜라
    * 의도를 드러내는 인터페이스
    * 명령-쿼리 법칙




### 디미터 법칙

다음 코드는 절차적인 방식의 영화 예매 시스템 코드 중에서 할인 가능 여부를 체크하는 코드를 가져온 것이다.

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
    }
}
```

* 이 코드의 가장 큰 단점은 인자로 전달된 ReservationAgency와 Screening 사이의 결합도가 너무 높기 때문에 Screening의 내부 구현을 변경할 때마다 ReservationAgency도 함께 변경된다는 것이다.
    * 문제의 원인은 ReservationAgency가 Screening 뿐만 아니라 Movie와 DiscountCondition에도 직접 접근하기 때문이다.
* 만약 Screening이 Movie를 포함하지 않도록 변경하거나 Movie가 DiscountCondition을 포함하지 않도록 변경된다면 어떻게 될까?
    * DiscountCondition이 내부에 sequence를 포함하지 않게 된다면 어떤 영향이 있을 것인가?
    * 만약 sequence의 타입이 int가 아니라 Sequence라는 이름의 클래스로 변경된다면?
* 이처럼 협력하는 객체의 내부 구조에 대한 결합으로 인해 발생하는 설계 문제를 해결하기 위해 제안된 원칙이 바로 **디미터 법칙(Law of Demeter)**이다.
    * 디미터 법칙을 간단하게 요약하면 객체의 내부 구조에 강하게 결합되지 않도록 협력 경로를 제한하라는 것이다.
    * 디미터 법칙은 "낯선 자에게 말하지 말라(don't talk to stranger)" 또는 "오직 인접한 이웃하고만 말하라(only talk to your immediate neighbors)"로 요약할 수 있다.
    * 자바나 C#과 같이 '도트'를 이용해 메시지 전송을 표현하는 언어에서는 "오직 하나의 도트만 사용하라(use only one dot)"라는 말로 요약하기도 한다.

* 클래스 내부의 메서드가 아래 조건을 만족하는 인스턴스에만 메시지를 전송해야 한다.
    * this 객체
    * 메서드의 매개변수
    * this의 속성
    * this의 속성인 컬렉션 요소
    * 메서드 내에서 생성된 지역 객체

* 4장에서 결합도 문제를 해결하기 위해 수정한 ReservationAgency의 최종 코드를 보자.

```java
public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        boolean discountable = checkDiscountable(screening);
        Money fee = calculateFee(screening, discountable, audienceCount);
        return createReservation(screening, customer, audienceCount, fee);
    }
}
```

* 이 코드에서 ReservationAgency는 메서드의 인자로 전달된 Screening 인스턴스에게만 메시지를 전송한다.
    * ReservationAgency는 Screening의 내부에 대한 어떤 정보도 알지 못한다.
* 디미터 법칙을 따르면 부끄럼 타는 코드(shy code)를 작성할 수 있다.
* 다음은 디미터 법칙을 위반하는 코드의 전형적인 모습을 표현한 것이다.

```java
screening.getMovie().getDiscountConditions();
```

* 메시지 전송자가 수신자의 내부 구조에 대해 물어보고 반환받은 요소에 대해 연쇄적으로 메시지를 전송한다.
    * 흔히 이와 같은 코드를 기차 충돌(train wreck)이라고 부르는데 여러 대의 기차가 한 줄로 늘어서 충돌한 것처럼 보이기 때문이다.
* 디미터 법칙은 객체가 자기 자신을 책임지는 자율적인 존재여야 한다는 사실을 강조한다.
* 디미터 법칙은 객체의 내부 구조를 묻는 메시지가 아니라 수신자에게 무언가를 시키는 메시지가 더 좋은 메시지라고 속삭인다.



### 묻지 말고 시켜라

* 앞에서 ReservationAgency는 Screening의 내부 Movie에 접근하는 대신 Screening에게 직접 요금을 계산하도록 요청했다.
    * 요금을 계산하는 데 필요한 정보를 잘 알고 있는 Screening에게 요금을 계산할 책임을 할당한 것이다.
    * 디미터 법칙은 훌륭한 메시지는 객체의 상태에 관해 묻지 말고 원하는 것을 시켜야 한다는 사실을 강조한다.
    * 묻지 말고 시켜라(Tell. Don't Ask)는 이런 스타일의 메시지 작성을 장려하는 원칙을 가리키는 원칙이다.
* 메시지 전송자는 메시지 수신자의 상태를 기반으로 결정을 내린 후 메시지 수신자의 상태를 바꿔서는 안 된다.
* 상태를 묻는 오퍼레이션을 행동을 요청하는 오퍼레이션으로 대체함으로써 인터페이스를 향상시켜라.



### 의도를 드러내는 인터페이스

* 켄트 백은 그의 기념비적인 책인 << Smalltalk Best Practice Patterns >>에서 메서드를 명명하는 두 가지 방법을 설명했다.
    * 첫 번째 방법은 메서드가 작업을 어떻게 수행하는지를 나타내도록 이름 짓는 것이다.
    * 이 경우 메서드의 이름은 내부의 구현 방법을 드러낸다.
    * 다음은 첫 번째 방법에 따라 PeriodCondition과 SequenceCondition의 메서드를 명명한 것이다.

```java
public Class PeriodCondition {
    public boolean isSatisfiedByPeriod(Screening screening) { ... }
}

public Class SequenceCondition {
    public boolean isSatisfiedBySequence(Screening screening) { ... }
}
```

* 이런 스타일은 좋지 않은데 그 이유를 두 가지로 요약할 수 있다.
    * 메서드에 대해 제대로 커뮤니케이션하지 못한다. 클라이언트의 관점에서 isSatisfiedByPeriod와 isSatisfiedBySequence 모두 할인 조건을 판단하는 동일한 작업을 수행한다. 하지만 메서드의 이름이 다르기 때문에 두 메서드의 내부 구현을 정확히 이해하지 못한다면 두 메서드가 동일 작업을 수행한다는 사실을 알아채기 어렵다.
    * 더 큰 문제는 메서드 수준에서 캡슐화를 위반한다는 것이다. PeriodCondition을 사용하는 코드를 SequenceCondition을 사용하도록 변경하려면 단순히 참조하는 객체를 변경하는 것뿐만 아니라 메서드를 변경해야 한다.

* 메서드의 이름을 짓는 두 번째 방법은 '어떻게'가 아니라 '무엇'을 하는지를 드러내는 것이다.
* 클라이언트 관점에서 협력을 바라봐야 한다. 두 메서드 모두 클라이언트의 의도를 담을 수 있도록 isSatisfiedBy로 변경하는 것이 적절할 것이다.
* 이처럼 어떻게 하느냐가 아니라 무엇을 하느냐에 따라 메서드의 이름을 짓는 패턴을 **의도를 드러내는 선택자(Intention Revealing Selector)**라고 부른다.
    * 어떻게 수행하는지를 드러내는 이름이란 메서드의 내부 구현을 설명하는 이름이다.
    * 두 메서드 모두 클라이언트의 의도를 담을수 있도록 isSatisfiedBy로 변경하는 것이 적절할 것이다.

```java
public Class PeriodCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}

public Class SequenceCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}
```

* 클라이언트가 두 메서드를 가진 동일한 타입으로 간주할 수 있도록 동일한 타입 계층으로 묶어야 한다.\

```java
public interface DiscountCondition {
  boolean isSatisfiedBy(Screening screening);
}
```

```java
public Class PeriodCondition implements DiscountCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}

public Class SequenceCondition implements DiscountCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}
```

* 이처럼 어떻게 하느냐가 아니라 무엇을 하느냐에 따라 메서드의 이름을 짓는 패턴을 **의도를 드러내는 선택자(Intention Revealing Selector)**라고 부른다.

* <<도메인 주도 설계>>에서 에릭 에반스는 켄트 벡의 의도를 드러내는 선택자를 인터페이스 레벨로 확장한 **의도를 드러내는 인터페이스(Intention Revealing Interface)**를 재시했다.



### 함께 모으기

#### 디미터 법칙을 위반하는 티켓 판매 도메인

* Theater의 enter 메서드는 디미터 법칙을 위반한 코드의 전형적인 모습을 잘 보여준다.

```java
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

기차 충돌 스타일의 전형적인 모습을 잘 보여준다.

```java
audience.getBag().minusAmount(ticket.getFee());
```

* 근본적으로 디미터 법칙을 위반하는 설계는 인터페이스와 구현의 분리 원칙을 위반한다.
    * 기억해야 할 점은 객체의 내부 구조는 구현에 해당한다는 것이다.
* 디미터 법칙을 위반한 코드는 사용하기도 어렵다.
    * 클라이언트 객체의 개발자는 Audience의 퍼블릭 인터페이스뿐만 아니라 Audience의 내부 구조까지 속속들이 알고 있어야 하기 때문이다.



#### 묻지 말고 시켜라

* Theater는 TicketSeller와 Audience의 내부 구조에 관해 묻지 말고 원하는 작업을 시켜야 한다.
* 먼저 Theater가 TicketSeller에게 자신이 원하는 일을 시키도록 수정하자.

```java
public class Theater {
  public void enter(Audience audience) {
    ticketSeller.setTicket(audience);
  }
}
```



.......



#### 인터페이스에 의도를 드러내자

* TicketSeller의 setTicket 메서드는 클라이언트의 의도를 명확하게 전달하고 있는가?
* Theater가 TicketSeller에게 setTicket 메시지를 전송해서 얻고 싶었던 결과는 무엇일까? 바로 Audience에게 티켓을 판매하는 것이다.
    * 따라서 setTicket 보다는 sellTo가 의도를 더 명확하게 표현하는 메시지라고 할 수 있다.
    * TicketSeller가 Audience에게 setTicket 메시지를 전송하는 이유는 무엇인가? Audience가 티켓을 사도록 만드는 것이 목적이다.

```java
public class TicketSeller {
  public void sellTo(Audience audience) { ... }
}

public class Audience {
  public Long buy(Ticket ticket) { ... }
}

public class Bag {
  public Long hold(Ticket  ticket) { ... }
}
```



## 03 원칙의 함정

### 디미터 법칙은 하나의 도트(.)를 강제하는 규칙이 아니다

```java
IntStream.of(1, 15, 20, 3, 9).filter(x -> x > 10).distinct().count();
```

이 코드는 디미터 법칙을 위반하지 않는다. 디미터 법칙은 결합도와 관련된 것이다.

IntStream을 다른 IntStream으로 변환할 뿐, 캡슐은 그대로 유지된다.



### 결합도와 응집도의 충돌





## 04 명령-쿼리 분리 원칙

* 어떤 절차를 묶어 호출 가능하도록 이름을 부여한 기능 모듈을 **루틴(routine)**이라고 부른다.
* 루틴은 다시 **프로시저(procedure)**와 **함수(function)**로 구분할 수 있다.
    * 프로시저는 부수효과를 발생시킬 수 있지만 값을 반환할 수 없다.
    * 함수는 값을 반환할 수 있지만 부수효과를 발생시킬 수 없다.
* 명령(Command)과 쿼리(Query)는 프로시저와 함수를 부르는 또 다른 이름이다.
* 명령-쿼리 분리 원칙을 한 문장으로 표현하면 "질문이 답변을 수정해서는 안 된다"는 것이다.



### 반복 일정의 명령과 쿼리 분리하기





### 명령-쿼리 분리와 참조 투명성





### 책임에 초점을 맞춰라

