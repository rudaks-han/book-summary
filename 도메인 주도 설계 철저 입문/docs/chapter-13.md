
![image-20201210083023271](chapter-13.assets/image-20201210083023271.png "original")



### 13.1 명세란?

객체를 평가하는 절차가 단순하다면 해당 객체의 메서드로 정의하면 되겠지만 복잡한 평가 절차가 필요할 수도 있다. 그런가 하면 평가 절차를 평가 대상 객체의 메서드로 두는 것이 자연스럽지 못한 경우도 있다.

결국 이러한 객체 평가 절차는 애플리케이션 서비스에 구현되기 마련인데, 객체에 대한 평가는 도메인 규칙 중에서도 중요도가 높은 것으로 서비스에 구현하기에 걸맞은 내용이 아니다.

이를 위한 대책으로 많이 쓰이는 것이 명세다. 명세는 어떤 객체가 그 객체의 평가 기준을 만족하는지 판정하기 위한 객체다.

우선 실제로 복잡한 평가 절차를 갖는 객체를 예로 들어 명세가 어떤 역할을 하는지 살펴보자.



#### 13.1.1 객체의 복잡한 평가 절차

어떤 객체가 특정한 조건을 만족하는지 평가하는 코드는 해당 객체의 메서드 형태로 정의된다. 앞서 봤던 Circle 객체 역시 이러한 메서드를 갖고 있었다(리스트 13-1).

[리스트 13-1] 특정한 조건 충족 여부를 평가하는 메서드

```java
public class Circle {
  	(...생략...)
  
    public boolean isFull() {
        return this.members.size() >= 29;
    }
}
```

이 정도의 단순한 조건이라면 큰 문제가 되지 않는다. 그러나 조건이 좀 더 복잡해진다면 어떻게 될까?

예를 들어 서클의 최대 인원이 소속된 사용자의 유형에 따라 다음과 같이 바뀌는 규칙이 있다고 하자.

* 사용자 중에는 프리미엄 사용자라는 유형이 존재한다.
* 서클의 최대 인원은 서클장과 소속 사용자를 포함해 30명이다.
* 프리미엄 사용자가 10명 이상 소속된 서클은 최대 인원 50명으로 늘어난다.

서클 객체는 자신에게 소속된 사용자의 목록을 저장하고 있지만, 단순히 UserId의 컬렉션을 포함하는 것을 넘어 프리미엄 사용자가 몇 명 소속되어 있는지까지 리포지토리를 통해 확인해야 한다. 그러나 Circle 객체는 사용자 리포지토리를 갖고 있지 않다. 그러므로 이 리포지토리를 가진 애플리케이션 서비스에서 조건 만족 여부를 확인한다(리스트 13-2).

[리스트 13-2] 서클의 최대 인원이 조건에 따라 변화하는 경우

```java
public class CircleApplicationService {
    private final ICircleRepository circleRepository;
    private final IUserRepository userRepository;

    public CircleApplicationService(ICircleRepository circleRepository, IUserRepository userRepository) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
    }

    public void join(CircleJoinComamnd command) {
        CircleId circleId = new CircleId(command.getCircleId());
        Circle circle = circleRepository.find(circleId);

        List<User> users = userRepository.find(circle.getMembers());
        // 서클에 소속된 프리미엄 사용자의 수에 따라 최대 인원이 결정됨
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        if (circle.countMembers() >= circleUpperLimit) {
            throw new CircleFullException(circleId.getValue());
        }
    }
    
    (...생략...)
}
```

서클의 최대 인원을 확인하는 것은 도메인의 규칙을 준수하기 위한 것이다. 지금까지 설명했듯이, 서비스는 도메인 규칙에 근거한 로직을 포함해서는 안된다. 이를 그대로 허용하면 도메인 객체가 제 역할을 빼앗기고 서비스 코드 이곳저곳에 도메인의 주요 규칙이 중복해서 작성된다.

