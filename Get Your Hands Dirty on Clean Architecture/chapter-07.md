# 7. 아키텍처 구성요소 테스트

> > > > >  

많은 프로젝트에서 나는 자동화 테스트가 의문 투성이인 것을 목격했다. 위키에 복잡하게 문서화된 규칙때문에 모든 사람들이 눈에 보이는 대로 테스트를 작성한다. 하지만 팀의 테스트 전략에 대해서는 정확히 이야기할 수 있는 사람은 없다.

이번 장은 헥사고날 아키텍처의 테스팅 전략을 제공한다. 아키텍처의 구성요소에 대한 테스트 유형을 살펴볼 것이다.



## 테스트 피라미드

다음 그림에서 테스트 피라미터(*테스트 피라미드는 2009년 Mike Cohn의 책 "Succeeding with Agile"으로 거슬로 올라갈 수 있다*)에 대해 논의해보자. 이것은 얼마나 많은 테스트를 할 지 결정하고 어떤 목적으로 테스트를 해야 하는지 결정하는데 도움을 주는 메타포이다.



<img src="chapter-07.assets/image-20210112181803343.png" alt="image-20210112181803343" style="zoom:67%;" />

[그림 7.1] 테스트 피라미드에 따르면, 우리는 비용이 적은 테스트를 만들어야 하고 비용이 비싼 테스트는 줄여야 한다.



기본적인 내용은 간단히 만들 수 있고 유지보수가 용이하며, 빠르게 실행되고 안정적으로 구성되어진 잘게 분할한 테스트로 구성된 높은 커버리지를 가져야 한다는 것을 나타낸다. 이 내용이 기대한 대로 동작하는 단일 유닛(일반적으로 클래스)을 확인하는 단위 테스트이다.

일단 테스트가 여러 유닛, 유닛 간 경계, 아키텍처 경계, 혹은 시스템 경계를 결합한다면 작성이 어려워지고 실행도 느리며 쉽게 깨진다(기능 오류가 아닌 구성 에러로 인한 실패). 피라미드에서는 그러한 테스트 작성 비용이 높아질 수록 목표 테스트 커버리지는 더 낮춰야 한다고 말해준다. 그렇지 않으면 새 기능 개발 대신 테스트 작성에 훨씬 많은 시간이 소요될 수 있기 때문이다. 

상황에 따라서는 테스트 피라미드가 다른 계층에서도 나타난다. 헥사고날 아키텍처를 테스트하기로 한 계층을 한번 보자. "단위 테스트", "통합 테스트", "시스템 테스트"의 정의는 상황에 따라 다르다는 것을 명심하자. 특정 프로젝트에서는 다른 프로젝트의 의미와 다를 수도 있다. 다음 내용은 이 장에서 사용되는 이러한 용어들에 대한 해석이다.

단위 테스트는 피라미드 상 가장 밑부분이다. 일반적으로 단위 테스트는 단일 클래스를 인스턴스화 하고 그 기능을 인터페이스를 통해서 테스트한다. 테스트 대상 클래스는 인스턴스가 생성되지 않는 다른 클래스를 의존하지만 테스트 하는 동안 실제 클래스 동작을 시뮬레이션 하는 mock으로 교체된다.

통합 테스트는 피라미드의 다음 단계를 나타낸다. 이 테스트는 네트워크를 연결하는 여러 단위를 인스턴스화 하고 진입 클래스의 인터페이스에 특정 데이터를 보냈을 때 기대한 대로 동작하는지 확인한다. 우리가 해석한 바에 의하면, 통합 테스트는 두 계층 사이 경계를 넘나들고 객체 간의 네트웍 연결은 불안정할 수도 있고 어떤 경우에는 mock으로 작업해야 한다.

마지막으로, 시스템 테스트는 애플리케이션을 구성하는 전체 네트웍 객체를 연결하고 특정 유스케이스가 애플리케이션 모든 계층을 통해 기대한 대로 동작하는지 확인한다.

