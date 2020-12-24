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



Account 엔티티는 실제 계좌의 현재 상태를 제공한다. 모든 인출과 예금은 Activity 엔티티에서 일어난다. 계좌의 모든 액티비티를 메모리로 로딩하는 것은 좋은 방법이 아니고 Account 엔티티가 지난 몇일 혹은 몇주의 정보만 ActivityWindow 객체에 담아야한다.

현재 계좌 잔고를 계산하기 위해서 Account 엔티티는 추가적으로 계좌가 이전에 실행된 잔고를 나타내는 baselineBalance 속성을 가지고 있다.

이 모델로 계좌로의 인출 및 예금은 withdraw()와 deposit() 메소드에서 했던 새로운 액티비를 추가하는 문제인 것이다. 인출하기 전에 인출을 할 수 없는 규칙을 한번 확인해 볼 것이다.

이제 인출 및 예금을 할 수 있는 Account 엔티티가 있고 유스케이스를 만들기 위해 외부로 옮길 수 있다.



## A Use Case in a Nutshell

먼저, 유스 케이스가 실제 무슨 작업을 하는지 알아보자. 일반적으로 다음 단계를 거치게 된다.

1. 입력을 받는다.
2. 비즈니스 규칙을 확인한다.
3. 모델의 상태를 변경한다.
4. 결과를 리턴한다.

유스케이스는 입력 어댑터로 부터 입력값을 받는다. 입력값 검증을 왜 호출하지 않았는지 의문이 들지도 모르겠다. 그 대답은 유스케이스는 도메인 로직에만 괌심을 가져야 하고 입력값 검증같은 것으로 오염이 되어서는 안된다고 믿기 때문이다. 입력값 검증은 따로 얘기할 예정이다.

그러나, 유스케이스는 비즈니스 룰에 대한 책임은 있다. 이러한 책임은 도메인 엔티티와 공유한다. 이 장의 후반부에서 입력값 검증과 비즈니스 룰 검증과의 차이에 대해 논의할 예정이다.

비즈니스 룰이 만족되었다면 유스케이스는 입력값에 기반하여 모델 상태를 변경한다. 일반적으로 도메인 객체의 상태를변경할 것이며 내소룬 상태를 어댑터의 영속화계층에서 구현된 포트로 새로운 상태를 전달할 것이다. 유스케이스는 또한 다은 외부 어댑터를 호출할 수도 있다. 마지막 단계는 외부 어댑터에서 외부 객체로 리턴값을 변환하는 작업이다.

이런 단계를 숙지하고서 "송금" 유스케이스를 어떻게 구현할지 알아보자.

1장(Wrong with Layers)에서 논의한 넓은 범위의 서비스 문제를 피하기 위해서 모든 유스케이스를 단일 클래스에 넣지 않고 각각 서비스 클래스로 분할하여 사용할 것이다.

```java
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        // TODO: validate business rules
        // TODO: manipuate model state
        // TODO: return output
    }
}
```

서비스는 내부 포트 인트페이스인 SendMoneyUseCase를 구현하고 외부 포트 인터페이스인 LoadAccountPort를 계좌을 가져오기 위해서 호출하고 데이터베이스에 계좌 상태를 저장하기 위해서 UpdateAccountStatePort를 호출한다. 다음 그림은 컴포넌트를 시작적으로 표현한 그림이다.

<img src="chapter-04.assets/image-20201224212232326.png" alt="image-20201224212232326" style="zoom:67%;" />

[그림 4.1] 서비스는 유스케이스를 구현하고 도메인 모델을 수정한다. 그리고 수정된 상태를 저장하기 위해 외부 포트를 호출한다.



## Validating Input

이제, 유스케이스의 책임이 아니었다고 강조했던 입력값 검증에 대해 이야기해보자. 나는 아직도 애플리케이션 레이어에 속해야 한다고 생각하기 때문에 한번 이야기해보자.

