# 7 Testing Architecture Elements

많은 프로젝트에서 자동화 테스트가 <u>mystery인</u> 것을 목격했다.위키에 문서화된 난잡한 규칙이 있기 때문에 모든 사람들은 그들이 생각하는 테스트 코드를 작성하지만 누구도 그 팀의 테스트 전략에 대한 대답을 할 수 없다.

이번 장은 헥사고날 아키텍처의 테스팅 전략을 제공한다. 아키텍처의 각 요소에 대한 테스트 유형을 논의해볼 것이다.



## The Test Pyramid

다음 그림에 테스트 피라미터에 대해 논의해보자. 이것은 얼마나 많은 테스트를 할 것인지 결정하고 어떤 목적으로 테스트를 해야 하는지에 대한 도움을 준다.

<img src="chapter-07.assets/image-20201226144035206.png" alt="image-20201226144035206" style="zoom:67%;" />

[그림 7.1] 테스트 피라미드에 따르면 <u>우리는 싼 테스트를 만들어야 하며 비싼 테스트를 줄여야 한다.</u>

이 그림은 작성이 용이하고 유주보수하기 쉽고 빠르게 실행되며 안정적인 잘게 쪼갠 테스트를 작성해야 한다고 나타낸다. 이것들은 기대한 대로 작동되는 단일 유닛을 확인하는 유닛 테스트이다.

일단 테스트가 여러가지 단위와 단위 사이의 경계, 아키텍처 경계 혹은 시스템 경계를 결합한다면 작성하기 어려워지고 실행이 느리며 깨지기 쉬워진다.(기능 오류 대신 구성에러 때문에 실패하기 때문에) 피라미드는 테스트가 더욱 비싸게 되면 될수록 테스트 커버리지는 더 낮게 맞춰야 한다라고 말해준다. 그렇지 않으면 새 기능 대신 테스트를 작성하는데 너무 많은 시간이 소요될 수 있기 때문이다. 

컨텍스트에 따라서 테스트 피라미드는 다른 레이어상에서 보여진다. 헥사고날 아키텍처를 테스트하는 레이어를 한번 살펴 보자. "단위 테스트", "통합 테스트", "시스템 테스트"의 정의는 문맥별로 다르다. 하나의 프로젝트에서 다른 의미로 이해될 수도 있다. 다음은 이장에서 사용하는 이 용어에 대한 해석이다.

단위 테스트는 피라미드의 바닥부분이다. 단위 테스트는 일반적으로 단일 클래스를 만들고 인터페이스에 의해 기능을 테스트한다. 테스트하는 클래스가 다른 클래스에 의존성이 있으면 다른 클래스는 인스턴스화 되지 않고 mock으로 대체되고 테스트 동안 필요할 때 실제 클래스의 행동을 시뮬레이션 한다.

통합 테스트는 피라미드의 다음 레이어를 나타낸다. 이 테스트는 네트워크를 연결하는 여러개의 단위를 인스턴스화 하고 어떤 데이터를 진입 클래스의 인터페이스로 보냈을 때 기대하는 대로 작동하는지 확인한다. 우리의 해석에 의하면 통합 테스트는 두개의 레이어 사이의 경계를 넘나들고 객체간의 네트웍 연결은 완전하기 않거나 mock처럼 동작한다.

마지막으로, 시스템 테스트는 애플리케이션의 UI를 포함하는 엔드 투 엔드(end-to-end) 테스트 레이어이다. 이 책에서는 백엔드 아키텍처에 대해서만 논의할 것이기 때문에 여기서는 고려하지 않겠다.

이제 테스트 타입을 정의했고 어느 유형의 테스트가 헥사고날 레이어에 가장 알맞는지 보자.



## Testing a Domain Entity with Unit Tests

아키텍터 중심부에 있는 도메인 엔티티를 볼 것이다. 4장 (Implementing a Use Case) Account 를 다시 생각해보자. Account의 상태는 계좌가 특정 시점에 있었던 잔고, 예금 목록, 인출 목록 등으로 구성되어 있다. withDraw() 메서드가 작동하는지 확인해보자.

```java
class AccountTest {

	@Test
	void withdrawalSucceeds() {
		AccountId accountId = new AccountId(1L);
		Account account = defaultAccount()
				.withAccountId(accountId)
				.withBaselineBalance(Money.of(555L))
				.withActivityWindow(new ActivityWindow(
						defaultActivity()
								.withTargetAccount(accountId)
								.withMoney(Money.of(999L)).build(),
						defaultActivity()
								.withTargetAccount(accountId)
								.withMoney(Money.of(1L)).build()))
				.build();

		boolean success = account.withdraw(Money.of(555L), new AccountId(99L));

		assertThat(success).isTrue();
		assertThat(account.getActivityWindow().getActivities()).hasSize(3);
		assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
	}
}
```

이 코드는 특정 상태의 Account를 인스턴스화 하는 단순 단위 테스트이고 withdraw()를 호출하고 인출이 성공했는지 확인하고 테스트 상 Account 객체의 상태에 부작용이 없었는지 확인한다.