위의 시스템 테스트는, 애플리케이션의 UI를 포함하는 엔드 투 엔드(end-to-end) 테스트 계층일 수도 있다. 이 책에서는 백엔드 아키텍처에 대해서만 논의하고 있으므로 엔드 투 엔드 테스트는 고려하지 않을 것이다.

이제 테스트 타입을 정의했고 어느 유형의 테스트가 헥사고날 아키텍처 계층에서 가장 적당한지 알아보자.



## 단위 테스트로 도메인 엔티티 테스트하기

아키텍처 내부 도메인 엔티티부터 시작해보자. *4장 (Implementing a Use Case)* 의 **Account** 를 다시 생각해보자. **Account**의 상태는 계좌의 특정 시점의 잔고, 입금 목록, 인출 목록 등으로 구성되어 있다. 이제 우리는 **withDraw()** 메서드가 기대한 대로 동작하는지 확인해보자.

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

이 테스트는 **Account**를 특정 상태로 인스턴스화 하는 단순 단위 테스트이고 **withdraw()**를 호출하여 인출이 성공했고 테스트 상 **Account** 객체 상태에 부작용이 없는지 확인한다.

이 테스트는 준비가 쉽고 이해하기 쉬우며 매우 빠르게 실행된다. 이 보다 더 단순한 테스트는 없을 것이다. 이와 같이 단위 테스트는 도메인 엔티티 내에 비즈니스 규칙을 확인하는 가장 좋은 방법이다. 도메인 엔티티 동작이 다른 클래스로의 의존이 없기 때문에 다른 유형의 테스트가 필요 없다.



## 단위 테스트로 유스케이스 테스트하기

계층 외부로 나가서, 다음 테스트 할 아키텍처 요소는 유스케이스이다. *4장(Implementing a Use Case)*에서 논의했던 **SendMoneyService** 테스트를 한번 보자. **SendMoney** 유스케이스는 다른 트랜잭션이 잔고를 잠시동안 변경할 수 없게 하기 위해 보내는 사람 **Account**를 잠근다. 성공적으로 보내는 사람 계좌에서 돈을 인출했다면, 돈을 입금하기 위해서 받는 사람 계좌를 잠근다. 그러고 나서 두 계좌의 잠금을 풀 수 있다.

트랜잭션이 성공할 때 기대한 대로 진행 됬는지 확인할 수 있다.

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

테스트 코드의 가독성을 높이기 위해서, 행위 주도(behavior-driven) 개발에서 주로 사용되는 given/when/then 부분으로 구조화 되어 있다.

"given" 영역에서 보내는 사람과 받는 사람 **Account** 객체를 생성하고 **given**...()으로 시작하는 이름의 메서드로 유효한 상태로 만든다. 또한 유스케이스의 입력값으로 사용되는 **SendMoneyCommand** 객체를 생성한다. "when" 부분에서, 유스케이스의 실행을 위해서 **sendMoney()** 메서드를 호출한다. "then" 부분에서 트랜잭션이 성공했고 특정 메서드가 보내는 사람/받는 사람 **Account** 계좌와 lock/unlock의 책임이 있는 **AccountLock** 객체에서 호출되었다는 것을 확인한다.