도메인 규칙은 도메인 객체에 정의돼야 한다. 최대 인원 확인을 Circle 클래스의 isFull 메서드에 정의할 방법을 찾아보자. 이렇게 하려고 보니 이번에는 Circle 클래스가 사용자 정보를 식별자만 가지고 있다는 점이 문제가 된다. 식별자만으로 사용자 정보를 얻으려면 isFull 메서드가 어떻게든 사용자 리포지토리를 전달받아야 한다(리스트 13-3).

[리스트 13-3] 리포지토리를 갖게 된 엔티티

```java
public class Circle {
    // 소속된 사용자 중 프리미엄 사용자 수를 확인해야 하는데
    // 가진 정보는 사용자의 식별자뿐이다.
    private List<UserId> members;

  	(...생략...)

    // 엔티티가 사용자 리포지토리를 갖는다?
    public boolean isFull(IUserRepository userRepository) {
        List<User> users = userRepository.find(members);
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        return countMembers() >= circleUpperLimit;
    }
}
```

이런 방법은 좋지 않다. 리포지토리는 도메인 설계에 포함된다는 점에서는 도메인 객체라고 할 수 있지만, 도메인 개념에서 유래한 객체는 아니다. Circle 클래스 사용자 리포지토리를 갖게 되면 도메인 모델을 나타내는 데 전념하지 못하게 된다.

엔티티나 값 객체가 도메인 모델을 나타내는 데 전념할 수 있으려면 리포지토리를 다루는 것은 가능한 한 피해야 한다.



#### 13.1.2 이 문제의 해결책 - 명세

명세라는 객체를 이용하면 엔티티나 값 객체가 리포지토리를 다루지 않으면서도 이 문제를 해결할 수 있다. 서클의 최대 인원에 여유가 있는지 확인하는 코드를 다음과 같이 명세로 분리할 수 있다(리스트 13-4).

[리스트 13-4] 서클의 최대 인원에 여유가 있는지 확인하는 명세

```java
public class CircleFullSpecification {
    private final IUserRepository userRepository;

    public CircleFullSpecification(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isSatisfiedBy(Circle circle) {
        List<User> users = userRepository.find(circle.getMembers());
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        return circle.countMembers() >= circleUpperLimit;
    }
}
```

명세는 객체가 조건을 만족하는지 확인하는 역할만을 수행한다. 평가 대상 객체가 복잡한 평가 절차 코드에 파묻히는 일 없이 원래 의도를 그대로 잘 드러낼 수 있다.

리스트 13-5는 서클의 최대 인원에 여유가 있는지 확인하는 데 명세를 이용하게 수정한 코드다.
[리스트 13-5] 명세를 이용한 예

```java
public class CircleApplicationService {
    private final ICircleRepository circleRepository;
    private final IUserRepository userRepository;

    (...생략...)
    
    public void join(CircleJoinComamnd command) {
        CircleId circleId = new CircleId(command.getCircleId());
        Circle circle = circleRepository.find(circleId);

        CircleFullSpecification circleFullSpecification = new CircleFullSpecification(userRepository);
        if (circleFullSpecification.isSatisfiedBy(circle)) {
            throw new CircleFullException(circleId.getValue());
        }
      
      	(...생략...)
    }
}
```

복잡한 객체 평가 코드를 캡슐화해 원래 객체의 의도를 잘 드러낼 수 있게 됐다.

###### 의도가 잘 드러나지 않는 객체

객체를 평가하는 코드를 곧이곧대로 해당 객체에 구현하면 객체의 원래 의도가 잘 드러나지 않는다. 다시 말해 이 객체가 무엇을 위한 것이며 어떤 역할을 하는지 잘 알 수 없다(리스트 13-6).

[리스트 13-6] 객체 평가 메서드로 가득한 클래스 정의

```java
public class Circle {
    public boolean isFull();
    public boolean isPopular();
    public boolean isAnniversary(DateTime today);
    public boolean isRecuiting();
    public boolean isLocked();
    public boolean isPrivate();
    public void join(User user);
}
```

