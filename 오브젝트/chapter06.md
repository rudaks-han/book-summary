# 6장 메시지와 인터페이스

협력과 메시지의 기본적인 개념을 살펴보자



## 01 협력과 메시지

### 클라이언트-서버 모델

* 협력은 어떤 객체가 다른 객체에게 무언가를 요청할 때 시작된다.
* 객체가 다른 객체에게 접근할 수 있는 유일한 방법은 메시지를 전송하는 것뿐이다.
* 두 객체 사이의 협력 관계를 설명하기 위해 사용하는 전통적인 메타포는 **클라이언트-서버(Client-Server) 모델**이다.
    * Screening은 클라이언트의 역할을 수행하고 Movie는 서버의 역할을 수행한다.
    * Screening은 가격을 계산하라 메시지를 전송하고 Movie는 가격을 계산하는 서비스를 제공함으로써 메시지에 응답한다.
* Movie는 할인 요금 계산하라 메시지를 DiscountPolicy의 인스턴스에 전송해서 할인 요금을 반환받는다.



### 메시지와 메시지 전송

* 한 객체가 다른 객체에게 도움을 요청하는 것을 **메시지 전송(message sending)** 또는 **메시지 패싱(message passing)**이라고 부른다.
* 이때 메시지를 전송하는 객체를 **메시지 전송자(message sender)**라고 부르고 메시지를 수신하는 객체를 **메시지 수신자(message receiver)**라고 부른다.
* isSatisfiedBy(screening)이 '메시지'이고, 여기에 메시지 수신자인 condition을 추가한 condition.isSatisfiedBy(screening)이 '메시지 전송'이다.



### 메시지와 메서드

* condition.isSatisfiedBy(screening)은 실제로 실행된ㄴ 코드는 달라진다.
* 이처럼 메시지를 실제로 실행되는 함수 또는 프로시저를 **메서드**라고 부른다.
* 객체의 메시지와 메서드라는 두 가지 서로 다른 개념을 실행 시점에 연결해야 하기 때문에 컴파일 시점과 실행 시점의 의미가 달라질 수 있다.
* 메시지 전송자는 자신이 어떤 메시지를 전송해야 하는지만 알면 된다.
* 수신자가 어떤 클래스의 인스턴스인지, 어떤 방식으로 요청을 처리하는지 모르더라도 원활한 협력이 가능하다.
* 메시지 수신자 역시 누가 메시지를 전송하는지 알 필요가 없다. 단시 메시지가 도착했다는 사실만 알면 된다.



### 퍼블릭 인터페이스와 오퍼레이션

* 객체가 의사소통을 위해 외부에 공개하는 메시지의 집합을 **퍼블릭 인터페이스**라고 부른다.
* 프로그래밍 언어의 관점에서 퍼블릭 인터페이스에 포함된 메시지를 **오퍼레이션(operation)**이라고 부른다.
* DiscountCondition 인터페이스에 정의된 isSatisfiedBy가 오퍼레이션이다.
* 그에 반해 메시지를 수신했을 때 실제로 실행되는 코드를 메서드라고 부른다.
* SequenceCondition/PeriodCondition의 isSatisfiedBy가 메서드이다.



### 시그니처

* 오퍼레이션(또는 메서드)의 이름과 파라미터 목록을 합쳐 **시그니처(signature)**라고 부른다.
* 오퍼레이션은 실행 코드 없이 시그니처만을 정의한 것이다.
* 메서드는 시그니처에 구현을 더한 것이다.



## 02 인터페이스와 설계 품질

* 좋은 인터페이스는 최소한의 인터페이스와 추상적인 인터페이스라는 조건을 만족해야 한다.
* 추상적인 인터페이스는 어떻게 수행하는지가 아니라 무엇을 하는지를 표현한다.

퍼블릭 인터페이스의 품질에 영향을 미치는 원칙과 기법

* 디미터 법칙
* 묻지 말고 시켜라
* 의도를 드러내는 인터페이스
* 명령-쿼리 법칙



### 디미터 법칙

* 객체의 내부 구조와 강하게 결합되지 않도록 협력 경로를 제한하라는 것
* "낮선 자에게 말하지 말라(don't talk to stranger)"
* "오직 인접한 이웃하고만 말하라(only talk to your immediate neighbors)"
* "하나의 도트만 사용하라(use only one dot)"

클래스 내부의 메서드가 아래 조건을 만족하는 인스턴스에만 메시지를 전송해야 한다.

* this 객체
* 메서드의 매개변수
* this의 속성
* this의 속성인 컬렉션 요소
* 메서드 내에서 생성된 지역 객체



### 묻지 말고 시켜라

* 묻지 말고 시켜라(Tell, don't Ask)
* ReservationAgent는 Movie에 접근하는 대신 Screening에게 직접 요금을 계산하도록 요청했다.



### 의도를 드러내는 인터페이스

```java
public Class PeriodCondition {
    public boolean isSatisfiedByPeriod(Screening screening) { ... }
}

public Class SequenceCondition {
    public boolean isSatisfiedBySequence(Screening screening) { ... }
}
```

이런 스타일은 좋지 않은데 그 이유를 두 가지로 요약할 수 있다.

* 메서드에 대해 제대로 커뮤니케이션하지 못한다. 클라이언트의 관점에서 isSatisfiedByPeriod와 isSatisfiedBySequence 모두 할인 조건을 판단하는 동일한 작업을 수행한다. 하지만 메서드의 이름이 다르기 때문에 두 메서드의 내부 구현을 정확히 이해하지 못한다면 두 메서드가 동일 작업을 수행한다는 사실을 알아채기 어렵다.
* 더 큰 문제는 메서드 수준에서 캡슐화를 위반한다는 것이다. PeriodCondition을 사용하는 코드를 SequenceCondition을 사용하도록 변경하려면 단순히 참조하는 객체를 변경하는 것뿐만 아니라 메서드를 변경해야 한다.

메서드의 이름을 짓는 두 번째 방법은 '어떻게'가 아니라 '무엇'을 하는지를 드러내는 것이다.

클라이언트 관점에서 협력을 바라봐야 한다. 두 메서드 모두 클라이언트의 의도를 담을 수 있도록 isSatisfiedBy로 변경하는 것이 적절할 것이다.

```java
public Class PeriodCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}

public Class SequenceCondition {
    public boolean isSatisfiedBy(Screening screening) { ... }
}
```

이처럼 어떻게 하느냐가 아니라 무엇을 하느냐에 따라 메서드의 이름을 짓는 패턴을 **의도를 드러내는 선택자(Intention Revealing Selector)**라고 부른다.

<<도메인 주도 설계>>에서 에릭 에반스는 켄트 벡의 의도를 드러내는 선택자를 인터페이스 레벨로 확장한 **의도를 드러내는 인터페이스(Intention Revealing Interface)**를 재시했다.



### 함께 모으기

#### 디미터 법칙을 위반하는 티켓 판매 도메인

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



#### 묻지 말고 시켜라



#### 인터페이스에 의도를 드러내자

* 객체는 자신이 아닌 클라이언트의 의도를 표현하는 이름을 가져야 한다. 
* sellTo, buy, hold는 클라이언트가 객체에게 무엇을 원하는지를 명확하게 표현한다. setTicket은 그렇지 않다.



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