내부적으로는, 테스트가 **given...()** 에서 mock 객체를 생성하기 위해 Mockito 라이브러리(https://site.mockito.org/)를 사용한다. Mockito는 특정 메서드가 mock 객체에서 호출되었는지 확인하기 위해서 **then()** 메서드를 제공한다.

테스트 상에 있는 유스케이스 서비스는 상태가 없기(stateless) 때문에 "then" 구문에서 상태를 확인할 수 없다. 대신 테스트는 서비스가 의존관계에 있는 어떤 메서드를 호출했는지 확인한다. 이것은 테스트가 행위 뿐만 아니라 테스트 코드의 구조 변화에 취약하다는 것을 의미한다. 차례로 테스트 대상 코드가 리팩토링 될 때 테스트도 수정될 가능성이 많다는 것을 의미한다.

이러한 내용을 숙지하고, 테스트에서 확인하고자 하는 상호작용에 대해서 더 많이 고민해야 한다. 이전 테스트에서 했던 모든 동작을 확인하는 것이 반드시 좋은 생각은 아닐 것이다. 대신 중요한 부분에 집중해야 한다. 그렇지 않으면 하나의 변경사항만 있는 테스트를 테스트의 가치를 저하시키는 테스트 클래스로 변경해야만 한다.

이러한 테스트가 여전히 단위 테스트이지만 의존관계의 연관성을 테스트하고 있기 때문에 통합 테스트에 가깝다. 하지만, mock으로 작업하고 실제 의존성을 관리할 필요가 없기 때문에 완전체의 통합테스트를 생성 및 유지하는 것은 더 쉽다.



## 통합 테스트로 웹 어댑터 테스트하기

한층 더 외부로 나가면, 어댑터가 나온다. 웹 어댑터 테스트를 알아보자.

웹 어댑터는 입력값을 JSON 형식의 HTTP로 받고 어떤 검증을 한 다음 입력값을 유스케이스 형식으로 매핑하고 나서 유스케이스로 결과값을 반환한다는 것을 생각해보자. 그리고 나서, 유스케이스 결과를 JSON으로 매핑하고 HTTP 응답을 통해 클라이언트에 반환한다.

웹 어댑터 테스트에서 모든 단계가 기대한 대로 동작하는지 확인한다.

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

위의 테스트는 Spring Boot 프레임워크에서 만든 **SendMoneyController** 웹 컨트롤러를 위한 표준 통합 테스트이다. **testSendMoney()** 메서드에서 입력 객체를 생성하고 mock HTTP 요청을 웹 컨트롤러에 보낸다. 요청 본문은 JSON 문자열로 입력 객체를 포함한다.

**isOk()** 메서드로, HTTP 응답 상태가 200인 것을 확인하고 mock 유스케이스가 호출된 것을 확인한다.

웹 어댑터의 대부분의 책임이 이 테스트에 포함된다.

**MockMvc** 객체로 mocking하고 있기 때문에 HTTP 프로토콜로 실제 테스트는 하지 않는다. 우리는 프레임워크가 HTTP를 적절하게 변환한다고 믿는다; 프레임워크를 테스트 할 필요는 없다.

하지만, JSON형식의 입력을 **SendMoneyCommand** 객체로 매핑하는 전체 경로가 포함된다. *4장, Implementing a Use Case*에서 설명한 것처럼 **SendMoneyCommand** 객체를 자기 검증 command로 만들었다면, 우리는 이러한 매핑이 구문상으로 유스케이스에 대한 유효한 입력이라고 확신했을 것이다. 또한 유스케이스가 실제 호출되고 HTTP 응답이 기대 결과를 가진다고 확인했을 것이다.

그러면, 이것이 왜 통합테스트이고 단위 테스트가 아닌 것인가? 이 테스트에서 단일 웹 컨트롤러 클래스만을 테스트하는 것처럼 보일지라도 포함되는 많은 것들이 있다. **@WebMvcTest** 어노테이션으로, 우리는 Spring으로 하여금 특정 request path, 즉 Java와 JSON의 매핑, HTTP 입력값 검증 등을 하는 네트웍 객체를 인스턴스화하게 한다. 이 테스트에서 우리는 웹 컨트롤러가 네트워크의 일부분인 것처럼 동작하는 것을 확인하고 있다.

웹 컨트롤러가 Spring 프레임워크에 밀접하게 연결되어 있기 때문에, 분리하지 않고 이 프레임워크에 통합하여 테스트하는 것은 의미가 있다. 웹 컨트롤러를 단순 단위 테스트로 테스트했다면 모든 매핑, 입력값 검증, HTTP 같은 테스트는 포함하지 못할 것이며 운영상에 실제 동작할지 확인할 수 없을 것이다. 



## 통합 테스트로 영속성 어댑터 테스트하기

비슷한 이유로 어댑터 내부 로직만 뿐만 아니라 데이터베이스와의 매핑도 확인하고 싶기 때문에 영속성 어댑터를 단위 테스트 대신 통합 테스트로 하는 것은 의미 있는 일이다.

*6장(Implementin a Persistence Adapter)*에서 만들었던 영속성 어댑터를 테스트 해 볼 것이다. 어댑터는 두개의 메서드를 가지고 있다. 하나는 데이터베이스에서 **Account** 엔티티를 가져오는 것이며 다른 하나는 데이터베이스에 새 계좌 활동내역을 저장하는 것이다.

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

우리는 **@DataJpaTest** 구문으로 Spring에게 Spring Data 리포지토리를 포함한 데이터베이스 접근이 필요한 네트워크 객체를 인스턴스화 하라고 한다. 어떤 객체가 네트워크에 추가된다는 것을 확인하게 위해 **@Import** 구문을 추가한다. 예를 들어, 이러한 객체는 테스트 대상 어댑터가 내부 도메인 객체를 데이터베이스 객체로 매핑할 때 필요하다. 

**loadAccount()** 테스트에서, 데이터베이스를 SQL 스크립트를 이용해 특정 상태로 둔다. 그리고 나서 어댑터 API로 계좌를 로드하고 SQL 스크립트로 실행했던 상태인 지를 확인한다.

**updateActivities()** 테스트는 반대로 진행된다. 새로운 계좌 활동을 가진 Account 객체를 만들고 어댑터가 저장하게 한다. 그리고 나서 활동 내역이 **ActivityRepository** API를 통해 데이터베이스에 저장되었는지 확인한다.

이 테스트의 중요한 관점은 데이터베이스를 mocking 하고 있지 않는다는 것이다. 테스트는 실제 데이터베이스에 연결한다. 데이터베이스를 mocking하면 테스트는 여전히 동일 코드 라인을 커버할 것이며 코드 상 동일한 높은 커버리지 라인수를 만들어 낼 것이다. 그러나 이러한 높은 커버리지에도 불구하고, 테스트는 SQL 구문 오류 혹은 데이터베이스 테이블과 Java 객체 사이 예상치 못한 매핑 오류로 인해서 실제 데이터베이스에 연결과정에서 실패할 가능성이 높다.

기본적으로 Spring은 테스트 동안 인메모리 데이터베이스를 사용한다는 것을 명심해라. 이떤 것도 구성할 필요가 없고 실용적이고 독립적으로 동작할 것이다.

그러나, 인메모리 데이터베이스는 아마도 운영에서 사용하는 데이터베이스가 아니기 때문에, 테스트가 인메모리에서 잘 동작했더라도 실제 데이터베이스에서는 오동작할 가능성이 여전히 있다. 예를 들어, 각 데이터베이스는 자신만의 SQL 스타일이 있다.

이런 이유로, 영속성 어댑터 테스트는 실제 데이터베이스에서 실행되어야 한다. Testcontainers(https://www.tstcontainers.org/)와 같은 라이브러리는 필요 시 Docker Container를 사용하는 등 이런 관점에서 많은 도움이 된다.

실제 데이터베이스에서 실행하는 것은 우리가 두개의 다른 데이터베이스 모두를 신경 쓸 필요가 없는 부가적인 혜택을 가진다. 테스트 동안 인메모리 데이터베이스를 사용한다면, 어떤 방식으로든 그것을 구성해야 하거나 혹은 데이터베이스 개별 버전의 마이그레이션 스크립트를 만들어야 할지도 모른다.



## 시스템 테스트로 메인 경로 테스트하기

피라미드 제일 윗부분에 있는 시스템 테스트는 전체 애플리케이션을 시작하고 모든 계층이 잘 동작하는지 확인하는 API를 통해 요청을 실행한다.

"Send Money" 유스케이스에 대한 시스템 테스트에서, 우리는 HTTP 요청을 애플리케이션에 보내고 계좌의 새로운 잔고 뿐만 아니라 응답값을 검증한다.

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

**@SpringBootTest**로 우리는 Spring에게 애플리케이션을 구성하는 전체 네트워크 객체를 시작하라고 한다. 우리는 또한 임의의 포트로 애플리케이션을 구성할 수 있다.

**test** 메서드에서, 우리는 단순히 요청을 생성하고, 애플리케이션에 보내고, 그리고 나서 응답값과 계좌의 잔고를 확인한다.

이전 웹 어댑터 테스트에서 했던 것 처럼, 우리는 요청을 보내기 위해 **MockMvc**가 아닌 **TestRestTemplate**를 사용하고 있다. 이것은 운영 환경과 좀 더 가깝게 테스트하도록 하는 실제 HTTP를 사용하고 있다는 것을 의미한다.

실제 HTTP로 진행하고 있으므로 실제 출력 어댑터를 가지고 있다. 우리 경우에 이것은 애플리케이션을 데이터베이스에 연결하는 단순한 영속성 어댑터이다. 다른 시스템과 통신하는 애플리케이션에서, 우리는 추가적인 출력 어댑터를 놓을 수 있을 것이다. 시스템 테스트에서 써드파티 시스템을 동작하게 하는 것은 쉬운 일은 아니다. 그래서 결국 mocking 해야할 것이다. 우리의 헥사고날 아키텍처에서는 몇 개의 출력포트 인터페이스만 스텁(stub)으로 만들면 되므로 더욱 쉽게 할수 있다.

테스트는 가능한 읽기 쉽게 만들어야 한다고 생각된다. 모든 복잡한 로직은 헬퍼 메서드로 숨겼다. 이러한 메서드들은 상태 확인에 사용할 수 있는 도메인 특화 언어(Domain Specific Language)를 구성한다.

이와 같은 도메인 특화 언어가 어떤 유형의 테스트에서도 중요하겠지만, 시스템 테스트에서는 훨씬 더 중요하다. 시스템 테스트는 단위 혹은 통합 테스트보다 애플리케이션의 실제 사용자의 시뮬레이션을 훨씬 더 잘한다. 그래서 우리는 애플리케이션을 사용자 관점에서 확인할 수 있다. 적절한 단어로 사용하면 더 쉬워진다. 또한 이런 단어는 프로그래머가 아닌 애플리케이션 사용자 역할에 적합한 도메인 전문가에게 테스트를 수행하고 피드백 할 수 있게 해준다. 행위-기반 개발에는 JGiven(http://jgiven.org/)이라는 라이브러리가 있다. 이것은 테스트의 단어를 생성하는 프레임워크를 제공한다.

이전 장에서 나타냈듯이, 만일 우리가 단위, 통합 테스트를 만들었다면, 시스템 테스트는 동일한 많은 코드를 커버할 것이다. 시스템 테스트가 추가적인 혜택을 제공하는가? 그렇다. 일반적으로 단위, 통합 테스트가 하는 것과는 다른 유형의 버그를 없앤다. 예를 들어, 계층 간의 매핑은 단위, 통합테스트 단독실행으로는 발견되지 못하는 버그가 처리될 수 있다.

시나리오를 만들기 위해 다양한 유스케이스와 결합된다면, 시스템 테스트는 더욱 그 위력을 발휘한다. 각 시나리오는 사용자가 전형적으로 애플리케이션을 접하는 특정 경로를 나타낸다. 가장 중요한 시나리오가 시스템 테스트에 포함된다면, 우리는 가장 최근 수정사항에서 애플리케이션이 정상적이었고 배포할 준비가 되었다고 말할 수 있다.



## 얼마나 많은 테스트를 해야 할까?

내가 구성원이었던 많은 프로젝트 팀에서 결정할 수 없었던 질문은 얼마나 많은 테스트를 해야 하는가? 이다. 테스트 커버리지가 80%이면 충분한가? 그것보다 높아야 하나?

라인 커버리지는 테스트 성공을 측정하기에는 나쁜 매트릭이다. 100%가 아닌 목표는 코드상 중요한 부분이 전혀 커버되지 않았을 수 있으므로 전혀 의미가 없을 수도 있다. 심지어 100%라고 해도, 모든 버그가 없다라고 확신할 수 없다.

나는 테스트 성공에 대한 측정 방식을 우리가 소프트웨어를 출시하기에 얼마나 편하게 느끼는지로 판단하는 것을 추천한다. 테스트를 모두 실행한 다음, 제품을 출시할 만큼 충분히 테스트를 신뢰한다면, 출시해도 좋은 것이다. 출시를 더 자주하면, 테스트에서 더 많은 신뢰가 생긴다. 단지 일년에 두번 출시한다면 일년에 두번만 확인하기 때문에 그 누구도 테스트를 신뢰할 수 없을 것이다.

처음 두번 정도의 출시에는 무조건적인 믿음이 필요하다, 하지만 만일 우리가 그러한 것들을 운영상의 버그 수정 및 학습에 대한 우선순위로 둔다면, 우리는 잘하고 있는 것이다.

매번 운영 버그에서 우리는 의문을 가져야 한다. 우리 테스트가 버그를 왜 잡지 못했을까? 그 해결책을 문서화 하고 버그를 커버하는 테스트를 추가한다. 시간이 경과함에 따라 출시할 때 더 편해지고, 문서화 작업이 성공으로 가는 매트릭을 점차 제공할 것이다.

하지만, 우리가 만들 테스트 전략으로 시작할 수 있다. 헥사고날 아키텍처의 그런 전략이 이런 것이다.

* 도메인 엔티티 구현 시 단위 테스트로 커버하라
* 유스케이스를 구현 시 단위 테스트로 커버하라.
* 어댑터를 구현 시 통합테스트로 커버하라.
* 사용자의 애플리케이션 중 가장 중요한 부분을 시스템 테스트로 커버하라.

"구현 시"이라는 단어를 주목하라. 테스트가 기능 개발 후가 아닌 개발 중에 수행될 때, 그러한 것들은 개발 도구 중 하나가 되고 더 이상 하기 싫은 일처럼 느끼지 않게 된다.

그러나, 새 필드 추가 시 마다 테스트 수정에 한시간씩 보내야 한다면, <u>우리는 뭔가 잘못하고 있는 것이다</u>. 아마도, 우리 테스트는 코드의 구조적 변화에 너무 깨지기 쉽고 개선방안을 마련해야 한다. 우리는 리팩토링때 마다 테스트를 수정해야 한다면 테스트는 그 가치를 잃어버리는 것이다.



## How does This Help Me Build Maintainable Software?

**헥사고날 아키텍처** 스타일은 도메인 로직과 외부의 어댑터를 분명하게 구분한다. 내부 도메인 로직을 단위 테스트로 커버하고, 어댑터를 통합 테스트로 커버하는 것은 테스트 전략을 명확하게 정의하는데 도움을 준다.

입력과 출력 포트는 테스트에서 매우 가시적인 mocking 지점을 제공한다. 각 포트에서, 우리는 mocking해야 할지, 실제 구현을 사용해야 할지 결정할 수 있다. 포트가 매우 작고 특정 기능이 집중되어 있다면, 그것을 mocking하는 것은 매우 쉬운 일일 것이다. 포트 인터페이스가 더 적은 메서드를 제공할 수록 테스트에서 mocking 해야하는 메서드에 대한 혼란도 줄어든다.

어떤 것을 mocking 하는 것이 많은 부담이 되거나 코드의 어떤 부분을 커버해야 할지에 대한 테스트 종류를 모른다면, 이는 위험 신호이다. 이런 관점에서, 우리는 테스트는 카나리아(canary)로 동작해야 하는 부가적인 책임을 가지고 있다 - 아키텍처 상 결함을 알려주며 유지보수가 용이한 코드를 만드는 길로 안내한다.



