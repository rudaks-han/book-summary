# 7장 트우터 확장판

## 7.2 목표

* 의존관계 역전 원칙과 의존관계 주입으로 결합도 피하기
* 저장소 패턴, 쿼리 객체 패턴으로 데이터 영구 저장하기
* 함수형 프로그래밍이란 무엇이며 자바로 구현한 실제 응용프로그램에 이를 적용하는 방법을 간단히 소개한다.



## 7.4 영구 저장과 저장소 패턴

* 프로세스를 재시작하면 모든 트웃과 사용자 정보가 사라진다.
* 프로세스를 재시작해도 정보가 사라지지 않도록 저장하는 기능이 필요하다.
* 저장소 패턴이라는 유명한 패턴을 이용하면 문제를 쉽게 해결할 수 있다.
* 저장소 패턴은 도메인 로직과 저장소 백엔드 간의 인터페이스를 정의한다. 
* 저장소 패턴을 이용하면 나중에 응용프로그램의 저장소를 다른 저장소 백엔드로 쉽게 갈아탈 수 있다. 
* 저장소 패턴의 장점은 다음과 같다.
    * 저장소 백엔드를 도메인 모델 데이터로 매핑하는 로직을 중앙화함
    * 실제 데이터베이스 없이도 코어 비즈니스 로직을 유닛 테스트하므로 빠르게 테스트를 실행할 수 있음
    * 각 클래스가 하나의 책임을 가지므로 유지보수성과 가동성이 좋아짐

* 저장소란 객체 컬렉션과 비슷하다. 
* 다만 컬렉션처럼 객체를 메모리에 저장하지 않고 다른 어딘가에 저장한다는 점이 다르다. 
* 대부분의 저장소는 다음과 같은 몇 가지 공통 기능을 구현한다.
    * add(): 새 객체 인스턴스를 저장소로 저장
    * get(): 식별자로 한 개의 객체를 검색
    * delete(): 영구 저장 백엔드에서 인스턴스 삭제
    * update(): 객체에 저장한 값이 인스턴스의 필드와 같게 만듦
* 이런 종류의 연산을 줄여서 CRUD라 부른다.



### 7.4.1 저장소 설계

* 저장소에 '일반적인' 기능을 추가하고 싶은 유혹이 생길 수 있지만 주의해야 한다. 
* 사용하지 않는 코드와 불필요한 코드는 일종의 부채이기 때문이다. 
* 사용하지 않는 코드는 부채일 뿐이다. 
* 요구 사항이 바뀌면서 코드베이스를 리팩터링하고 개선할 때, 사용하지 않는 코드가 많아진다면 작업이 더욱 어려워진다.
* YAGNI는 '여러분은 그 기능이 필요하지 않을거예요(you aren't gonna need it)'를 의미한다. 
* 미래에 사용할 것 같은 기능은 구현하지 말고 정말로 사용해야 할 때 그 기능을 구현하라는 의미다.

```java
public interface UserRepository extends AutoCloseable {
  boolean add(User user);
  Optional<User> get(String userId);
  void update(User user);
  void clear();
  FollowStatus follow(User follower, User userToFollow);
}
```

Twoot 객체는 불변이므로 TwootRepository에 update() 기능을 구현하지 않았다.

```java
public interface TweetRepository {
  Twoot add(String id, String userId, String content);
  Optional<Twoot> get(String id);
  void delete(Twoot twoot);
  void query(TwootQuery twootQuery, Customer<Twoot> callback);
  void clear();
}
```

* TwootRepository의 add() 메서드는 몇 가지 파라미터 값을 받아 객체를 만들어 반환한다.
* 데이터 계층은 트우터 객체 시퀀스를 만드는 적절한 도구를 가지므로 고유 객체를 만드는 일을 데이터 계층에 위임한다.
* 아래 예는 제네릭 인터페이스로 저장소 패턴을 구현한 예다.

```java
public interface AbstractRepository<T> {
  void add(T value);
  
  Optional<T> get(String id);
  
  void update(T value);
  
  void delete(T value);
}
```



