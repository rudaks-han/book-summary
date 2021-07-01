# 6장 트우터

## 6.3 트우터 요구사항

* 사용자가 게시하는 각각의 마이크로블로그를 트웃(twoot)이라고 부르며 연속적인 트웃 스트림을 갖는다.
* 다른 사용자를 팔로우해야 그 사용자가 무엇을 트우팅했는지 알 수 있다.



* 서비스를 어떻게 사용할지 다양한 유스 케이스와 시나리오를 브레인스토밍했다.
* 시스템을 완성하기 위해 필요한 기능은 다음과 같다.
    * 고유의 사용자 ID와 비밀번호로 트우터에 로그인한다.
    * 각 사용자는 자신이 팔로우하는 사용자 집합을 갖는다.
    * 사용자는 트웃을 전송할 수 있으며 로그인한 모든 팔로워는 이 트웃을 바로 볼 수 있다.
    * 사용자가 로그인하면 최종 로그인한 이후로 게시된 팔로워의 모든 트웃을 볼 수 있다.
    * 사용자는 모바일이나 웹사이트로 로그인할 수 있다.



## 6.4 설계 개요

* 마지막 요구 사항을 달성하려면 많은 컴퓨터가 상호작용할 수 있는 시스템을 만들어야 한다.
* 어떤 사용자는 집에 있는 데스크톱으로 트우터 웹사이트에 접속할 수 있고 어떤 사용자는 스마트폰으로 트우터를 실행할 수 있기 때문이다. 
* 다양한 환경의 사용자가 어떻게 서로 통신할 수 있을까?



* 보통 소프트웨어 개발자는 이런 문제를 클라이언트 서버 모델로 해결한다.
* 클라이언트 그룹은 서비스를 사용하는 그룹이고 서버는 관련 서비스를 제공하는 그룹이다.



![image-20210630191703594](images/image-20210630191703594.png)



### 6.4.1 풀 기반

* 풀 기반(pull-based) 통신에서는 클라이언트가 서버로 정보를 요청한다.
* 풀 기반 통신은 점대점(point-to-point) 또는 요청 응답(request-response) 통신 형식으로도 불린다.
* 클라이언트는 서버로 HTTP 요청을 보내 페이지의 데이터를 가져온다(pull).



![image-20210630192044775](images/image-20210630192044775.png)



### 6.4.2 푸시 기반

* 푸시 기반 통신 방법도 있다. 
* 이를 리액티브 또는 이벤트 주도 통신이라고 부른다.
* 푸시 기반 모델에서는 작성자(publisher)가 방출한 이벤트 스트림을 여러 구독자가 수신한다.
* 주식 시장 거래 프로그램을 설계할 때 다양한 회사의 가격과 틱 정보가 필요할 때마다 요청하는 것이 아니라 연속적으로 정보가 제공되어야 한다.



![image-20210630192233010](images/image-20210630192233010.png)



## 6.5 이벤트에서 설계까지

### 6.5.1 통신

* 웹소켓은 TCP 스트림으로 양방향 이벤트 통신을 지원하는 가벼운 통신 프로토콜이다.
* 요즘은 아마존의 단순 큐 서비스(SQS) 같은 호스트된 클라우드 기반 메시지 큐를 메시지 송출이나 수신에 점점 많이 사용한다. 메시지 큐는 그룹 내의 프로세스 중 한 프로세스가 전송된 메시지를 받아 처리하는 상호 프로세스 통신 방식이다.
* 메시지 전송이나 메시지 큐를 구현하는 오픈 소스인 Aeron, ZeroMQ, AMPQ로 구현하는 방법도 있다.



* 프로젝트를 시작하면서 선택한 기술을 바꾸지 못하고 계속 사용해야 하는 상황이라면 이는 좋은 아키텍처 결정이 아님을 반증한다.
* 다양한 통신 방식을 혼합할 수도 있다.
* 웹사이트와는 웹소켓으로 통신하며 안드로이드 모바일 앱과는 푸시 노티피케이션으로 통신하는 그림이다.



![image-20210630192820783](images/image-20210630192820783.png)



### 6.5.2 GUI

