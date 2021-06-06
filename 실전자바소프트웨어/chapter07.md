# 7장 트우터 확장판

## 7.4 영구 저장과 저장소 패턴

저장소 패턴은 도메인 로직과 저장소 백엔드 간의 인터페이스를 정의한다. 저장소 패턴을 이요하면 나중에 응용프로그램의 저장소를 다른 저장소 백엔드로 쉽게 갈아탈 수 있다. 저장소 패턴의 장점은 다음과 같다.

* 저장소 백엔드를 도메인 모델 데이터로 매핑하는 로직을 중앙화함
* 실제 데이터베이스 없이도 코어 비즈니스 로직을 유닛 테스트하므로 빠르게 테스트를 실행할 수 있음
* 각 클래스가 하나의 책임을 가지므로 유지보수성과 가동성이 좋아짐

저장소랑 객체 컬렉션과 비슷하다. 다만 컬렉션처럼 객체를 메모리에 저장하지 않고 다른 어딘가에 저장한다는 점이 다르다. 



### 7.4.1 저장소 설계

저장소에 '일반적인' 기능을 추가하고 싶은 유혹이 생길 수 있지만 주의해야 한다. 사용하지 않는 코드와 불필요한 코드는 일종의 부채이기 때문이다. 사용하지 않는 코드는 부채일 뿐이다. 요구 사항이 바뀌면서 코드베이스를 리팩터링하고 개선할 때, 사용하지 않는 코드가 많아진다면 작업이 더욱 어려워진다.

YAGNI는 '여러분은 그 기능이 필요하지 않을거예요(you aren't gonna need it)'를 의미한다. 미래에 사용할 것 같은 기능은 구현하지 말고 정말로 사용해야 할 때 그 기능을 구현하라는 의미다.

```java
public interface UserRepository extends AutoCloseable {
  boolean add(User user);
  Optional<User> get(String userId);
  void update(User user);
  void clear();
  FollowStatus follow(User follower, User userToFollow);
}
```

```java
public interface TweetRepository {
  Twoot add(String id, String userId, String content);
  Optional<Twoot> get(String id);
  void delete(Twoot twoot);
  void query(TwootQuery twootQuery, Customer<Twoot> callback);
  void clear();
}
```



### 7.4.2 쿼리 객체