호출하는 어댑터가 유스케이스로 보내기 전에 입력값 검증을 왜 하지 말아야 하는걸까? 유스케이스에 필요한 모든것을 이미 검증했다고 믿어야 할까? 유스케이스는 하나의 어댑터에서만 호출되는 것은 아니기 때문에 유효성 검증은 각각 어댑터에서 구현되어야 하고 잘못 사용될 수도 있을 것이다.

애플리케이션 레이어는 입력값 검증을 신경써야 한다. 그렇지 않으면 애플리케이션 코어 외부에서 잘못된 입력 정보를 받을 수 있고 그로 인해 모델 상태에 심각한 문제를 유발할 수도 있다.

그러나 유스케이스 클래스가 아니고 어디에서 입력값 검증을 해야 할까?

입력 모델 객체를 하나 만들어서 관리할 것이다. "송금" 유스케이스에서 입력 모델은 이전 코드에서 봤던 "SendMoneyCommand" 클래스이다.

```java
@Getter
public class SendMoneyCommand {
		private final AccountId sourceAccountId;
		private final AccountId targetAccountId;
		private final Money money;

		public SendMoneyCommand(
				AccountId sourceAccountId,
				AccountId targetAccountId,
				Money money) {
      		this.sourceAccountId = sourceAccountId;
			 this.targetAccountId = targetAccountId;
			 this.money = money;
      		requireNonNull(sourceAccountId);
      		requireNonNull(targetAccountId);
      		requireNonNull(money);
      		requireGreaterThan(money, 0);
		}
	}
```

송금을 하기 위해서 전송자와 수신자의 ID, 송금액 등의 정보가 필요하다. 어떤 파라미터도 null이 되어서는 안되며 송금액도 0보다는 커야 한다. 이런 조건이 위배되면 생성자에서 예외를 던지기만 하면 된다.

SendMoneyCommand 필드를 만들때 <u>immutable</u>로 만든다. 그래서 일단 생성되고 나면 변경될 수 없게 한다.

SendMoneyCommand는 유스케이스 API의 한 부분이기 때문에 내부 포트 패키지에 위치한다. 그래서 유효성 확인은 애플리케이션 코어에 남아있지만 유스케이스 코드를 오염시키지 않는다.

그러나 각각의 번거로운 유효성 확인을 수작업으로 하기를 원하는 걸까? java 계열에서는 Bean Validation API가 표준으로 사용된다. 클래스 필드의 어노테이션을 붙이기만 하면 된다.

```java
class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

		@NotNull
		private final AccountId sourceAccountId;

		@NotNull
		private final AccountId targetAccountId;

		@NotNull
		private final Money money;

		public SendMoneyCommand(
				AccountId sourceAccountId,
				AccountId targetAccountId,
				Money money) {
			this.sourceAccountId = sourceAccountId;
			this.targetAccountId = targetAccountId;
			this.money = money;
			this.validateSelf();
		}
	}
```

SelfValidating 추상 클래스는 validateSelf() 메소드를 제공하고 생성자 마지막에 그냥 호출만 하면 된다. 이것은 필드(@NotNull)에 Bean Validation 어노테이션을 검증하고 위반 시 예외를 던진다. Bean 검증이 어떤 경우에는 사용할 수 없다면, 금액이 0보다 큰지 여부를 확인하는 것처럼 직접 구현할 수도 있다. 

SelfValidating의 구현부는 다음과 같다.

```java
public abstract class SelfValidating<T> {

  private Validator validator;

  public SelfValidating() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  protected void validateSelf() {
    Set<ConstraintViolation<T>> violations = validator.validate((T) this);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
```

<u>입력 모델에 있는 validation으로 유스케이스 구현부 금처에 오염방지 레이어를 생성했다. 이것은 계층형 아키텍처에서의 레이어가 아니다. 다음 레이어를 호출하고 호출자의 잘못된 입력을 경계짓어주는 유스케이스를 보호하는 경계막이다.</u>



## The Power of Contructors