### 7.4.2 쿼리 객체

* 저장소를 마치 자바 컬렉션처럼 구현한 다음, 여러 Twoot 객체를 반복하며 필요한 작업을 수행하는 방법으로 간단하게 이 기능을 구현한다.
* 구현은 단순하지만, 데이터 저장소의 모든 데이터 행을 자바 응용프로그램으로 가져온 다음 필요한 쿼리를 수행할 수 있으므로 속도가 현저히 느릴 가능성이 있다.
* 기존의 쿼리 기능을 그대로 활용하는 것이 바람직하다.
* 사용자 객체로 관련 트웃을 검색하는 twootsForLogon() 메서드를 구현한다.
* 그러면 비즈니스 로직 기능이 저장소 구현과 결합되는 단점이 생긴다.
* 요구 사항이 바뀌면 코어 도메인 로직뿐만 아니라 저장소도 바꿔야 하므로 구현을 바꾸기 어려우며 단일 책임 원칙에도 위배된다.

```java
List<Twoot> twootsForLogon(User user);
```

```java
List<Twoot> twootsFromUsersAfterPosition(Set<String> inUsers, Position lastSeenPosition);
```

* TwootRepository로 쿼리할 조건을 객체 안에 추상화했다.

```java
List<Twoot> query(TwootQuery query);
```

```java
public class TwootQuery {
  private Set<String> inUsers;
  private Position lastSeenPosition;
  
  public Set<String> getInUsers() {
    return inUsers;
  }
  
  public Position getLastSeenPosition() {
    return lastSeenPosition;
  }
  
  public TwootQuery inUsers(final Set<String> inUsers) {
    this.inUsers = inUsers;
    return this;
  }
  
  public TwootQuery inUsers(String... inUsers) {
    return inUsers(new HashSet<>(Arrays.asList(inUsers)));
  }
  
  public TwootQuery lastSeenPosition(final Position lastSeenPosition) {
    this.lastSeenPosition = lastSeenPosition;
    return this;
  }
  
  public boolean hasUsers() {
    return inUsers != null && !inUsers.isEmpty();
  }
}
```

* 객체 List를 반환한다는 것은 모든 Twoot 객체를 메모리에 저장해 한 번에 처리함을 의미한다.
* List의 크기가 매우 클 수 있으므로 이는 좋은 방법이 아니다.
* 모든 Twoot 객체를 메모리에 저장하는 대신 각 객체를 UI로 푸시해 이 문제를 해결할 수 있다.
* 여기서는 Customer<Twoot> 콜백으로 간단히 문제를 해결한다.



```java
void query(TwootQuery twootQuery, Customer<Twoot> callback);
```

쿼리 메서드 사용 예

```java
twootRepository.query(
  new TwootQuery()
  	.inUsers(user.getFollowing())
  	.lastSeenPosition(user.getLastSeenPosition()),
  user::receiveTwoot);
)
```



* 이 책에서는 설명하지 않았지만 작업 단위(Unit of Work) 패턴이라는 저장소 구현 기법도 있다.
* 예를 들어 두 은행 계좌 사이에 돈을 이체하거나 한 계좌에서 돈을 인출하고 이를 다른 계좌로 입금하고 싶다.
* 이때 한 가지 동작이라도 실패하면 전체 동작을 취소해야 한다.
* 데이터베이스는 보통 ACID를 준수하도록 트랜잭션을 구현하므로 이런 종류의 작업이 안전하게 수행될 수 있도록 보장한다.
* 작업 단위는 데이터 베이스 트랜잭션이 원활하게 수행되도록 돕는 디자인 패턴이다.



* 하지만 설계한 저장소 인터페이스를 실제로 어떻게 구현하는지는 아직 설명하지 않았다.
* 자바 생태계에는 이 작업을 자동화하는 다양한 객체 관계 매핑(ORM)이 준비되어 있다.
* 그중에서도 하이버네이트는 가장 유명한 ORM 중 하나다.



