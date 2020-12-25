# 5 Implementing a Web Adapter

오늘날 대부분 애플리케이션 웹 인터페이스가 있다. (웹 브라우저를 경유하는 UI 혹은 다른 시스템에서 우리 애플리케이션을 호출하는 HTTP API)

타겟 아키텍처에서 외부 세계와의 모든 통신은 어댑터를 통한다. 웹 인터페이스와 같이 제공하는 어댑터를 구현하는 방법을 논의해보자.



## Dependency Inversion

다음 그림은 웹 어댑터에 대한 논의에 적절한 아키텍처 요소에 대한 확대 그림이다. 어댑터 그 자체와 애플리케이션 코어와 통신하는 포트들



<img src="chapter-05.assets/image-20201225220135079.png" alt="image-20201225220135079" style="zoom:67%;" />

[그림 5.1] 입력 어댑터는 애플리케이션 서비스에서 구현되는 전용 입력 포트를 통해 애플리케이션 레이어와 통신한다.

웹 어댑터는 <u>"driving"</u> 혹은 "입력" 어댑터이다. 그것은 외부로 부터 요청을 받아서 변환한 후 애플리케이션 내부를 호출한다. 제어 흐름은 컨트롤러로 부터 애플린케이션 레이어의 서비스로 흐른다.

애플리케이션 레이어는 웹 어댑터가 통신할 수 있는 특정 포트를 제공한다. 서비스는 이 포트를 구현하고 웹 어댑터를 이 포트를 호출할 수 있다.

좀 더 가깝게 보면 의존성 역전 원칙의 적용 대상이 되는 것을 확인할 수 있다. 제어 으름이 왼쪽에서 오른쪽으로 흐르기 때문에 웹 어댑터를 유스케이스를 직접 호출하게 할 수 있다.

<img src="chapter-05.assets/image-20201225220640810.png" alt="image-20201225220640810" style="zoom:67%;" />

[그림 5.2] 포트 인터페이스를 삭제하고 서비스를 직접 호출한다.

어댑터와 유스케이스 사이에 또 다른 간접적으로 레이어를 왜 추가해야할까? 포트들이 외부 세계에서 애플리케이션 내부와 통신할 수 있는 장소이기 때문이다. 적절한 장소에 포트를 가지고 있고 외부와의 통신이 어떻게 발생하는지 정확히 안다. <u>Which is valuable information for any...</u>

<u>Having said that,</u> 11장에서 이야기 할 Shortcuts 중 하나는 입력 포트를 남겨두고 애플리케이션 서비스를 직접 호출하도록 하는 것이다.

하나의 질문은 어떤 방식이 상호작용 방식에 적절한가이다. 웹소켓으로 웹브라우저로 실시간 정보를 보내는 애플리케이션을 생각해보라. 애플리케이션 내부가 이런 실시간 데이터를 어떻게 웹브라우저로 보내는 웹어댑터로 전송할까?

이 시나리오에서 분명히 포트가 필요하다. 아래 그림에서 묘사된 것처럼, 이 포트는 웹 어댑터에서 구현되어야 하고 애플리케이션 내붕에서 호출되어야 한다.

<img src="chapter-05.assets/image-20201225221657556.png" alt="image-20201225221657556" style="zoom:67%;" />

[그림 5.3] 애플리케이션이 웹 어댑터에게 알려야만 한다면, 직접적으로 의존을 가지는 외부 포트를 관통할 필요가 있다.

기술적으로 말하면 이것은 외부 포트가 될 것이며 웹 어댑터가 내/외부 어댑터가 된다. 동일한 어댑터가 동시에 양쪽에 있을 수 없다는 이유는 없다.

이 장의 나머지 부분에서 대부분 흔한 경우기 때문에 웹 어댑터가 입력 어댑터라고 가정할 것이다.



## Responsibilities of a Web Adapter

웹 어댑터가 실제로 무슨 일을 하는가? BuckPal 애플리케이션에서 REST API를 제공할 필요가 있다. 웹 어댑터의 책임은 어디에서 시작하고 어디서 끝나는가?

웹 어댑터는 일반적으로 이런 일들을 한다.

1. HTTP 요청을 java 객체로 매핑한다.
2. 인증 확인
3. 입력값 검증
4. 입력값을 유스케이스의 입력 모델로 매핑
5. 유스케이스 호출
6. 유스케이스의 출력값을 HTTP로 매핑
7. HTTP 응답값을 리턴