* UI 통신 기술이나 UI를 서버의 비즈니스 로직과 결합하면 몇 가지 단점이 발생한다.
    * 테스트하기 어렵고 테스트 실행도 느려진다. 모든 테스트가 실행 중인 메인 서버로 이벤트를 발행하거나, 수신해야 하기 때문이다.
    * 2장에서 설명한 단일 책임 원칙을 위반한다.
    * 클라이언트가 반드시 UI를 갖는다고 가정한다. 처음에는 트우터 클라이언트가 당연히 UI를 가져야 할 것처럼 보이지만 미래에는 인공지능 챗봇이 사용자의 문제를 알아서 해결할 수도 있다. 문제를 해결할 수 없다면 챗봇이 귀여운 고양이 사진이라도 트웃해 사람들 기분이라도 풀어줄 것이다.
* 결론적으로 메시징을 코어 비즈니스 로직과 분리할 수 있도록 신중하게 추상화해야 한다.
* 즉 클라이언트에게 메시지를 전송하고 클라이언트의 메시지를 수신하는 인터페이스가 필요하다.



### 6.5.3 영구 저장

트우터는 수신한 데이터를 어떻게 저장할까?

* 직접 인덱스하고 검색할 수 있는 일반 텍스트 파일: 기록된 데이터를 쉽게 볼 수 있으며 다른 응용프로그램과의 디펜던시를 줄일 수 있다.
* 전통적인 SQL 데이터베이스: 모두가 알고 있으며 잘 검증된 시스템으로 강력한 질의를 지원한다.
* NoSQL 데이터베이스: 다양한 유스 케이스, 질의 언어, 데이터 저장 모델을 지원하는 여러 데이터베이스가 있다.



* 소프트웨어 프로젝트를 처음 시작하는 단계에서는 어떤 기술이 적합한지 선택하기가 어려울 뿐만 아니라 시간이 흐르면서 요구사항은 계속 바뀐다.
* 저장소 백엔드가 응용프로그램의 다른 부분과 결합하지 않도록 설계하고 싶다.
* 어떤 기능이 특정 기술과 결합하지 않도록 방지하는 일은 개발자가 흔히 고민하는 문제 중 하나다.



### 6.4.2 육각형 아키텍처

* 앨리스터 콕번(Alister Cockburn)이 정립한 포트와 어댑터(ports and adapters) 또는 육각형 아키텍처(hexagonal architecture)라 불리는 조금 더 일반화된 아키텍처를 적용해 이 문제를 해결할 수 있다.

* 코어 비즈니스 로직과 분리하려는 특정 기술이 있다면 포트를 이용한다. 
* 외부 이벤트는 포트를 통해 코어 비즈니스 로직으로 전달된다. 어댑터는 포트로 연결하는 특정 기술을 이용한 구현 코드다.
* UI 이벤트를 발행하고 수신하는 포트와 웹 브라우저와 통신하는 웹소켓 어댑터를 가질 수 있다.





![image-20210701064656482](images/image-20210701064656482.png)



* 이 아키텍처를 이용하려면 포트와 어댑터를 추상화해야 한다.
* 나중에 트우터에 노티피케이션 시스템을 확장할 계획도 있다.
    * 사용자가 관심이 있는 트웃을 로그인해서 볼 수 있도록 알리고 보여주는 기능은 포트에 해당한다. 이 기능을 이메일이나 텍스트 메시지의 어댑터로 구현할 수 있다.
* 인증 서비스에서도 포트를 활용할 수 있다.
    * 처음에는 사용자명과 비밀번호를 저장하는 어댑터를 구현하고 나중에는 이를 OAuth 백엔드나 다른 종류의 시스템으로 바꿀 수 있다.
* 어떤 기능을 포트로 지정하고 어떤 기능을 코어 도메인으로 분리해야 하는지 궁금해진다.
    * 정해진 규칙은 따로 없으므로 개인적인 판단과 환경에 따라 응용프로그램에 맞는 결정을 하면 된다.
* 비즈니스 문제를 해결하는 데 꼭 필요한 기능을 응용프로그램의 코어로 분류하고 나머지 특정 기술에 종속된 기능이나 통신 관련 기능은 코어 응용프로그램의 외부 세계로 분류하는 것이 일반적인 관례다.



## 6.6 작업 순서

* 5장에서 테스트 주도 개발을 소개했으니 테스트 클래스를 먼저 구현하는 것이 바람직하다는 걸 알고 있다.