## 7.5 함수형 프로그래밍

* 스트림 API와 컬렉터 API, Optional 클래스 등 함수형 프로그래밍 구현에 도움을 주는 몇 가지 기능이 추가되었다.
* 자바 8 이전에는 라이브러리 개발자가 활용할 수 있는 추상화 수준에 한계가 있었다.
* 예를 들어 큰 데이터 컬렉션에서는 효과적으로 병렬 연산을 수행할 수 없었다.



### 7.5.1 람다 표현식

* 익명 함수를 람다 표현식으로 줄여서 정의한다.

```java
public interface ReceiverEndPoint {
  void onTwoot(Twoot twoot);
}
```

예제에서는 ReceiverEndPoint 인터페이스 구현을 제공하는 새로운 객체를 만든다.

```java
public class PrintingEndPoint implements ReceiverEndPoint {
  @Override
  public void onTwoot(final Twoot twoot) {
    System.out.println(twoot.getSenderId() + ": " + twoot.getContent());
  }
}
```

익명 클래스로 ReceiverEndPoint 구현

```java
final ReceiverEndPoint anonymousClass = new ReceiverEndPoint() {
  @Override
  public void onTwoot(final Twoot twoot) {
    System.out.println(twoot.getSenderId() + ": " + twoot.getContent());
  }
};
```

자바 8에서는 람다표현식을 사용할 수 있다.

```java
final ReceiverEndPoint lambda = twoot -> System.out.println(twoot.getSenderId() + ": " + twoot.getContent());
```



### 7.5.2 메서드 레퍼런스

```java
twoot -> twoot.getContent();
```

메서드 레퍼런스

```java
Twoot:getContent
```



람다로 SenderEndPoint 생성

```java
(user, twootr) -> new SenderEndPoint(user, twootr)
```

메서드 레퍼런스로 SenderEndPoint 생성

```java
SenderEndPoint::new
```



### 7.5.3 실행 어라운드

* 실행 어라운드(execute around)는 함수형 디자인 패턴에서 자주 사용된다.
* 항상 비슷한 작업을 수행하는 초기화, 정리 코드가 있고, 초기화, 정리 코드에서 실행하는 비즈니스 로직에 따라 이를 파라미터화하고 싶은 상황을 겪어봤을 것이다.
    * 파일
        파일을 사용하기 전에 열고, 파일을 사용한 다음 닫는다. 작업에 문제가 생기면 예외를 기록해야 한다. 파라미터화된 코드로 파일의 내용을 읽거나 파일에 데이터를 기록한다.
    * 락
        임계 구역 이전에 락을 획득한 다음, 크리티컬 섹션 다음에 락을 해제한다. 파라미터화된 코드가 임계 구역이다.
    * 데이터베이슷 연결
        초기화 작업에서 데이터베이스를 연결하고 작업을 완료한 후 연결을 닫느다. 데이터베이스 연결 풀을 이용하는 상황이라면, 연결 로직에서 풀의 연결을 가져오도록 만들 수 있어 유용하다.
* 실행 어라운드 패턴에서는 초기화, 정리 코드에서 공통 메서드를 추출해 문제를 해결한다.

extract 메서드에 실행 어라운드 패턴 사용

```java
<R> R extract(final String sql, final Extract<R> extract) {
  try (var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    stmt.clearParameters();
    return extractor.run(stmt);
  } catch (SQLException e) {
    throw new IllegalStateException(e);
  }
}
```



### 7.5.4 스트림

* 자바의 가장 중요한 함수형 프로그래밍 기능은 컬렉션 API와 스트림에 중점을 둔다.
* 스트림 덕분에 루프를 이용하지 않고 높은 수준으로 컬렉션 처리 코드를 추상화할 수 있다.



#### map()

* 어떤 형식의 값을 포함하는 스트림을 다른 형식의 값의 스트림으로 변환할 때 map()을 활용한다.

루프로 사용자 튜플 만들기