객체 평가 메서드를 이렇게 객체에 그대로 남겨두면 객체에 대한 의존이 무방비하게 증가해 변경이 닥쳤을 때 어려움을 겪는다.

어떤 객체를 평가하는 수단이 해당 객체에만 있어야 한다는 법은 없다. 이런 코드를 명세 같은 외부 객체로 분리하는 선택지도 있다는 것을 알아두기 바란다.



#### 13.1.3 리포지토리를 되도록 사용하지 않기

명세도 엄연한 도메인 객체이므로 내부에서 일어나는 입출력을 최대한 억제해야 한다는 의견도 있다. 이런 경우 일급 컬렉션을 이용하는 방법을 생각해 볼 수 있다. 일급 컬렉션(first-class collection)은 List 등의 제네릭 컬렉션 객체 대신 특화된 컬렉션 객체를 이용하는 패턴이다.

예를 들어 리스트 13-7은 서클의 소속 사용자를 나타내는 일급 컬렉션이다.

[리스트 13-7] 서클의 소속 사용자를 나타내는 일급 컬렉션

```java
public class CircleMembers {
    private final CircleId id;
    private final User owner;
    private final List<User> members;

    public CircleMembers(CircleId id, User owner, List<User> members) {
        this.id = id;
        this.owner = owner;
        this.members = members;
    }

    public int countMembers() {
        return members.size() + 1;
    }

    public long countPremiumMembers(boolean containsOwner) {
        long premiumUserNumber = members.stream().filter(User::isPremium).count();
        if (containsOwner) {
            return premiumUserNumber + (owner.isPremium() ? 1 : 0);
        } else {
            return premiumUserNumber;
        }
    }
}
```

CircleMembers는 일반적으로 사용되는 List 등과 달리 서클의 식별자와 이에 소속된 사용자의 정보를 모두 저장한다. 그리고 독자적인 계산 처리를 메서드로 정의할 수 있다.

CircleMembers 클래스가 사용된 명세는 리스트 13-8과 같다.

[리스트 13-8] CircleMember 클래스를 사용한 명세

```java
public class CircleMembersFullSpecification {
    public boolean isSatisfiedBy(CircleMembers members) {
        long premiumUserNumeber = members.countPremiumMembers(false);
        long circleUpperLimit = premiumUserNumeber < 10 ? 30 : 50;
        return members.countMembers() >= circleUpperLimit;
    }
}
```

일급 컬렉션을 적용하기로 했다면 애플리케이션 서비스에서 일급 컬렉션 객체에 정보를 주입하는 절차를 추가해야 한다(리스트 13-9).

[리스트 3-9] 일급 컬렉션 객체에 정보 주입하기

```java
User owner = userRepository.find(circle.getOwner());
List<User> members = userRepository.find(circle.getMembers());
CircleMembers circleMembers = new CircleMembers(circle.getId, owner, members);
CircleMembersFullSpecification circleFullSpec = new CircleMembersFullSpecification();
if (circleFullSpec.isSatisfiedBy(circleMembers)) {
    (...생략...)
}
```

도메인 객체에서 입출력을 가능한 한 배제해야 한다. 일급 컬렉션을 통해 이를 관철하는 데 도움을 받을 수 있을 것이다.



### 13.2 명세와 리포지토리의 조합

명세는 단독으로 사용되기도 하지만 리포지토리와 조합해 사용할 수도 있다. 구체적으로 밝히면, 리포지토리가 명세를 전달받아 명세에 정의된 조건과 합치하는 객체를 검색하는 방법이다.

리포지토리에는 검색을 수행하는 메서드가 정의돼 있지만, 검색에도 중요한 규칙이 포함되는 경우가 있다. 이런 검색 기능을 리포지토리의 메서드로 정의하면 중요한 도메인 규칙이 리포지토리 구현체로 빠져나가는 일이 생긴다.