테스트는 준비하기 쉽고 이해가 수비고 빠르게 실행된다. 이것보다 더 단순한 테스트는 없다. 이 같은 단위 테스트는 도메인 엔티티 내에 비즈니스 룰을 확인하는 가장 좋은 방법이다. 도메인 엔티티 행위가 다른 클래스의 의존성의 영향이 없기 때문에 다른 유형의 테스트가 필요없다.



## Testing a Use Case with Unit Tests

레이어 밖으로 잠시 나가면, 다음 테스트 할 아키텍처 요소는 유스케이스다. 4장(Implementing a Use Case)에서 논의했던 SendMOneyService의 테스트를 한번 보자. SendMoney 유스케이스는 잠시동안 잔고를 변경할 수 없게 하기 위해서 보내는사람 Account를 잠근다.성공적으로 계좌에서 돈을 인출했다면, 돈을 입금하기 위해서 받는 사람 계좌를 잠근다. 그러고 나서 두개의 계좌의 잠금을 풀 수 있다.

트랜잭션이 성공할 때 예상한 대로 진행됬는지 확인할 수 있다.

```java
class SendMoneyServiceTest {
    // declaration of fields omitted
    
    @Test
	void transactionSucceeds() {

		Account sourceAccount = givenSourceAccount();
		Account targetAccount = givenTargetAccount();

		givenWithdrawalWillSucceed(sourceAccount);
		givenDepositWillSucceed(targetAccount);

		Money money = Money.of(500L);

		SendMoneyCommand command = new SendMoneyCommand(
				sourceAccount.getId().get(),
				targetAccount.getId().get(),
				money);

		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isTrue();

		AccountId sourceAccountId = sourceAccount.getId().get();
		AccountId targetAccountId = targetAccount.getId().get();

		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));

		then(accountLock).should().lockAccount(eq(targetAccountId));
		then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(targetAccountId));

		thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
	}
  
    // helper methods omitted
}
```

테스트 코드를 가독성을 위해서 behavior-driven 개발에서 주로 사용되는 given/when/then 부분으로 구조화 되어 있다.

"given"에서 보내는 사람과 받는 사람 Account의 객체를 생성하고 given으로 시작하는 이름을 이용하여 올바른 상태로 만든다. 유스케이스의 입력값으로 사용되는 SendMoneyCommand 객체를 생성한다. "when"에서 유스케이스를 실행하기 위해서 sendMoney() 메소드를 호출한다. "then"에서 보내느사람과 받는 사람의 Account와 계좌의 lock/unlock의 책임이 있는 AccountLock 객체를 호출했다.

테스트는 given의 객체를 mock하기 위한 Mockito 라이브러리를 이용한다. Mockito는 어떤 메서드가 mock 객체에서 호출되었는지 확인하기 위해 then() 메서드를 제공한다.

유스케이스 서비스 테스트가 stateless하기 때문에 "then"에서 상태를 확인할 수 없다. 대신 테스트는 서비스가 의존관계에 있는 어떤 메서드를 호출했는지 확인한다. 이것은 테스트가 행위 뿐만 아니라 테스트 코드의 구조 변화에 취약하다는 것을 의미한다. 차례로 테스트 코드가 리펙토링 된다면 수정될 가능성이 많다는 것을 의미한다.

이것을 생각하고, 테스트에서 확인하고자 하는 내용에 대해 더 많이 생각해야 한다. 이전 테스트에서 했던 모든 인터랙션을 확인하는 것은 좋은 생각이 아닐것이다. 대신 중요한 부분에 집중해야 한다. 그렇지 않으면 테스트의 가치를 약화시키는 모든 테스트를 단일 테스트 코드로 변경해야만 할 것이다.



## Testing a. eb Adapter with Integration Tests

다른 레이어로 이동해보면 어댑터 부분이 있다. 웹 어댑터 테스트하는 것을 논의해보자.

웹 어댑터는 입력값을 JSON형식으로 받고 HTTP를 통해 어떤 검증을 하고 입력값을 유스케이스의 형식으로 매핑하고 유스케이스로 결과값을 반환한다는 것을 생각해보자. 유스케이스 결과를 JSON으로 변환하고 HTTP 응답을 통해 클라이언트에 반환한다.

웹 어댑터 테스트에서 모든 단계가 예상한대로 흘르가는지 확인한다.

```java
@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SendMoneyUseCase sendMoneyUseCase;

	@Test
	void testSendMoney() throws Exception {

		mockMvc.perform(post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
				41L, 42L, 500)
				.header("Content-Type", "application/json"))
				.andExpect(status().isOk());

		then(sendMoneyUseCase).should()
				.sendMoney(eq(new SendMoneyCommand(
						new AccountId(41L),
						new AccountId(42L),
						Money.of(500L))));
	}

}
```

이전 테스트는 Spring Boot 프레임워크에서 만든 SendMoneyController인 웹 컨트롤러의 표준 통합 테스트이다.



