```java
private String usersTupleLoop(final Set<String> following) {
  List<String> quotedIds = new ArrayList<>();
  for (String id : following) {
    quotedIds.add("'" + id + "''");
  }
  return '(' + String.join(",", quotedIds + ')');
}
```

map() 으로 사용자 튜플 만들기

```java
public String usersTuple(final Set<String> following) {
  return following
    	.stream()
    	.map(id -> "'" + id + "'")
    	.collect(Collectors.joining(",", "(", ")"));
}
```



#### forEach()

* forEach()는 스트림의 모든 요소를 인수로 받아 작업을 수행하는 Cusumer 콜백을 한 개의 인수로 받는다.



#### filter()

* 어떤 데이터를 반복하면서 각 요소에 if문을 적용하는 상황이라면 Stream.filter() 메서드를 이용할 수 있다.



루프로 트웃을 반복하면서 if문으로 확인

```java
public void queryLoop(final TwootQuery twootQuery, final Customer<Twoot> callback) {
  if (!twootQuery.hasUsers()) {
    return;
  }
  
  var lastSeenPosition = twootQuery.getLastSeenPosition();
  var inUsers = twootQuery.getInUsers();
  
  for (Twoot twoot: twoots) {
    if (inUsers.contains(twoot.getSenderId()) &&
      twoot.isAfter(lastSeenPosition)) {
      callback.accept(twoot);
    }
  }
}
```



함수형 스타일

```java
public void queryLoop(final TwootQuery twootQuery, final Customer<Twoot> callback) {
  if (!twootQuery.hasUsers()) {
    return;
  }
  
  var lastSeenPosition = twootQuery.getLastSeenPosition();
  var inUsers = twootQuery.getInUsers();
  
  twoots
    	.stream()
    	.filter(twoot -> inUsers.contains(twoot.getSenderId()))
    	.filter(twoot -> twoot.isAfter(lastSeenPosition))
    	.forEach(callback);
}
```



#### reduce()

* 전체 리스트를 한 개의 값으로 줄이는 상황, 예를 들어 다양한 트랜잭션에서 모든 값의 합계를 찾는 작업에서 reduce() 패턴을 사용한다.

```java
Object accumulator = initialValue;
for (Object element : collection) {
  accumulator = combine(accumulator, element);
}
```



### 7.5.5 Optional

* Optional은 null을 대신하도록 자바 8에서 추가된 코어 자바 라이브러리 데이터 형식이다.
* null을 사용하면 무시무시한 NullPointerException이 방생하는 문제가 있다.
* Optional을 두 가지 기능을 제공한다.
* 첫 번째는 버그를 피하기 위해 변수의 값이 있는지 개발자가 확인하도록 장려한다.
* 두 번째는 클래스의 API에서 값이 없을 수 있다는 사실을 Optional 자체로 문서화한다.

값으로 Optional 만들기

```java
Optional<String> a = Optional.of("a");

assertEquals("a", a.get());
```

* Optional은 값을 갖지 않을 수 있는데, 이때 팩토리 메서드 empty()를 사용한다.
* 또한 nullable()로 null이 될 수 있는 값을 Optional로 만들 수도 있다.



```java
Optional emptyOptional = Optional.empty();
Optional alsoEmpty = Optional.ofNullable(null);

assertFalse(emptyOptional.isPresent());

// a는 이전 예제에 정의되어 있음
assertTrue(a.isPresent());
```

* Optional의 get()은 NoSuchElementException을 던질 수 있으므로 isPresent()를 사용하면 조금 더 안전하게 get()을 호출할 수 있다.
* 하지만 이는 Optional을 제대로 활용하는 방법이 아니다.
* 이는 어떤 객체가 null인지 확인하는 기존 방법과 같기 때문이다.
* Optional이 비었을 때 대쳇값을 제공하는 orElse() 메서드로 깔끔하게 코드를 구현할 수 있다.
* 대쳇값 계산에 시간이 많이 걸린다면 orElseGet()을 이용한다.
* 그래야 Optional이 비었을 때만 Supplier 함수로 전달한 함수가 실행되기 때문이다.