우선, 웹 어댑터는 어떤 기준, 특정 URL 경로, HTTP method, content-Type을 만족하는 HTTP 요청을 받아야 한다. 파라미터와 내용은 객체로 역직렬화되어야 한다.

일반적으로 웹 어댑터는 인증, 인가 확인을 하고 실패하면 오류를 리턴한다.

그리고 나서 내부 객체 상태가 확인된다. 유스케이스의 입력 모델에 대한 책임으로 입력값 검증에 대해 우린 이미 논의하지 않았는가? 그렇다. 유스케이스의 입력 모델 해당 컨텍스트상에서 유효한 입력값이어야 한다. 여기서 웹 어댑터에 대한 입력모델에 대해서 이야기 해보자. 그건은 완전히 다른 구조를 가질지도 모르고 유스케이스에 입력모델과는 다른 의미를 가질지도 모른다. 그래서 다른 입력값 검증을 수행해야 할지도 모른다.

유스케이스의 입력 모델에서 이미 했듯이 웹 어댑터의 동일한 입력값 확인을 수행하는 것을 권장하지는 않는다. 대신 웹 어댑터의 입력 모델을 유스케이스의 입력값 모델로 변환할 수 있다고 확인한다. 이 변환 작업이 안되면 모두 유효성 에러이다.

웹 어댑터의 다음 책임을 볼 차례다. 변환된 입력 모델의 유스케이스를 호출.어댑터는 유스케이스 출력을 나타내고 HTTP로 직렬화한다.

웹 어댑터에는 많은 책임이 있다. 그러나 웹 애플리케이션 레이어가 관심가져서는 안되는 많은 책임이 있다. HTTP와 관련된 어떤 것도 애플리케이션 레이어에 노출되어서는 안된다. 애플리케이션 내부가 외부에서 HTTP를 다룬다는 것을 안다면 HTTP를 사용하지 않는 다른 입력 어댑터가 도메인 로직을 수행하는 기회를 잃어버리는 것이다. 좋은 아키텍처에서는 선택사항을 항상 개방해 놓아야 한다.

웹 레이어 대신에 도메인과 애플리케이션 레이어 사이에 개발을 시작한다면 이러한 웹 어댑터와 애플리케이션 레이어 사이의 경계는 자연적으로 따라온다는 것을 명심해라. 입력 어댑터를 생각하지 않고 유스케이스를 먼저 구현한다면 경계를 <u>흐릿하게 하지 않는다.</u>



## Slicing Controllers

Spring MVC와 같은 대부분 웹 프레임워크에서 controller 클래스를 만든다. 애플리케이션에 직접 요청하는 모든 응답을 처리하는 단일 controller를 만드는가? 반드시 그렇게 할 필요는 없다. 웹 어댑터는 한개 이상의 클래스로 구성되어 있다.

그러나 3장(Organizing Code)에서 논의한 대로 동일한 패키지 구조에 놓아야만 한다.

그럼 몇개의 컨트롤러를 만들어야 하는가? 적은것 보다는 많은게 낫다라고 생각한다. 각 컨트롤러는 웹 어댑터를 구현한다.  

BuckPal 애플리케이션내의 account 엔티티를 한번 보자. 쉬운 접근법은 계좌의 모든 요청을 받는 단일 AccountController를 만드는 것이다. REST API를 제공하는 스프링 컨트롤러는 다음 코드의 형태가 된다.

```java
package buckpal.adapter.web;

@RestController
@RequiredArgsConstructor
class AccountController {
	private final GetAccountBalanceQuery getAccountBalanceQuery;
    private final ListAccountsQuery listAccountsQuery;
    private final LoadAccountQuery loadAccountQuery;
  
    private final SendMoneyUseCase sendMoneyUseCase;
    private final CreateAccountUseCase createAccountUseCase;
   
    @GetMapping("/accounts")
    List<AccountResource> listAccounts() {
        
    }
  
    @GetMapping("/accounts/id")
    AccountResource getAccount(@PathVariable("accountId") Long accoutId) {
        ...
    }
  
    @GetMapping("/accounts/{id}/balance")
    long getAccountBalance(@PathVariable("accountId") Long accoutId) {
        ...
    }
  
    @PostMapping("/accounts")
    AccountResource createAccount(@RequestBody AccountResource accout) {
        ...
    }
  
    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(
        @PathVariable("sourceAccountId") Long sourceAccountId,
        @PathVariable("targetAccountId") Long targetAccountId,
        @PathVariable("amount") Long amount,
    ) {
     	... 
    }
}
```