```java
@Test
public void shouldBeAbleToAuthenticateUser() {
    // 유효 사용자의 로그온 메시지 수신
    
    // 로그온 메서드는 새 엔드포인트 반환
    
    // 엔드포인트 유효성을 확인하는 어서션
}
```

* 이미 포트로 아키텍처 결정과 UI통신을 분리했으니 API를 어떻게 정의할지 결정해야 한다.
* 사용자에게 이벤트를 발송하는 기능, 즉 한 사용자가 다른 사용자를 팔로우하고 있을 때 팔로우 대상 사용자가 느웃을 올렸다는 걸 알리는 기능이 필요하다.
* 포트와 어댑터의 목표는 응용프로그램의 코어와 특정 어댑터 구현의 결합을 제거하는 것이다.
* 예제에서는 이벤트를 코어로 보내는 SenderEndPoint 클래스와 코어로부터 이벤트를 수신하는 인터페이스를 ReceiverEndPoint라는 이름으로 정했다.



![image-20210701072702817](images/image-20210701072702817.png)



onLogon 초기 버전 시그니처

```java
SenderEndPoint onLogon(String userId, ReceiverEndPoint receiver);
```



* 보통 로그인이 성공하면 SenderEndPoint를 반환하고 그렇지 않으면 null을 반환하는 방법도 있다. 하지만 이 방법도 다음과 같은 문제가 있다.
    * 개발자가 null을 확인하지 않으면 NullPointerException이 발생할 수 있다. 이는 자바 개발자가 정말 흔히 저지르는 실수다.
    * 컴파일 타임에서는 이런 종류의 문제를 피할 수 있는 도움을 제공하지 않는다. 이 문제는 런타임에서만 발생하기 때문이다.
    * 메서드의 시그니처로는 로그인 실패 시 null을 의도적으로 반환하도록 설계된 것인지 아니면 단순히 버그인지 알 수 없다.
* Optional 데이터 형식을 사용해 더 자연스럽게 문제를 해결할 수 있다.

```java
Optional<SenderEndPoint> onLogon(String userId, ReceiverEndPoint receiver);
```



```java
@Test
public void shouldNotAuthenticateUserWithWrongPassword() {
    final Optional<SenderEndPoint> endPoint = twootr.onLogon(TestData.USER_ID, "bad password", receiverEndPoint);
    
    assertFalse(endPoint.isPresent());
}
```

* 간단하게 사용자 ID를 키로, 비밀번호를 값으로 Map<String, String>에 저장할 수 있다.
* 하지만 실전에는 사용자는 도메인의 중요한 개념이다.
* 다양한 시나리에서 사용자를 참조해야 하며, 다른 사용자와 통신하는 많은 시스템 기능에서 사용자가 필요하다.
* User라는 도메인 클래스가 필요한 이유다.
* TDD는 소프트웨어 설계를 방해한다는 비판을 종종 받는다.
    * TDD에서는 테스트 구현에 초점을 맞춘 나머지 빈약한 도메인 모델을 양산하고, 결국 어느 시점에 코드를 다시 구현해야 한다.
    * 빈약한 도메인 모델(anemic domain model)이란 비즈니스 로직을 갖지 않으며 다양한 메서드에 절차적 형식으로 흩어져 정의된 도메인 객체를 가리킨다.



## 6.7 비밀번호와 보안

* 트우터는 비밀번호를 저장해야 하므로 이 문제를 조금 살펴보자.
* 가장 간단한 방법은 일반 텍스트로 알려진 문자열 형태로 비밀번호를 저장하는것이다.
    * 보통 좋지 않은 방법으로 소문나 있는데, 이는 데이터베이스에 접근한 모든 이가 사용자의 비밀번호를 확인할 수 있기 때문이다.
    * 게다가 많은 사람들이 다양한 서비스에 같은 비밀번호를 사용한다.
* 비밀번호에 암호화 해시함수를 적용하면 데이터베이스에 접근한 사람이 비밀번호를 읽지 못하게 방지할 수 있다.
* 이는 임의의 길이의 문자열을 입력받아 다이제스트라는 출력으로 변환하는 기능이다.
* 암호화 해시 함수는 항상 같은 결과를 출력하므로 이 기능에 동일한 값을 입력하면, 이전과 같은 결과가 나온다.