이럴 때는 이 중요 규칙을 명세로 정의한 다음 리포지토리에 이 명세를 전달해 중요 규칙을 구현한 코드가 리포지토리의 일개 구현체로 빠져나가는 일을 방지할 수 있다.



##### 13.2.1 추천 서클 검색 기능으로 본 복잡한 검색

사용자가 서클에 가입하고 싶을 때 자신에게 맞는 서클을 검색할 수 있다면 편리할 것이다. 추천 서클 검색 기능을 개발하는 과정을 살펴보자.

추천 서클 검색 기능을 만들려면 우선 추천 서클이 무엇인지에 대한 정의를 분명히 내려야 한다. 이를테면 '활동이 활발한 서클'이나 '최근에 결성된 서클'처럼 여러 가지 조건을 생각할 수 있다. 여기서는 우선 다음 두 가지 조건을 만족하는 서클을 추천 서클로 삼는다.

* 최근 1개월 이내에 결성된 서클
* 소속된 사용자 수가 10명 이상

추천 서클의 정의가 결정됐으니 그 다음으로 추천 서클 검색 기능을 어디에 구현할 것인가를 결정해야 한다.

지금까지 사용자 및 서클 검색은 실질적으로 리포지토리가 담당해왔다. 추천 서클 검색 역시 리포지토리에 맡기기로 한다(리스트 13-10).

[리스트 13-10] 리포지토리에 추천 서클 검색 메서드 추가하기

```java
public interface ICircleRepository {
  	(...생략...)
    List<Circle> findRecommended(long now);
}
```

findRecommended 메서드는 인자로 받은 날짜로부터 조건과 가장 부합하는 서클을 골라주는 메서드다. 애플리케이션 서비스에서 findRecommended 메서드를 이용해 사용자에게 노출할 추천 서클을 제안한다(리스트 13-11).

[리스트 13-11] 애플리케이션 서비스에서 추천 서클을 검색하는 코드

```java
public class CircleApplicationService
{
    private long now;
  
    public CircleGetRecommendResult getRecomend(CircleGetRecommendRequest request) {
        // 리포지토리에 모두 맡기면 된다.
        List<Circle> recommendedCircles = circleRepository.findRecommended(now);
        return new CircleGetRecommendResult(recommendedCircles);
    }
}
```

코드 자체는 잘 동작하지만, 한 가지 문제가 있다. 추천 서클을 추려내는 조건이 리포지토리의 구현체에 의존한다는 점이다.

추천 서클 조건은 중요도가 높은 도메인 규칙이다. 이러한 규칙이 인프라스트럭처 객체에 불과한 리포지토리의 구현체에 좌지우지되는 것은 바람직하지 않다.

리포지토리가 강력한 패턴이기는 해도 도리어 그 강력함 때문에 도메인의 중요 규칙이 인프라스트럭처 영역으로 유출될 수 있다는 것에 주의하기 바란다.



#### 13.2.2 명세를 이용한 해결책

도메인의 중요 지식은 가능한 한 도메인 객체로 표현해야 한다. 추천 서클 여부를 판단하는 처리는 말 그대로 객체에 대한 평가이므로 명세로 정의할 수 있다(리스트 13-12).

[리스트 13-12] 추천 서클 조건 만족 여부를 판단하는 명세 객체

```java
@NoArgsConstructor
@Getter
public class CircleRecommendSpecification implements ISpecification<Circle> {
    private long executeDateTime;

    public CircleRecommendSpecification(long executeDateTime) {
        this.executeDateTime = executeDateTime;
    }

    public boolean isSatisfiedBy(Circle circle) {
        if (circle.countMembers() < 10) {
            return false;
        }

        return circle.getCreated() > executeDateTime - 60*1000*60*24*30;
    }
}
```

CircleRecommendSpecification은 추천 서클 조건 만족 여부를 판정하는 객체다. 추천 서클 검색의 구현 코드는 리스트 13-13과 같다.

[리스트 13-13] 명세를 통해 추천 서클 검색하기

