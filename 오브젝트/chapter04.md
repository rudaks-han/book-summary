# 4장 설계 품질과 트레이드 오프

* 객체지향 설계의 핵심은 역할, 책임, 협력이다.

* 협력은 애플리케이션의 기능을 구현하기 위해 <u>메시지를 주고 받는 객체들 사이의 상호작용이다</u>
* 책임은 객체가 다른 객체와 협력하기 위해 <u>수행하는 행동</u>
* 역할을 <u>대체 가능한 책임의 집합이다</u>

* 객체지향 설계란 올바른 객체에게 올바른 책임을 할당하면서 낮은 결합도와 높은 응집도를 가진 구조를 창조하는 활동이다.
* 이 정의에는 객체지향 설계에 관란 두 가지 관점이 섞여 있다.
    * 첫 번째 관점은 객체지향 설계의 핵심이 책임이라는 것이다.
    * 두 번째 관점은 책임을 할당하는 작업이 응집도와 결합도 같은 설계 품질과 깊이 연관돼 있다는 것이다.
* 설계는 변경을 위해 존재하고 변경에는 어떤 식으로든 비용이 발생한다.
    * 훌륭한 설계란 합리적인 비용안에서 변경을 수용할 수 있는 구조를 만드는 것이다.
* 결합도와 응집도를 합리적인 수준으로 유지할 수 있는 중요한 원칙이 있다.
    * 객체의 상태가 아니라 객체의 행동에 초점을 맞추는 것이다.
    * 객체를 단순한 데이터의 집합으로 바라보는 시각은 객체의 내부구현을 퍼블릭 인터페이스에 노출시키는 결과를 낳기 때문에 결과적으로 설계가 변경에 취약해진다.



## 01 데이터 중심의 영화 예매 시스템

* 객체지향 설계에서는 두 가지 방법을 이용해 시스템을 객체로 분할할 수 있다.
    * 첫 번째 방법은 상태를 분할의 중심축으로 삼는 방법
    * 두 번째 방법은 책임을 분할의 중심축으로 삼는 방법
* 데이터 중심의 관점에서 객체는 자신이 포함하고 있는 데이터를 조작하는 데 필요한 오퍼레이션을 정의한다.
* 책임 중심의 관점에서 객체는 다른 객체가 요청할 수 있는 오퍼레이션을 위해 필요한 상태를 보관한다.

* 시스템을 분할하기 위해 데이터와 책임 중 어느 것을 선택해야 할까? 
    * 결론부터 말하자면 훌륭한 객체지향 설계는 <u>데이터가 아니라 책임에 초점을 맞춰야 한다</u>. 
    * 이유는 변경과 관련이 있다.

* 객체의 상태
    * 객체의 상태는 구현에 속한다.
    * 구현은 불안정하기 때문에 변하기 쉽다.
    * 상태를 객체 분할의 중심축으로 삼으로 구현에 관한 세부사항이 객체의 인터페이스에 스며들게 되어 캡슐화의 원칙이 무너진다.
    * 결과적으로 상태 변경은 인터페이스의 변경을 초래하며 이 인터페이스에 의존하는 모든 객체에게 변경의 영향이 퍼지게 된다.
    * 따라서 데이터에 초점을 맞추는 설계는 변경에 취약할 수밖에 없다.
* 객체의 책임
    * 객체의 책임은 인터페이스에 속한다.
    * 인터페이스 뒤로 책임을 수행하는 데 필요한 상태를 캡슐화함으로써 구현 변경에 대한 파장이 외부로 퍼저나가는 것을 방지한다.
    * 따라서 책임에 초점을 맞추면 상대적으로 변경에 안정적인 설계를 얻을 수 있게 된다.



### 데이터를 준비하자

* 데이터 중심의 설계란 객체 내부에 저장되는 데이터를 기반으로 시스템을 분할하는 방법이다.

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;
}
```

* 기본적인 정보인 영화 제목, 상영시간, 기본 요금은 동일하다.
* 차이점은 할인 조건의 목록이 인스턴스 변수로 Movie안에 직접 포함돼 있다는 것이다.
* 또한 할인 정책을 DiscountPolicy라는 별도의 클래스로 분리했던 이전 예제와 달리 Movie안에 직접 정의하고 있다.
* 할인 정책의 종류를 결정하기 위해서 movieType을 사용한다.

```java
public enum MovieType {
    AMOUNT_DISCOUNT,
    PERCENT_DISCOUNT,
    NONE_DISCOUNT;
}
```

* 데이터 중심의 설계에는 객체가 포함해야 하는 데이터에 집중한다.
* 이 객체가 포함해야 하는 데이터는 무엇인가?

```java
public class Movie {
    public MovieType getMovieType() {
        return movieType;
    }

    public void setMovieType(MovieType movieType) {
        this.movieType = movieType;
    }

    public Money getFee() {
        return fee;
    }

    public void setFee(Money fee) {
        this.fee = fee;
    }

    public List<DiscountCondition> getDiscountConditions() {
        return Collections.unmodifiableList(discountConditions);
    }