* 해싱 함수는 아주 비싼 연산이긴 하지만 여전히 무차별 대입(brute force)으로 특정 길이 이내의 키를 맞추거나 레이보 테이블(rainbow table)로 해싱된 값을 되돌릴 수 있다는 점이 해싱 함수의 약점이다.
* 솔트(salt)로 이를 방지할 수 있다.
* 솔트란 암호 해싱 함수에 적용하는 임의로 생성된 추가 입력이다.

> 해싱과 솔트
>
> https://st-lab.tistory.com/100



* 전송되는 데이터의 보안도 신경 써야 한다.
* 가장 흔하면서 단순한 방법은 전송 계층 보안(Transport Layer Security), 즉 연결된 네트워크로 전달되는 데이터의 프라이버시와 무결성을 제공하는 암호화된 프로토콜을 사용하는 것이다.



## 6.8 팔로워와 트웃

* 이번에는 사용자 팔로우 기능을 살펴보자.
* 소프트웨어 설계를 크게 두 가지 방법으로 접근한다.
* 상향식 기법
    * 응용프로그램의 코어(데이터 저장 모델이나 코어 도메인 객체 간의 관계) 설계에서 시작해 시스템 전체를 만드는 방법이다.
    * 팔로우를 할 때 발생하는 사용자의 관계를 어떻게 모델링할지 결정해야 한다.
    * 사용자는 여러 사용자를 팔로우할 수 있고 한 사용자는 여러 팔로워를 가질 수 있으므로 다대다 관계가 성립한다.
    * 이렇게 정의한 데이터 모델 위에 사용자에게 필요한 기능을 제공하는 비즈니스 기능을 구현한다.
* 하향식 기법
    * 사용자 요구 사항이나 스토리에서 출발해 구현하는 데 필요한 동작이나 기능을 먼저 개발하고, 점차 저장소나 데이터 모델을 추가한다.
    * 예를 들어 다른 사용자를 팔로우하는 이벤트 수신 API를 만든 다음, 이 동작에 필요한 저장소를 설계한다.
    * 이렇게 API를 먼저 구현하고 저장 비즈니스 로직을 나중에 구현한다.
* 어떤 방법이 항상 옳다고 단정하긴 어렵지만 경험상 영업과 관련된 형식의 자바 응용프로그램에서는 하향식 기법이 주효했다.
    * 보통 소프트웨어의 데이터 모델이나 코어 도메인을 먼저 설계하면 실질적으로 소프트웨어의 동작에 필요 없는 부분까지 만들 수 있기 때문이다.
    * 반면 하향식 기법은 요구 사항과 스토리를 구현하면서 초기 버전의 설계 방식에 문제가 있음을 발견하게 된다는 단점이 있다.
    * 하향식 기법을 사용할 때 현재 설계에 안심하지 않고 반복적으로 개선하기 위해 노력해야 한다.
* 여기서는 하향식 기법을 사용한다.



### 6.8.1 오류 모델링

```java
@Test
public void shouldFollowValidUser() {
  logon();
  
  final FollowStatus followStatus = endPoint.onFollow(TestData.OTHER_USER_ID);
  
  assertEquals(SUCCESS, followStatus);
}
```

* 인수로 전달된 사용자 ID가 실제 사용자와 일치하지 않는다면?
* 사용자가 이미 팔로우하고 있는 사용자를 다시 팔로우하려 한다면?



* 성공했을 때는 아무것도 반환하지 않고(void), 이외에는 예외를 던지는 방법이 있다.
    * 나쁘지 않은 선택이지만 UI에서는 웬만해서 이런 시나리오를 피한다.
* 불리언으로 성공일 때는 true, 실패일 때는 false로 표현하는 간단한 방법도 있다.
    * 여러 가지 이유로 동작이 실패할 수 있는 상황에서 왜 이 동작이 실패했는지 알려줄 수 없다는 것이 불리언의 단점이다.
* Int 상숫값으로 표현하는 방법도 있다.
    * Int 상수값은 그 자체에 오류가 발생할 수 있고, 안전한 형식을 제공하지 못하며, 가독성과 유지보수성도 낮아진다.
* enum이 int기반의 상태 코드보다 여러모로 낫다.



FollowStatus

```java
public enum FollowStatus {
  SUCCESS,
  INVALID_USER,
  ALREADY_FOLLOWING
}
```

```java
@Test
public void shouldNotDuplicateFollowValidUser() {
  logon();
  
  endPoint.onFollow(TestData.OTHER_USER_ID);
  
  final FollowStatus followStatus = endPoint.onFollow(TestData.OTHER_USER_ID);
  assertEquals(ALREADY_FOLLOWING, followStatus);
}
```