```java
public class CircleApplicationService
{
    private final ICircleRepository circleRepository;
    private long now;

		(...생략...)

    public CircleGetRecommendResult getRecommend(CircleGetRecommendRequest request) throws SQLException {
        CircleRecommendSpecification recommendSpecification = new CircleRecommendSpecification(now);

        List<Circle> circles = circleRepository.findAll();
        List<Circle> recommendCircles = circles.stream()
                .filter(circle -> new CircleRecommendSpecification().isSatisfiedBy(circle))
                .limit(10)
                .collect(Collectors.toList());

        return new CircleGetRecommendResult(recommendCircles);
    }
}
```

이것으로 추천 서클의 선정 조건을 리포지토리에 구현할 필요가 없게 됐다.

그리고 명세의 메서드를 직접 호출하지 않고 리포지토리에 명세를 전달해 메서드를 호출할 수 있다(더블 디스패치). 이런 방법을 사용하려면 미리 명세의 인터페이스를 정의한다(리스트 13-14).

[리스트 13-14] 명세의 인터페이스 및 구현 클래스

```java
public interface ISpecification<T> {
    boolean isSatisfiedBy(T value);
}

public class CircleRecommendSpecification implements ISpecification<Circle> {
  	(...생략...)
}
```

리포지토리는 이 인터페이스를 사용해 추천 서클을 추려낸 서클의 리스트를 반환한다(리스트 13-15).

[리스트 13-15] 명세 인터페이스를 사용해 추천 서클을 추려내는 리포지토리

```java
public interface ICircleRepository
{
		(...생략...)
  
    List<Circle> find(ISpecification<Circle> specification) throws SQLException;
}
```

명세를 인터페이스로 정의하면 리포지토리가 모든 명세 타입을 메서드에 추가로 정의할 필요가 없다. ISpecification<Circle> 인터페이스를 구현해 새로운 명세를 정의하고 그대로 인자로 전달하기만 하면 해당 명세에 따른 검색이 가능하다.

리스트 13-16은 리스트 13-14의 명세를 이용해 구현한 추천 서클 검색이다.

[리스트 13-16] 리스트 13-14의 명세를 이용해 구현한 추천 서클 검색

```java
public class CircleApplicationService
{
    private final ICircleRepository circleRepository;
    private long now;
  
  	(...생략...)
  
  	public CircleGetRecommendResult getRecommend(CircleGetRecommendRequest request) throws SQLException {
        CircleRecommendSpecification circleRecommendSpecification = new CircleRecommendSpecification(now);
        // 리포지토리에 명세를 전달해 추천 서클을 추려냄(필터링)
        List<Circle> recommentCircles = circleRepository.find(circleRecommendSpecification).stream()
                .limit(10).collect(Collectors.toList());

        return new CircleGetRecommendResult(recommentCircles);
    }
}
```

명세를 이용한 방법으로 추천 서클을 선정하는 조건을 서비스 대신 도메인 객체에 구현할 수 있었다.



#### 13.2.3 명세와 리포지토리를 함께 사용할 때 생기는 성능 문제

리포지토리에 명세를 전달하는 기법은 도메인 규칙을 도메인 객체에서 유출되지 않게 하고 확장성을 높일 수 있는 효과적인 방법이지만, 그만큼 단점도 있다.

리스트 13-16에 실린 ICircleRepository의 구현 클래스를 살펴보자(리스트 13-17).

[리스트 13-17] 명세 객체를 이용하는 리포지토리 구현체

```java
public class CircleRepository implements ICircleRepository {
    private final Connection connection;

  	(...생략...)
  
    @Override
    public List<Circle> find(ISpecification<Circle> specification) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM circles");
        ResultSet resultSet = pstmt.executeQuery();
        List<Circle> circles = new ArrayList<>();

        while (resultSet.next()) {
            // 인스턴스를 생성해 조건에 부합하는지 확인(조건을 만족하지 않으면 버림)

            Circle circle = createInstance(resultSet);
            if (specification.isSatisfiedBy(circle)) {
                circles.add(circle);
            }
        }

        return circles;
    }
}
```