account 리소스에 관련된 모든것이 단일 클래스에 있다. 좋아 보이기는 하지만 이 접근법에 대한 불편한 점을 논의해보자.

우선, 클래스당 코드량이 적은게 좋다. 나는 3만 코드가 있는 대규모 클래스를 가진 레거시 프로젝트를 경험했다. 단일 클래스에 3만 라인이 있는 아키텍처이엇다. class 파일을 업로드 함으로써 재배포 하지 않고 런타임에 시스템을 변화시킨다.

웃자고 하자는 얘기는 아니다. 컨트롤러가 수년동안 200라인 이상 된가면, 메서드로 명백히 구분이 되어 있을지라도 50라인 이상 이해하기는 더욱 어려워진다.

테스트 코드에도 동일한 논쟁이 있다. 컨트롤러가 많은 코드가 있다면 테스트 코드도 많을 것이다. 테스트 코드는 더 추상화되는 경향이 있기 때문에 운영 코드보다 이해하기가 더 어렵다. 운영에 대한 테스트 케이스를 더 찾기 쉽게 하기 위해서 더 작은 클래스로 나눈다.

그러나 중요한 것은 모든 동작을 단일 컨트롤러로 넣는 것은 데이타 구조를 재사용하게 한다. 이전 코드 예에서 많은 동작은 AccountResource 모델 클래스를 공유한다. <u>It serves as a bucket for everything that is needed in any of the operations</u>. AccountResource는 id 필드가 있다. 생성에서는 필요가 없어서 혼란스러울 것이다. Account가 User 객체와 1대 다 관계를 가진다고 생각해보라. 예약을 생성/수정할 때 User 객체를 포함해야 할까? 사용자들은 list 오퍼레이션을 리턴받을까? 이것은 단순한 예이다. 

그래서 컨트롤러를 분리하여 생성하는 접근법을 선호한다.유스케이스와 가까운 메서드명과 클래스명을 사용한다. 

```java
@RestController
@RequiredArgsConstructor
class SendMoneyController {

	private final SendMoneyUseCase sendMoneyUseCase;

	@PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
	void sendMoney(
			@PathVariable("sourceAccountId") Long sourceAccountId,
			@PathVariable("targetAccountId") Long targetAccountId,
			@PathVariable("amount") Long amount) {

		SendMoneyCommand command = new SendMoneyCommand(
				new AccountId(sourceAccountId),
				new AccountId(targetAccountId),
				Money.of(amount));

		sendMoneyUseCase.sendMoney(command);
	}

}

```

또한 각 컨트롤러는 CreateAccountResource 혹은 UpdateAccountResource 같이 각각의 모델을 가지고 있거나 이전 예와 같이 입력으로 primitive를 사용할 수도 있다.

그런 전용 모델 클래스는 컨트롤러 패키지에서 다른 곳에서 재사용이 안되게 private이 될 수도 있다. 컨트롤러는 여전히 모델을 공유할지도 모르지만 공유 클래스를 사용한다. <u>..............</u>

또한 컨트롤러와 서비스의 이름에 대해서 열심히 생각해보자. 예를 들어, CreateAccount 대신에 RegisterAccount가 더 좋은 이름이 아닐까? BuckPal 애플리케이션에서 계좌 생성하는 방법은 사용자가 등록하는 것이다. "register"라는 단어가 의미를 전달하는데 더 좋은 단어이다. Create, Update, Delete를 사용하는 케이스가 있다. 사용하기 전에 다시 한번 생각해봐야 한다.

이렇게 분리된 스타일의 장점은 다른 오프레이션과 병렬 작업을 할 수 있게 한다. 두 개발자가 작업할 때 병합할 필요가 없다.



## How Does This Help Me Build Maintainable Software?

애플리케이션에 웹 어댑터 만들때, HTTP 메소드가 유스케이스를 호출하고 결과를 도메인 로직을 실행하지 않는 HTTP로 변환하는 어댑터를 만든다는 것을 명심해야 한다.

반면, 애플리케이션 레이어는 HTTP를 사용해서는 안되며 HTTP 세부사항을 노출해서는 안된다. 웹 어댑터가 필요 시 다른 어댑터로 대체할 수 있게 해준다.

웹 컨트롤러를 분리할 때 모델을 공유하지 않는 너무 많은 클래스를 만들지 않을까 걱정해서는 안된다. 이해하기 쉽고 테스트 및 병렬작업이 쉽다. 최초에 잘게 쪼갠 컨트롤러로 시작하면 유지보수 동안 그 효과를 발휘할 것이다.





