shouldNotFollowInValidUser() 테스트는 사용자가 유효하지 않음을 가정하며 결과 상태로 이를 확인할 수 있다.



### 6.8.2 트우팅

* 트웃 전송 기능이 필요하므로 SenderEndPoint에 onSendTwoot() 메서드를 추가한다.
* 또한 사용자가 트웃을 게시했음을 팔로워에게 알려야 한다.

```java
public interface ReceiverEndPoint {
  void onTwoot(Twoot twoot);
}
```



### 6.8.3 목 만들기

* 목 객체 개념을 이용해 쉽게 문제를 해결할 수 있다.
* 목 객체는 다른 객체인 척하는 객체다.
* 목 객체는 원래 객체가 제공하는 메서드와 공개 API를 모두 제공한다.



```java
public class MockReceiverEndPoint implements ReceiverEndPoint {
  private final List<Twoot> receivedTwoots = new ArrayList<>();
  
  @Override
  public void onTwoot(final Twoot twoot) {
    receivedTwoots.add(twoot);
  }
  
  public void verifyOnTwoot(final Twoot twoot) {
    assertThat(receivedTwoots, contains(twoot));
  }
}
```

* 모키토 관련 대부분의 기능은 Mockito 클래스에서 제공하는 정적 메서드로 제공하므로 이를 정적 임포트해서 사용한다.

```java
private final ReceiverEndPoint receiverEndPoint = mock(ReceiverEndPoint.class);
```



### 6.8.4 목으로 확인하기

```java
verify(receiverEndPoint).onTwoot(aTwootObject);
```



### 6.8.5 모킹 라이브러리

* 이 책에서는 모키토 라이브러리를 사용했지만, 다른 자바 모킹 프레임워크도 있다.
* 파워목이나 이지목 모두 유명한 프레임워크다.
* 파워목
    * 모키토 문법을 그대로 지원하며 모키토가 지원하지 않는 목 기능(예를 들어 final 클래스나 정적 메서드)도 지원한다.
* 이지목
    * 이지목은 엄격한 모킹을 장려한다는 점이 다르다.
    * 엄격한 모킹이란 명시적으로 호출이 발생할 거라 선언하지 않은 상태에서 호출이 발생했을 때 이를 오류로 간주하는 것이다.
    * 하지만 이 때문에 관계 없는 동작과 결합한다는 단점이 있다.



### 6.8.6 SenderEndPoint 클래스

```java
public class SenderEndPoint {
  private final User user;
  private final Twootr twootr;
  
  SenderEndPoint(final User user, final Twootr twootr) {
    Objects.requireNotNull(user, "user");
    Objects.requireNotNull(twootr, "twootr");
    
    this.user = user;
    this.twootr = twootr;
  }
  
  public FollowStatus onFollow(final String userIdToFollow) {
    Objects.requireNotNull(userIdtoFollow, "userIdToFollow");
    
    return twootr.onFollow(user, userIdToFollow);
  }
}
```



* 실제 Twoot을 전송하려면 코어 도메인을 조금 바꿔야 한다.
* User 객체는 Twoot이 도착했음을 알릴 수 있도록 팔로워 집합을 가져야 한다.

```java
void onSendTwoot(final String id, final User user, final String content) {
  final String userId = user.getId();
  final Twoot twoot = new Twoot(id, userId, content);
  user.followers()
    .filter(User::isLoggedOn)
    .forEach(follower -> follower.receiveTwoot(twoot));
}
```



## 6.9 Position 객체

* Position 객체를 살펴보기 전에 왜 Position 객체가 필요한지 알아보자.
* 사용자가 로그인했을 때, 로그인 이전부터 발생한 팔로워의 모든 트웃을 볼 수 있어야 한다.
* 그러러면 다양한 트웃을 재생할 수 있어야 하며, 사용자가 로그인했을 때 어떤 트웃을 확인하지 않았는지 알아야 한다.



```java
@Test
public void shouldReceiveReplayOfTwootsAfterLogoff() {
  final String id = "1";
  
  userFollowsOtherUser();
  
  final SenderEndPoint otherEndPoint = otherLogon();
  otherEndPoint.onSendTwoot(id, TWOOT);
  logon();
  
  verify(receiverEndPoint).onTwoot(twootAt(id, POSITION_1));
}
```