명세에 정의된 조건과 부합 여부는 객체를 생성한 다음 명세의 메서드를 통해 확인해야 알 수 있다. 결과적으로 이 코드는 모든 서클의 정보를 받아온 다음 하나하나 조건에 부합 하는지 확인하는 코드가 된다. 데이터 건수가 많지 않다면 괜찮겠지만, 수만 건을 넘어간다면 매우 느린 작업이 될 수 있다.

리포지토리에서 명세를 필터로 이용할 때는 항상 성능을 염두에 두기 바란다.



#### 13.2.4 복잡한 쿼리는 리드모델로

추천 서클 검색처럼 특수한 조건을 만족하는 객체를 검색하는 기능은 편리한 소프트웨어라면 거의 반드시 포함되는 기능이다. 이러한 기능은 대부분 사용자의 편의성을 위한 것으로, 성능면에서도 요구사항이 높은 경우가 많다.

이런 상황이라면 명세와 리포지토리를 결합해 사용하는 패턴을 사용하지 않는 것도 고려해야 한다. 이번 장의 주제인 명세와는 조금 거리가 있지만, 한번 훑어볼 가치는 있다.

리스트 13-18은 서클의 목록을 받아온 다음 각 서클의 서클장이 되는 사용자의 정보도 함께 받아오는 코드다.

[리스트 13-18] 서클 목록을 받아오는 코드

```java
public class CircleApplicationService
{
    public CircleGetSummariesResult getSummaries(CircleGetSummariesCommand command) {
        // 모든 서클의 목록을 받아옴
        List<Circle> all = circleRepository.findAll();
        // 페이징 처리
        List<Circle> circles = all.stream()
                .skip((command.getPage() -1) * command.getSize())
                .limit(command.getSize())
                .collect(Collectors.toList());

        List<CircleSummaryData> summaries = new ArrayList<>();

        for(Circle circle: circles) {
            // 각 서클의 서클장에 해당하는 사용자 정보 검색
            User owner = userRepository.find(circle.getOwner());
            summaries.add(new CircleSummaryData(circle.getId().getValue(), owner.getName().getValue()));
        }

        return new CircleGetSummariesResult(summaries);
    }
  
  	(...생략...)
}
```

이 코드에는 두 가지 문제가 있다.

첫 번째 문제는 코드 초반에 모든 서클의 목록을 가져온다는 점이다. 페이징 처리가 포함돼 있으므로 모든 서클의 목록이 필요하지는 않다. 오히려 목록 중 대부분이 불필요하다. 시스템 자원을 들여 복원해 온 인스턴스 중 대부분이 참조 한 번 되지 않은 채 버려진다.

두 번째 문제는 서클에 소속된 사용자의 목록을 받아오는 검색이 반복문을 통해 여러 번 실행 된다는 점이다. 리포지토리의 구체적인 구현이 어떤지는 알 수 없으나 일반적인 SQL을 상정하면 대량의 쿼리가 쓰이는 셈이다. 원래대로라면 JOIN 문 등을 활용해 하나의 쿼리로 만들 수 있다. 

리스트 13-18의 코드는 정상적으로 동작은 하지만 최적화와는 거리가 멀다. 도메인 지식을 도메인 레이어 안에 모아둔다는 목적을 생각하면 이 코드도 일리가 있겠지만 이것만을 이유로 사용자의 편의성을 위한 최적화 요청을 외면할 수 있을까?

시스템의 애초 존재 의의는 사용자의 문제를 해결하는 것이다. 다른 어떤 것이 변해도 이것만은 변할 수 없다. 시스템은 사용자를 우호적으로 대해야 한다. 사용자에게 우호적이지 못한 시스템은 곧 사용자에게 외면받을 것이고 그 뒤는 조용한 죽음이 기다릴 뿐이다.