    public void setDiscountConditions(List<DiscountCondition> discountConditions) {
        this.discountConditions = discountConditions;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
```

* 이제 할인 조건을 구현해보자.
* 할인 조건 타입을 저장할 DiscountConditionType을 정의하자.

```java
public enum  DiscountConditionType {
    SEQUENCE,
    PERIOD
}
```

```java
public class DiscountCondition {
    private DiscountConditionType type;

    private int sequence;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
```

```java
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getWhenScreened() {
        return whenScreened;
    }

    public void setWhenScreened(LocalDateTime whenScreened) {
        this.whenScreened = whenScreened;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
```

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Money getFee() {
        return fee;
    }

    public void setFee(Money fee) {
        this.fee = fee;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }
}
```

```java
public class Customer {
    private String name;
    private String id;

    public Customer(String name, String id) {
        this.id = id;
        this.name = name;
    }
}
```



### 영화를 예매하자

* ReservationAgency는 데이터 클래스들을 조합해서 영화 예매 절차를 구현하는 클래스다.

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



## 02 설계 트레이드 오프

### 캡슐화

* 상태와 행동을 하나의 객체 안에 모으는 이유는 객체의 내부 구현을 외부로부터 감추기 위해서다. 
* 여기서 구현이란 나중에 변경될 가능성이 높은 어떤 것을 가리킨다.
* 변경될 가능성이 높은 부분을 **구현**이라고 부르고 상대적으로 안정적인 부분을 **인터페이스**라고 부른다.
* 설계가 필요한 이유는 요구사항이 변경되기 때문이고, <u>캡슐화가 중요한 이유</u>는 <u>불안정한 부분과 안정적인 부분을 분리</u>해서 <u>변경의 영향을 통제</u>할 수 있기 때문이다. 
* 따라서 변경의 관점에서 설계의 품질을 판단하기 위해 캡슐화를 기준으로 삼을 수 있다.
* 정리하면 캡슐화란 변경 가능성이 높은 부분을 객체 내부로 숨기는 추상화 기법이다.
* 객체 내부에 무엇을 캡슐화해야 하는가? 
* 변경될 수 있는 어떤 것이라도 캡슐화해야 한다. 이것이 바로 객체지향 설계의 핵심이다.



### 응집도와 결합도

* 응집도는 모듈에 포함된 내부 요소들이 연관돼 있는 정도를 나타낸다. 
    * 모듈 내의 요소들이 하나의 목적을 위해 긴밀하게 협력한다면 그 모듈은 높은 응집도를 가진다.
* 결합도는 의존성의 정도를 나타내며 다른 모듈에 대해 얼마나 많은 지식을 갖고 있는지를 나타내는 척도다.
    * 어떤 모듈이 다른 모듈에 대해 너무 자세한 부분까지 알고 있다면 두 모듈은 높은 결합도를 가진다.

* 좋은 설계란 오늘의 기능을 수행하면서 내일의 변경을 수용할 수 있는 설계다.
* 높은 응집도와 낮은 결합도를 가진 설계를 추구해야 하는 이유는 단 한 가지다. 그것이 설계를 변경하기 쉽게 만들기 때문이다.
* 하나의 변경을 수용하기 위해 모듈 전체가 함께 변경된다면 응집도가 높은 것이고 모듈의 일부분만 변경된다면 응집도가 낮은 것이다.
* 응집도가 높을수록 변경의 대상과 범위가 명확해지기 때문에 코드를 변경하기 쉬워진다.
    * 변경으로 인해 수정되는 부분을 파악하기 위해 코드 구석구석을 헤매고 다니거나 여러 모듈을 동시에 수정할 필요가 없으며 변경을 반영하기 위해 오직 하나의 모듈만 수정하면 된다.
* 결합도는 한 모듈이 변경되기 위해서 다른 모듈의 변경을 요구하는 정도로 측정할 수 있다.
    * 하나의 모듈을 수정할 때 얼마나 많은 모듈을 함께 수정해야 하는지를 나타낸다.
    * 결합도가 높으면 높을수록 함께 변경해야 하는 모듈의 수가 늘어나기 때문에 변경하기가 어려워진다.



## 03 데이터 중심의 영화 예매 시스템의 문제점

데이터 중심의 설계가 가진 대표적인 문제점을 다음과 같이 요약할 수 있다.

* 캡슐화 위반
* 높은 결합도
* 낮은 응집도



### 캡슐화 위반

* Movie가 캡슐화의 원칙을 어기게 된 근본적인 원인은 객체가 수행할 책이밍 아니라 내부에 저장할 데이터에 초점을 맞췄기 때문이다.
* 앨런 홀럽은 접근자와 수정자에 과도하게 의존하는 설계 방식을 **추측에 의한 설계 전략(design-by-guessing strategy)**이라고 부른다.



### 높은 결합도

* ReservationAgent는 한 명의 예매 요금을 계산하기 위해 Movie의 getFee 메서들 호출하며 계산된 결과를 Money 타입의 fee에 저장한다. 이때 fee의 타입을 변경한다고 가정해보자.
    * getFee 메서드의 반환 타입도 함께 수정해야 할 것이다.
    * ReservationAgency의 구현도 수정해야 할 것이다.
* ReservationAgency가 모든 데이터 객체에 의존한다는 것을 알 수 있다. 시스템 안의 어떤 변경도 ReservationAgency의 변경을 유발한다.



### 낮은 응집도

다음과 같은 수정사항이 발생하는 경우에 ReversationAgency의 코드를 수정해야 할 것이다.

* 할인 정책이 추가될 경우
* 할인 정책별로 할인 요금 계산하는 방법이 변경될 경우
* 할인 조건이 추가되는 경우
* 할인 조건별로 할인 여부를 판단하는 방법이 변경될 경우
* 예매 요금을 계산하는 방법이 변경될 경우

낮은 응집도는 두 가지 측면에서 설계에 문제를 일으킨다.

* 변경과 아무 상관이 없는 코드들이 영향을 받게 된다.
    * 예를 들어 ReservationAgency 안에 할인 정책을 선택하는 코드와 할인 조건을 판단하는 코드가 함께 존재하기 때문에 새로운 할인 정책을 추가하는 작업이 할인 조건에도 영향을 미칠 수 있다.
* 하나의 요구사항 변경을 반영하기 위해 동시에 여러 모듈을 수정해야 한다.
    * 새로운 할인 정책을 추가해야 한다고 가정해보자.
    * MovieType에 새로운 할인 정책을 표현하는 열거형 값을 추가
    * ReservationAgency의 reserve 메서드의 switch 구문에 추가해야하고
    * 필요한 데이터를 Movie에 추가



## 04 자율적인 객체를 향해

### 캡슐화를 지켜라



### 스스로 자신의 데이터를 책임지는 객체



## 05 하지만 여전히 부족하다

### 캡슐화 위반

* isDiscountable(DayOfWeek dayOfWeek, LocalTime time) 메서드의 시그니처를 보면 DiscountCondition에 속성으로 포함돼 있는 DayOfWeek 타입의 요일 정보와 LocalTime 타입의 시간 정보를 파라미터로 받는다.
    * 이 메서드는 객체 내부에 DayOfWeek 타입의 요일과 LocalTime 타입의 시간 정보가 인스턴스 변수로 포함돼 있다는 사실을 외부에 노출하고 있다.
    * isDiscountable(int sequence) 메서드 역시 int 타입의 정보를 포함하고 있음을 외부에 노출한다.
* Movie 역시 내부 구현을 인터페이스에 노출시키고 있다.
    * calculateAmountDiscountedFee, calculatedPercentDiscountedFee, calculateNoneDiscountedFee라는 세 개의 메서드를 노출하고 있다.



> **캡슐화의 진정한 의미**
>
> 캡슐화란 변할 수 있는 어떤 것이라도 감추는 것이다. 그것이 속성 타입이건, 할인 정책의 종류건 상관 없이 내부 구현의 변경으로 인해 외부의 객체가 영향을 받는다면 캡슐화를 위반한 것이다. 설계에서 변하는 것이 무엇인지 고려하고 변하는 개념을 캡슐화해야 한다.



### 높은 결합도

* 중요한 것은 Movie와 DiscountCondition 사이의 결합도이므로 DiscountCondition에 대한 어떤 변경이 Movie에게까지 영향을 미치는지를 살펴봐야 한다.

    * DiscountCondition의 기간 할인 조건의 명칭이 PERIOD에서 다른 값으로 변경된다면 Movie를 수정해야 한다.
    * DiscountCondition의 종류가 추가되거나 삭제된다면 Movie안의 if ~ else 구문을 수정해야 한다.
    * 각 DiscountCondition의 만족 여부를 판단하는 데 필요한 정보가 변경된다면 Movie의 isDiscountable 메서드로 전달된 파라미터를 변경해야 한다.

    

### 낮은 응집도

* 할인 조건의 종류를 변경하기 위해서는 DiscountCondition, Movie 그리고 Screening을 함께 수정해야 한다. 하나의 변경을 수용하기 위해 코드의 여러 곳을 동시에 변경해야 한다는 것은 설계의 응집도가 낮다는 증거다.



## 06 데이터 중심 설계의 문제점

두 번째 설계가 변경에 유연하지 못한 이유는 캡슐화를 위반했기 때문이다.



### 데이터 중심 설계는 객체의 행동보다는 상태에 초점을 맞춘다

* 데이터 중심 설계 방식에 익숙한 개발자들은 일반적으로 데이터와 기능을 분리하는 절차적 프로그래밍 방식을 따른다.
* 데이터 중심의 관점에서는 객체는 그저 단순한 데이터의 집합체일 뿐이다.
* 이로 인해 접근자와 수정자를 과도하게 추가하게 된다.
* 접근자와 수정자는 public 속성과 큰 차이가 없기 때문에 객체의 캡슐화는 완전히 무너질 수밖에 없다.



### 데이터 중심 설계는 객체를 고립시킨 채 오퍼레이션을 정의하도록 만든다