* 이 기능을 구현하려면 사용자가 로그아웃한 후 어떤 트웃이 발생했는지 시스템이 알아야 한다.
    * 모든 트웃의 시간을 기록하고 사용자가 로그아웃한 시간과 다시 로그인한 시간 사이에 발생한 모든 트웃을 검색
    * 트웃을 연속적인 스트림으로 간주하며 특정 트웃을 스트림의 위치로 지정해 사용자가 로그아웃했을 때 마지막으로 확인한 트웃의 위치를 저장
    * 위치(position) 기능으로 마지막으로 확인한 트웃의 위치를 기록
* 메시지를 시간순으로 정렬하는 방법은 고려하지 않는다.
* 처음에는 메시지를 정렬해야 한다고 생각할 수 있지만 이는 좋은 생각이 아니다.
    * 예를 들어 밀리초 단위로 메시지 시간을 기록한다고 가정하자.
    * 만약 두 트웃이 동시에 발생한다면 어떨까?
    * 사용자가 로그아웃한 같은 시간에 트웃을 수신한다면 어떨까?
* 사용자가 로그아웃한 시간을 기록하는 방법엔 문제가 있다.
    * 사용자가 버튼을 명시적으로 클릭해서 로그아웃한다면 별 문제가 없겠지만, 실질적으로 다양한 방법으로 UI를 중단할 수 있기 때문이다.
* 이런 이유로 트웃을 재생하는 가장 안전한 방법 즉, 트웃에 위치를 할당하고 사용자가 마지막으로 확인한 트웃의 위치를 저장하는 방법을 선택했다.



```java
public class Position {
  public static final Position INITIAL_POSITION = new Position(-1);
  
  private final int value;
  
  public Position(final int value) {
    this.value = value;
  }
  
  public int getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    //
  }
  
  @Override
  public boolean equals(final Object o) {
    //
  }
  
  @Override
  public int hashCode() {
    return value;
  }
  
  public Position next() {
    return new Position(value + 1);
  }
}
```



### 6.9.1 equals()와 hashCode() 메서드

* 같은 값을 갖는 객체 두 개를 비교했을 때, 예상과 다르게 두 값이 같지 않다고 판정되는 상황을 종종 목격할 수 있다.



같아야 할 것 같지만 다른 Point 객체

```java
final Point p1 = new Point(1, 2);
final Point p2 = new Point(1, 2);
System.out.println(p1 == p2); // false 출력
```



Point 객체 일치 정의

```java
@override
public boolean equals(final Object o) {
  //
}

@override
public int hasCode() {
  //
}

final Point p1 = new Point(1, 2);
final Point p2 = new Point(1, 2);
System.out.println(p1.equals(p2)); // true 출력
```



### 6.9.2 equals()와 hashCode() 메서드 사이의 계약

* 두 객체를 equals() 메서드로 같다고 판단했을 때 hashCode() 메서드 역시 같은 값을 반환해야 한다.
* equals(), hashCode() 메서드를 직접 구현하는 일은 드물다.
* 이 기능을 구현하려면 모든 Twoot에 Position 정보가 필요하므로 Twoot 클래스에 위치 정보를 저장하는 필드를 추가하자.
* 또한 사용자가 마지막으로 본 Position을 저장할 lastSeenPosition 필드는 User에 추가한다.
* User가 Twoot을 수신하면 위치를 갱신하고, User가 로그인하면 아직 사용자가 확인하지 않은 트웃을 방출한다.
* 따라서 SenderEndPoint나 ReceiverEndPoint에 새 이벤트를 추가해야 한다.
* 우선은 JDK에서 제공하는 List에 Twoot 객체를 저장했다가 필요할 때 트웃을 재생한다.
* 이제 사용자는 트우터에 접속하지 않아도 모든 트웃을 확인할 수 있다.



## 6.10 총정리

* 통신 방식이라는 큰 그림의 아키텍처를 배웠다.
* 어떤 라이브러리와 프레임워크를 선택하든 도메인 로직에 영향이 없도록 결합을 분리하는 기술을 익혔다.
* 테스트를 먼저 만들고 코드를 구현하는 방식을 배웠다.
* 조금 더 큰 프로젝트에 객체지향 도메인 모델링 기술을 적용했다.