이전 입력 모델 (SendMoneyCommand)은 생성자에서 많은 책임을 가진다. 클래스가 <u>변경불가능(immutable)</u>이기 때문에 생성자의 인자는 클래스의 각 속성을 포함한다. 생성자는 파라미터를 검증하기 때문에 불완전한 상태의 객체를 생성하지 못한다.

우리의 케이스에서, 생성자는 3개의 파라미터만 가지고 있다. 더 많은 파라미터가 필요하다면 어떻게 해야 할까? 좀 더 편리하게 사용하기 위해 Builder 패턴을 사용해야 할까? 긴 파라미터를 private으로 만들고 builder() 메소드 안으로 숨길수도 있다. 20개의 파라미터로 생성자를 호출하는 대신에 이와 같이 객체를 만들 수 있다.

```java
new SendMoneyCommandBuilder()
  	.sourceAccountId(new AccountId(41L))
  	.targetAccountId(new AccountId(42L))
  	// ... initialize many other fields
  	.build();
```

builder가 불안전하게 객체를 만들지 못하게 생성자가 입력값 검증을 할수도 있다.

좋은 것 같지 않는가? SendMoneyCommandBuilder(소프트웨어 프로젝트에서 몇번씩 발생하는)에 또 다른 필드를 추가하면 어떤일이 생길지 생각해보자. 생성자와 빌더에 필드를 추가한다. 그리고 나서 동료(전화, 이메일, 기타 등)가 <u>연속적인 사고의 흐름을 방해한다.</u> 잠시 휴식후에 다시 코드를 작업하는데 코드와 빌더에 새필드를 추가해야 한다는 것을 까먹는다.

Immutable 객체를 불완전항 상태로 생성하는데 컴파일러로 부터 어떤 경고 메시지를 받지 않았다. 물론 파라미터를 빠트렸기 때문에 런타임에서 검증 로직이 오류를 던질 것이다.

새 필드가 추가 혹은 삭제될 때마다 빌더 뒷쪽에 숨기기 않고 직접 생성자를 사용한다면 코드상 변화를 반영하도록 컴파일 에러를 확인할 수 있을 것이다.

긴 파라미터 리스트는 보기 좋게 포맷팅 될 수 있고 IDE가 좋다면 파라미터 이름에 대한 힌트도 제공해 줄것이다.



## Different Input Models for Different Use Cases

우리는 다른 유스케이스에 대해 동일한 입력 모델을 사용하기 쉽다. "계좌 등록"과 "계좌 정보 수정" 유스케이스를 생각해보자. 둘은 최초에는 동일 입력값, 즉 계좌 설명과 같은 계좌 상세정보들이 필요하다.

차이점은 "계좌 정보 수정" 유스케이스의 경우는 특정 계좌를 수정할 ID가 필요하다는 것이다. 그리고 "계좌 등록" 유스케이스는 소유자의 ID가 필요할 수도 있고 누군가에게 부여할 수도 있을 것이다. 양쪽 유스케이스에 대해 동일 입력 모델을 사용한다면 "계좌 정보 수정" 유스케이스에서 계좌 ID에 대해 null을 허용해야만 하고 "계좌 등록" 유스케이스에 소유자 ID에 null을 허용해야 할 것이다.

immutable 객체에서 null을 유효상태로 허용하는 것은 code smell 이다. 더욱 중요한 것은 입력값 검증은 어떻게 처리할 수 있을까? 입력값 검증은 등록, 수정에 대한 유스케이스가 달려져야만 한다. 각각의 입력값 검증 로직이 유스케이스에 작성이 되어야하고 입력값 검증에 대한 염려로 인해 <u>비즈니스 코드를 오염시키???</u>

또한 "계좌 등록" 유스케이스에서 계좌 ID 필드가 non-null 값이라면 어떻겠는가? 에러를 던질 것인가? 그냥 무시할 수 있는가? 코드를 볼 때 의문을 제기할 만한 유지보수에 관련된 내용들이다.