도메인의 보호를 이유로 사용자에게 불편을 강요하는 것은 결코 옳은 길이 아니다. 도메인의 영역을 보호하는 것도 물론 중요하지만 애플리케이션 영역은 프레젠테이션(즉 시스템 사용자)을 의식하지 않을 수 없다.

이런 문제는 특히 리포지토리를 통해 데이터를 읽을 때 발생한다. 프레젠테이션 계층은 일반적인 리포지토리 읽기 작업보다 훨씬 복잡한 작업을 요구하는 경우가 많다. 현재 상황에 대한 요약이나 페이징 처리가 가장 흔한 예다.

복잡한 읽기 작업에서 성능 문제가 우려된다면 이러한 부분에 한해 도메인 객체의 제약에서 벗어나는 방법도 가능하다. 리스트 13-19는 페이징을 위한 쿼리를 직접 실행하는 예다.

[리스트 13-19] 최적화를 위해 직접 쿼리를 실행하는 코드

```java
public class CircleQueryService {
		(...생략...)

    public CircleGetSummariesResult getSummaries(CircleGetSummariesCommand command) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT " +
                        "circles.id as circleId, " +
                        "users.name as ownerName " +
                        "FROM circles " +
                        "LEFT OUTER JOIN users " +
                        "ON circles.ownerId = users.id " +
                        "ORDER BY circles.id " +
                        "OFFSET :skip ROW " +
                        "FETCH NEXT :size ROWS ONLY"
        );
        pstmt.setInt(1, (command.getPage() - 1));
        pstmt.setInt(2, command.getSize());

        ResultSet resultSet = pstmt.executeQuery();

        List<CircleSummaryData> summaries = new ArrayList<>();

        while(resultSet.next()) {
            String circleId = resultSet.getString("circleId");
            String ownerName = resultSet.getString("ownerName");
            CircleSummaryData summary = new CircleSummaryData(circleId, ownerName);
            summaries.add(summary);
        }

        return new CircleGetSummariesResult(summaries);
    }
}
```

리스트 13-19는 SQL을 사용하는 모듈이지만, ORM이 적용된 모듈이라도 무방하다.

읽기 대상 내용(쿼리)은 복잡하지만, 동작 내용 자체는 도메인 로직이라 할 만한 것이 거의 없다. 반대로 쓰기 작업(명령)에는 도메인에 의한 제약이 있는 경우가 많다.

이 때문에 쓰기 작업에서는 도메인과 결합을 느슨하게 하기 위해 도메인 객체 등을 적극적으로 활용하지만, 읽기 작업에서는 그렇지 않은 경우가 있다. 여기서는 자세히 다루지 않지만, 이러한 아이디어는 CQS(command-query separation) 혹은 CQRS(command-query responsibility segregation)라는 개념에서 온 것이다. 이들 개념은 객체의 메서드를 성격에 따라 커맨드와 쿼리로 크게 나눠 다르게 다루는 것이 요점인데, 프레젠테이션 계층의 성능적인 요구를 만족하면서도 시스템의 통제를 늦추지 않는 효과를 거둘 수 있다.



### 13.3 정리

객체의 평가는 그 내용 자체만으로도 지식이다. 명세는 객체를 평가하는 조건과 절차를 모델링 한 객체다.

객체의 평가를 해당 객체 자신에게 맡기는 방법도 있지만, 이 방법이 항상 바람직하지는 않다. 명세와 같은 외부 객체에 평가를 맡긴 코드가 오히려 이해하기 쉬운 경우도 많다.

이번 장에서는 리포지토리에 명세 객체를 전달해 필터링을 수행하는 기법을 소개했다. 하지만 이러한 기법도 만능이 아니며 성능 면에서 문제가 생길 수 있다.

읽기 작업은 단순하지만 최적화를 필요로 하는 경우가 많다. 도메인의 제약은 잠시 잊고 클라이언트의 편의성을 우선적으로 고려할 수 있어야 한다.

  

  

 