orElse()와 orElseGet() 사용

```java
assertEquals("b", emptyOptional.orElse("b"));
assertEquals("c", emptyOptional.orElseGet(() -> "c"));
```

* Optional은 스트림 API에 사용할 수 있는 메서드(filter(), map(), isPresent() 등)도 제공한다.
* Optional.filter()는 조건을 만족하면 Optional의 요소를 유지하고, 프레디케이트 결과가 거짓이거나 빈 Optional이면 그대로 빈 Optional을 반환한다.
* 마찬가지로 map()은 Optional 안의 값을 반환하는데, 값이 없으면 함수를 아예 적용하지 않는다.
* 이렇게 Optional이 값을 포함해야 연산을 적용하므로 null보다 Optional이 더 안전하다.



## 7.6 사용자 인터페이스

* 자바스크립트 프런트엔드와 서버 간의 모든 통신은 JSON 표준으로 이루어진다.



## 7.7 의존관계 역전과 의존관계 주입

* 다음은 의존관계 역전의 정의다.
    * 높은 수준의 모듈은 낮은 수준의 모듈에 의존하지 않아야 한다. 두 모듈 모두 추상화에 의존해야 한다.
    * 추상화는 세부 사항에 의존하지 않아야 한다. 세부 사항은 추상화에 의존해야 한다.
* Twootr 라는 높은 수준의 진입점 클래스를 만들었지만, 이 클래스는 DataUserRepository 같은 다른 낮은 수준의 모듈에 의존하지 않는다.
* 이는 구현이 아닌 UserRepository 인터페이스처럼 추상화에 의존하기 때문이다.
* 의존관계 주입(DI)이라는 개념도 있다.



팩토리로 인스턴스 만들기

```java
public Twootr() {
  this.userRepository = UserRepository.getInstance();
  this.twootRepository = TwootRepository.getInstance();
}

// 트우터 시작
UserRepository.setInstance(new DatabaseUserRepository());
TwootRepository.setInstance(new DatabaseTwootRepository());
Twootr twootr = new Twootr();
```

의존관계 주입으로 인스턴스 만들기

```java
public Twootr(final UserRepository userRepository, final TwootRepository twootRepository) {
  this.userRepository = userRepository;
  this.twootRepository = twootRepository;
}

// 트우터 시작
Twootr twootr = new Twootr(new DatabaseUserRepository(), new DatabaseTwootRepository());
```



## 7.8 패키지와 빌드 시스템

* 자바에서는 코드베이스를 여러 패키지로 쪼갤 수 있다.
    * com.iteratrlearning.shu_book.chapter_06는 프로젝트의 최상위 패키지다.
    * com.iteratrlearning.shu_book.chapter_06.database는 SQL 데이터베이스 저장용 어댑터를 포함한다.
    * com.iteratrlearning.shu_book.chapter_06.in_memory는 인메모리 저장용 어댑터를 포함한다.
    * com.iteratrlearning.shu_book.chapter_06.web_adapter는 웹소켓 기반 UI 어댑터를 포함한다.



## 7.9 한계와 단순화

* 트우터가 한 개의 스레드에서 실행된다고 가정하므로 동시성 문제는 완전히 무시했다.
* 또한 호스팅 서버에 장애가 일어났을 때의 상황도 고려하지 않았다.
* 확장성도 무시했다.
* 마찬가지로 로그인했을 때 모든 트웃을 보여주는 기능도 심한 병목현상을 일으킬 수 있다.



## 7.10 총정리

* 저장소 패턴으로 데이터 저장과 비즈니스 로직의 결합을 제거할 수 있다.
* 두 가지 방식의 저장소 구현 방법을 살펴봤다.
* 자바 8 스트림을 포함한 함수형 프로그래밍 개념을 소개했다.
* 다양한 패키지로 큰 프로젝트를 구성하는 방법을 확인했다.