각 유스케이스의 전용 입력 모델은 유스케이스를 더욱 분명하게 하고 다른 유스케이스로 부터 디커플링하고 원지 않은 부작용을 없앤다. 그러나 각각의 유스케이스에 입력 모델마다 입력값을 매핑해야 하므로 비용이 드는 작업이다. 8장(Mapping between Boundaries)에서 매핑 전략에 대해서 논의해 볼것이다.



## Validation Business Rules

단순 입력값 검증하는 작업이 아니라 비즈니스 룰의 확인하는 작업이 유스케이스 로직의 한 부분이다. 비즈니스 룰은 애플리케이션의 중심이며 적절하게 처리되어야 한다. 그럼 언제 입력값 검증을 하고 언제 비즈니스 룰 확인을 해야 하는걸까?

그 두가지 사이에 구분하는 방법은 다음과 같다.

비즈니스 룰 검증은 도메인 모델의 현재 상태의 접근을 필요로 하는 것이고 반면에 입력값 검증은 그렇지 않다. 입력값 검증은 @NotNull 어노테이션에서 했던 것처럼 선언적으로만 구현될 수도 있지만 비즈니스 룰은 더 많은 컨텍스트가 필요하다.

입력값 검증은 구문상 검증이며 반면에 비즈니스 룰은 유스케이스의 의미상 검증이다.

"계좌는 초과인출되어서는 안된다"는 룰을 한번 보자. 의미상으로 소스와 타켓 계좌가 존재하는지 확인하는 모델의 상태를 접근할 필요가 있기 때문에 비즈니스 룰이다.

대조적으로 "송금액은 0보다 커야한다"라는 룰은 모델 접근없이 확인될 수 있고 입력값의 한 부분으로 처리될 수 있다.

이 차이에 대한 논쟁의 의지가 많다는 것을 안다. 송금액이 매우 중요하기 때문에 어떤 경우에는 비즈니스 룰로 판단되어야 한다고 주장할 수도 있다.

위의 구별은 코드상 입력값 확인을 어디에 두어야 하는지 결정할 때 도움을 준다. 모델상태에 접근하는지 아닌지에 대한 단순한 대답이다. 특정 부분에서 규칙을 구현하는데 도움이 되며 향후 유지보수를 하는데도 도움이 된다.

그럼 비즈니스 룰을 어떤게 구현할까?

최선의 방법은 "계좌는 초과 인출될 수 없다" 규칙에서 봤던 것처럼 도메인 엔티티 내부에 두는 것이다.

```java
package buckpal.domain;

public class Account {
  // ...
  public boolean withdraw(Money money, AccountId targetAccountId) {
		if (!mayWithdraw(money)) {
			return false;
		}
	    // ...
}
```

이런 식으로 비즈니스 룰을 배치할 수 있고 규칙이 필요한 로직 옆에 둘 수 있다.

도메인 엔티티 내에서 비즈니스 규칙을 확인하는 것이 쉽지 않다면 도메인 엔티티 작업을 시작하기 전에 유스케이스 코드에 둘 수도 있다.

```java
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {
  	// ...
  	@Override
  	public boolean sendMoney(SendMoneyCommand command) {
      	requireAccountExists(command.getSourceAccountId());
      	requireAccountExists(command.getTargetAccountId());
	  }
}
```

실제 입력값 검증을 하는 메소드를 호출하여 실패한다면 전용 예외를 던질 수 있다. 사용자 측 어댑터는 오류메시지로 예외를 표시를 하거나 다른 방법으로 처리될 수 있다.

이전 케이스에서 입력값 검증은 단순히 소스와 타켓 계좌가 데이터베이스에 존재하는지만 확인한다. 더욱 복잡한 비즈니스 룰은 데이터베이스에서 도메인 모델을 로딩해야 하고 상태를 체크해야 할지도 모른다. 어쨋던, "계좌는 초과 인출되어서는 안된다"라는 룰에서 했던 것처럼, 도메인 모델을 로드해야 한다면 도메인 엔티티에서 비즈니스 룰을 구현해야만 한다. 



## Rich versus Anemic Domain Model























































