# 7 아키텍처 구성요소 테스트

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

이전 테스트는 Spring Boot 프레임워크에서 만든 SendMoneyController인 웹 컨트롤러의 표준 통합 테스트이다. testSendMoney() 메서드에서 입력 객체를 생성하고 mock HTTP 요청을 웹 컨트롤러에 보낸다. 요청 본문은 JSON 문자열로 입력 객체를 포함한다.

isOk() 메소드와 함께 HTTP 응답상태가 200인 것을 확인하고 mock 유스케이스가 호출된 것을 확인한다.

웹 어댑터의 대부분의 책임은 이 테스트를 포함한다.

MockMVC 객체로 mocking하고 있기 때문에 HTTP 프로토콜로 실제 테스트를 하지는 않는다. 프레임워크가 HTTP를 적절하게 변환한다고 믿어서 프레임워크를 테스트할 필요가 없다.

JSON의 입력을 SendMoneyCommand 객체로 매핑하는 전체 경로가 포함된다. SendMoneyCommand 객체를 4장(Implementing a Use Case에서 설명한것처럼 <u>self-validating command로 만든다면</u>, 우리는 이러한 매핑이 구문상 유스케이스에 대한 유효한 입력이라고 확인했다. 또한 유스케이스가 호출되고 HTTP 응답이 예상 상태를 가진다고 확인했다.

이것이 왜 통합테스트이고 단위 테스트가 아닌가? 이 테스트에서 단일 웹 컨트롤러 클래스만을 테스트하는 것처럼 보여도 <u>포함하는 많은 것들이 있다.</u> @WebMvcTest 어노테이션으로 Spring이 요청에 응답하는 전체 네트워크 상의 객체를 인스턴스하고 Java와 JSON 사이에 매핑을 하며 HTTP 입력확인 등을 하라고 전달한다. 이 테스트에서 웹 컨트롤러가 네트워크의 한 부분인 것처럼 작동하는 것을 확인하고 있다.

웹 컨트롤러가 스프링 프레임워크에 강하게 연결되어 있기 때문에 분리하여 테스트하는 대신 이 프레임워크 상에서 통합하여 테스트하는 것은 의미가 있다. 웹 컨트롤러를 단순 유닛 테스트와 테스트했다면 모든 메핑, 입력값 검증, HTTP 같은것을 포함하지 못할 것이며 운영상에 실제로 동작할지 확인할 수 없을 것이다. 



## Testing a Persistence Adapter with Integration Tests

비슷한 이유로 어댑터 내부 로직을 화인하지 않고 데이터베이스와의 매핑만 확인하기 때문에 영속성 어댑터를 단위 테스트 대신 통합 테스트로 하는 것은 의미가 있다. 

6장(Implementin a Persistence Adapter)에서 만들었던 영속성 어댑터를 테스트하고 싶다. 어댑터는 두개의 메서드를 가지고 있다. 하나는 Account 엔티티를 로딩하는 것이며 다른 하나는 데이터베이스에 새 계좌활동을 저장하는 것이다.

```java
@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

	@Autowired
	private AccountPersistenceAdapter adapterUnderTest;

	@Autowired
	private ActivityRepository activityRepository;

	@Test
	@Sql("AccountPersistenceAdapterTest.sql")
	void loadsAccount() {
		Account account = adapterUnderTest.loadAccount(new AccountId(1L), LocalDateTime.of(2018, 8, 10, 0, 0));

		assertThat(account.getActivityWindow().getActivities()).hasSize(2);
		assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
	}

	@Test
	void updatesActivities() {
		Account account = defaultAccount()
				.withBaselineBalance(Money.of(555L))
				.withActivityWindow(new ActivityWindow(
						defaultActivity()
								.withId(null)
								.withMoney(Money.of(1L)).build()))
				.build();

		adapterUnderTest.updateActivities(account);

		assertThat(activityRepository.count()).isEqualTo(1);

		ActivityJpaEntity savedActivity = activityRepository.findAll().get(0);
		assertThat(savedActivity.getAmount()).isEqualTo(1L);
	}

}
```

@DataJpaTest로 스프링이 데이터베이스에 연결된 Spring Data 리포지토리를 포함한 데이터베이스 엑세스가 필요한 객체를 인스턴스 할 수 있다. 네트워크에 추가되는 어떤 객체를 확인하기 위해 @Import를 사용할 수도 있다. 이 객체는 테스트에서 어댑터가 내부 도메인 객체를 데이터베이스 객체로 매핑할 때 필요하다. 

loadAccount() 테스트에서 데이터베이스를 SQL 스크립트를 이용해 특정 상태로 둘 수 있다. 그리고 나서 어댑터 API로 계좌를 로드하고 SQL 스크립트로 실행했던 데이터베이스 상태를 가졌는지 확인한다.

updateActivities()에 대한 테스트는 반대로 진행된다. 새로운 계좌 확동과 어댑터가 저장하게 하는 Account 객체를 만든다. 그리고 나서 액티비티가 ActivityRepository API를 통해 데이터베이스에 저장되었는지 확인한다.

이 테스트의 중요 관점은 데이터베이스를 mocking 하고 있지 않는다는 것이다. 테스트는 실제 데이터베이스에 연결한다. 데이터베이스를 mocking하면 테스트는 여전히 동일 코드를 포함할 것이며 코드 상 동일한 커버리지의 라인수를 만들어 낼 것이다. 그러나 높은 커버리지에 불과하고도 테스트는 SQL 구문상 오류 혹은 데이터베이스 테이블과 java 객체사이 매핑 오류 등 실제 데이터베이스의 준비과정에서 실패할 가능성이 있다.

기본적으로 스프링은 테스트 동안 메모리 데이터베이스를 사용한다. 이것은 매우 실용적이고 구성할 필요도 없고 독립적으로 동작할 것이다.

인메모리 데이터베이스가 운영에서 사용하는 데이터베이스는 아니기 때문에 테스트가 인메모리에서 잘 동작했지만 실제로는 오동작할 가능성이 여전히 있다. 데이터베이스는 자신의 선호하는 SQL을 구현하는 것이 좋다.

이런 이유로 영속성 어댑터 테스트는 실제 데이터베이스에서 실행되어야 한다. Testcontainers와 같은 라이브러리는 필요 시 Docker Container를 사용하는 등 이런 관점에서 많은 도움이 된다.

실제 데이터베이스에서 실행하는 것은 두개의 다른 데이터베이스를 신경쓸 필요가 없다는 부가적인 혜택이 있다. 테스트 동안 인메모리 데이터베이스를 사용한다면 어떤 방식으로든 구성해야 할지도 모르거나 각 데이터베이스의 마이그레이션 스크립트를 만들어야 할지도 모른다.



## Testing Main Paths with System Tests

시스템 테스트인 프라미드 젤 윗부분에, 시스템 테스트가 전체 애플리케이션을 시작하고 API에 요청을 실행하고 레이어에서 작동하는지 확인한다.

"Send Money" 유스케이스상의 시스템 테스트에서는 HTTP 요청을 애플리케이션에 보내고 계좌의 새 잔고를 응답으로서 받는다.

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LoadAccountPort loadAccountPort;

	@Test
	@Sql("SendMoneySystemTest.sql")
	void sendMoney() {

		Money initialSourceBalance = sourceAccount().calculateBalance();
		Money initialTargetBalance = targetAccount().calculateBalance();

		ResponseEntity response = whenSendMoney(
				sourceAccountId(),
				targetAccountId(),
				transferredAmount());

		then(response.getStatusCode())
				.isEqualTo(HttpStatus.OK);

		then(sourceAccount().calculateBalance())
				.isEqualTo(initialSourceBalance.minus(transferredAmount()));

		then(targetAccount().calculateBalance())
				.isEqualTo(initialTargetBalance.plus(transferredAmount()));

	}

	private ResponseEntity whenSendMoney(
			AccountId sourceAccountId,
			AccountId targetAccountId,
			Money amount) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<Void> request = new HttpEntity<>(null, headers);

		return restTemplate.exchange(
				"/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
				HttpMethod.POST,
				request,
				Object.class,
				sourceAccountId.getValue(),
				targetAccountId.getValue(),
				amount.getAmount());
	}
  
    // some helper methods omitted
}
```

@SpringBootTest로 스프링이 애플리케이션을 구성하는 전체 객체를 시작할 수 있다. 임의의 포트로 애플리케이션을 구성할 수 있다.

테스트 메소드에서 요청을 만들고 애플리케이션에 보내고 나서 응답값과 계좌의 잔고를 확인한다.

요청을 보내기 위해 이전 웹 어댑터 테스트에서 했던 MockMvc가 아닌 TestRestTemplate를 사용하고 있다. 이것은 운영환경과 좀 더 가깝게 테스트하도록 하는 실제 HTTP를 사용하고 있다는 것을 의미한다.

실제 HTTP로 진행하고 있으므로 실제 출력 어댑터를 가지고 있다. 우리의 케이스에서 이것은 데이터베이스에 애플리케이션을 연결하는 영속성 어댑터이다. 다른 시스템에 연결하는 애플리케이션에서 추가적인 출력 어댑터를 가졌을 것이다. 써드파티 시스템을 가지는건 시스템 테스트에서 손쉬운 일은 아니라서 결국 mocking 해야할지도 모른다. 헥사고날 아키텍처는 몇개의 출력포트 인터페이스를 <u>stub out</u> 해야하므로 더욱 쉽게 만들 수 있다.

테스트는 가능한 읽기 쉽게 만들어야 한다고 생각된다. 모든 복잡한 로직은 헬퍼 메소드로 숨겼다. 이 메소드는 상태를 확인할 수 있는 도메인 전용 언어를 구성한다.

이 같은 도메인 전용 언어가 테스트 관점에서는 좋은 생각이지만 시스템 테스트에서는 더욱 중요하다. 시스템 테스트는 애플리케이션의 실제 사용자가 단위 혹은 통합 테스트가 할 수 있는 것 보다 더 훌륭하게 작동한다. 그래서 사용자 관점으로부터 애플리케이션을 확인할 수 있게 한다. 이것은 적절한 단어로 사용하면 더 좋다. 이 단어는 프러그래머가 아닌 애플리케이션 사용자를 포함하기에 적절한 도메인 전문자가 테스트를 수행하고 피드백을 주기에 해준다. 행위-기반 개발에는 JGiven이라는 라이브러리가 있다. 이것은 테스트의 단어를 생성하는 프레임워크를 제공한다.

이전장에서 나타낸 단위, 통합 테스트를 만들었다면 시스템 테스트는 동일 코드를 포함할 것이다. 그들은 부가적인 혜택을 제공하는가? 그렇다. 일반적으로 단위와 통합테스트가 하는 것보다 다른 유형의 버그를 없앤다. 레이어 사이의 매핑은 단위, 통합테스트 단독으로는 알지 못하는 <u>밝혀지지 않을 수 있다.</u> 

시스템 테스트는 시나리오를 만들기 위해 다양한 유스케이스와 결합된다면 더욱 강력함을 발휘한다. 각 시나리오는 애플리케이션을 관통하는 특정 경로는 나타낸다. 가장 중요한 시나리오가 시스템 테스트에서 수행된다면 가장 최근 수정사항에서 애플리케이션이 정상적이었다고 확인할 수 있고 배포할 준비가 되었다고 할 수 있다.



## How Much Testing is Enough?

많은 프로젝트에서의 질문 사항은 얼마나 많은 테스트를 수행해야 하는가에 대답은 할 수 없었다. 테스트가 80% 라인 커버리지라면 충분한가? 그것보다 높아야 하나?

라인 커버리지는 테스트 성공을 측정하기에 나쁜 매트릭이다. 100%가 아닌 목표는 코드의 중요부분이 전혀 커버되지 않았을 수 있기 때문에 의미가 없을 수도 있다. 심지어 100%라도 모든 버그가 없다라고 확신할 수 없다.

소프트웨어를 출시하기에 얼마나 편한가에 의해 테스트 성공을 측정하기를 제안한다. 그들 모두 실행한 다음 출시하기에 충분히 신뢰한다면 좋다. 출시를 더 자주하면 테스트에서더 많이 신뢰할 수 있다. 단지 일년에 두번 출시한다면 일년에 두번만 확인하기 때문에 그 누구도 테스트를 신뢰할 수 없을 것이다.

처음 출시 두번째까지는 믿을이 필요하고 운영상 버그를 수정하는 우선순위를 둔다면 잘하고 있는 것이다.

매 운영 버그에서는 의문을 가져야 한다. 우리 테스트가 버그를 왜 잡지 못하는걸까? 답을 문서화 하고 버그를 커버하는 테스트를 추가한다. 시간이 경과함에 따라 출시할 때 더 편해지고 시간이 지남에 따라 문서화는 성공으로의 매트릭을 제공할 것이다.

그러나 우리가 만들어야 하는 테스트를 정의하는 전략을 시작하도록 돕는다. 이것은 헥사고날 아키텍처의 그런 전략중 하나다.

* 도메인 엔티티를 구현 시 단위 테스트를 구현하라.
* 유스케이스를 구현 시 단위 테스트를 구현하라.
* 어댑터를 구현 시 통합테스트를 구현하라.
* 사용자의 애플리케이션중 가장 중요부분을 시스템 테스트로 구현하라.
* "구현 시"이라는 단어를 주시하라. 테스트가 피처 개발 후가 아닌 개발동안 작성될때 개발 도구가 되고 하기 싫은일처럼 느끼지 않게 된다.
* 새로운 필드 추가할 때마다 테스트를 수정하는데 한시간씩 보내야 한다면 잘못하고 있는 것이다. 아마도 테스트는 코드의 구조적 변화에 너무 깨지기 쉽고 개선방안을 마련해야 한다. 리팩토링때마다 테스트를 수정해야 한다면 그 가치를 잃어버리는 것이다.

## How does This Help Me Build Maintainable Software?

헥사고날 아키텍처 스타일은 분명하게 도메인 로직과 외부 어댑터를 구분한다. 중앙 도메인 로직을 단위 테스트로 커버하고 어댑터를 통합 테스트로 커버하는데 분명한 테스트 전략을 정의한다.

입력과 출력 포트는 테스트에서 매우 가시적인 mocking 지점을 제공한다. 포트가 매우 작고 집중되고 있다면 하기 싫은 일 대신 식은죽 먹기 일수 있다. 포트 인터페이스가 더 적은 메소드를 제공할 수록 테스트에서 mocking해야하는 메소드에 대한 혼란도 줄어든다.

코드에서 어떤 부분을 커버해야 할 테스트 종류를 모른다면 mock 해야 할 것들이 더욱 많아지고 이는 경고 표시이다. 이런 관점에서 테스트는 <u>canary로</u> 행동하는 부가적인 책임을 가지고 있다. 아키텍처 상 결함을 알려주며 유지보수가 용이한 코드로 수정하게 하는...



