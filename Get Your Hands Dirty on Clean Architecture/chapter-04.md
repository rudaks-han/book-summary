# 4 Implementing a Use Case

실제 코드상에서 논의한대로 아키텍처를 어떻게 명확히 나타내는지를 한번 보자.

애플리케이션, 웹, 영속성 레이어는 아키텍처에서 느슨하게 결합되어 있기 때문에 도메인 코드를 자유롭게 모델링할 수 있다. DDD를 사용하수도 있고 풍부하거나 빈약한(anemic) 도메인 모델을 구현할 수도 있고 자신만의 방법을 개발할 수도 있다.

이 장에서는 이전 장에서 소개했던 헥사고날 아키텍처 스타일 내의 유스케이스를 구현하는 자신만의 방법은 나타낸다.

도메인 중심 아키텍처 적합한 도메인 엔터티를 만들고 거기에 유스케이스를 만들어 볼 예정이다.



## Implementing the Domain Model

계좌 간의 송금에 대한 유스케이스를 구현해보자. 이런 객체 지향 방식을 모델링 하는 방법은 인출/예금이 가능하도록 계좌간의 인출 시 다른 계좌로 송금하는 방식으로 Account 엔티티를 만드는 것이다.

```java
package buckpal.domain;

@AllArgsConstructor
@Getter
public class Account {
    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money calculateBalance() {
        return Money.add(
            this.baselineBalance,
            this.activityWindow.calculateBalance(this.id)
        );
    }

    public boolean withDraw(Money money, AccountId targetAccountId) {
        if (!mayWithDraw(money)) {
            return false;
        }

        Activity withDrawal = new Activity(
            this.id,
            this.id,
            targetAccountId,
            LocalDateTime.now(),
            money
        );
        this.activityWindow.addActivity(withDrawal);
        return true;
    }

    private boolean mayWithDraw(Money money) {
        return Money.add(
            this.calculateBalance(),
            money.negate()
        ).isPositive();
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = new Activity(
            this.id,
            sourceAccountId,
            this.id,
            LocalDateTime.now(),
            money
        );
        this.activityWindow.addActivity(deposit);
        return true;
    }
}

```



Account 엔티티는 실제 계정의 현재 상태를 제공한다. 모든 인출과 예금은 Activity 엔티티에서 일어난다. 계정의 모든 액티비티를 메모리로 로딩하는 것은 좋은 방법이 아니고 Account 엔티티가 지난 몇일 혹은 몇주의 정보만 ActivityWindow 객체에 담아야한다.

현재 계좌 잔고를 계산하기 위해서 Account 엔티티는 추가적으로 계좌가 이전에 실행된 잔고를 나타내는 baselineBalance 속성을 가지고 있다.

이 모델로 계좌로의 인출 및 예금은 withdraw()와 deposit() 메소드에서 했던 새로운 액티비를 추가하는 문제인 것이다. 인출하기 전에 인출을 할 수 없는 규칙을 한번 확인해 볼 것이다.

이제 인출 및 예금을 할 수 있는 Account 엔티티가 있고 유스케이스를 만들기 위해 외부로 옮길 수 있다.



## A Use Case in a Nutshell

