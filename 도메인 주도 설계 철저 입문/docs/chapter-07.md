
![image-20201210082431211](chapter-07.assets/image-20201210082431211.png "original")



### 7.1 특정 기술 요소에 대한 의존의 결과

<img src="chapter-07.assets/image-20201202074940960.png" alt="image-20201202074940960" style="zoom:50%;" />

[그림 7-1] 복잡한 의존 관계

나무 블록을 쌓아 올리고 나서 그 중간에 위치한 블록을 뽑아내야 하는 상황을 상상해 보자(그림 7-1). 우리가 뽑아내려는 블록은 그 위에 얹힌 블록을 떠받치고 있다. 섣불리 블록을 뽑아 내리다가는 쌓여 있는 블록이 모두 무너져 내릴 것이다. 프로그램의 의존 관계에서도 이런 일이 일어날 수 있다.

소프트웨어에서 중심적 지위를 가진 객체를 변경해야 하는 경우를 생각해 보자. 이 객체에 의존하는 객체도 이 객체가 의존하는 객체도 여러 가지가 있을 것이다. 하나의 변경도 여러 객체에 영향을 미친다. 이처럼 정교하게 서로 엮인 코드를 수정하는 작업에 대한 부담감은 거의 공포에 가까운 감정으로 개발자를 압박할 것이다.

프로그램을 만들어가는 과정에서 객체 간의 의존 관계가 발생하는 것을 막을 수는 없다. 의존관계는 객체를 사용하는 것만으로도 발생하기 때문이다. 의존 관계를 피하는 것보다는 이를 잘 제어하는 것이 중요하다.

이번 장에서 설명한 의존 관계 제어는 도메인 로직을 기술적 요소에서 분리해 소프트웨어를 유연하게 만드는 방법이다. 기술적 요소에 지배당한 코드의 문제점을 먼저 살펴보고 이를 해결할 방법도 알아보자.



### 7.2 의존이란 무엇인가

먼저 간단한 예제를 통해 의존이 무엇인지 알아보자. 의존은 어떤 객체가 다른 객체를 참조하면서 발생한다. 리스트 7-1과 같은 간단한 코드에도 의존 관계가 존재한다.

[리스트 7-1] ObjectA가 ObjectB에 의존하는 관계의 예

```java
public class ObjectA {
    private ObjectB objectB;
}
```

ObjectA는 ObjectB를 참조한다. 그러므로 ObjectB에 대한 정의가 없으면 ObjectA의 정의가 성립할 수 없다. 이때 ObjectA는 ObjectB에 의존한다고 한다. 이때 ObjectA가 ObjectB에 의존한다고 한다.

이 의존관계를 다이어그램으로 나타낼 수 있다. 그림 7-2는 ObjectA와 ObjectB의 의존 관계를 나타낸 다이어그램이다.



<img src="chapter-07.assets/image-20201202080935535.png" alt="image-20201202080935535" style="zoom:67%;" />

[그림 7-2] 참조를 통해 발생하는 의존 관계

의존은 의존하는 객체에서 의존의 대상이 되는 객체 쪽으로 화살표를 통해 나타낸다. 그림 7-2의 화살표는 참조를 통해 발생하는 의존을 나타낸다.

의존 관계는 참조를 통해서만 발생하는 것은 아니다. 인터페이스와 그 구현체가 되는 구상 클래스 사이에도 의존 관계가 생긴다(리스트 7-2).

[리스트 7-2] UserRepository는 IUserRepository 클래스에 의존한다.

```java
public interface IUserRepository {
  User find(UserId id);
}

public class UserRepository implements IUserRepository {
  public User find(UserId id) {
    (...생략...)
  }
}
```

UserRepository 클래스는 IUserRepository 인터페이스를 구현한다. 만약 IUserRepository가 정의되어 있지 않다면 클래스 선언부에 컴파일 에러가 발생하며 UserRepository의 정의가 성립하지 않는다. 따라서 UserRepository는 IUserRepository에 의존한다.



<img src="chapter-07.assets/image-20201202080617609.png" alt="image-20201202080617609" style="zoom:67%;" />

[그림 7-3] 일반화를 통한 의존 관계

인터페이스와 구현 클래스 간의 의존 관계를 그림 7-3에 다이어그램으로 나타냈다. 속이 빈 화살표는 일반화(generalization) 관계임을 나타낸다.

지금까지 본 예제를 통해, 의존 관계는 프로그램을 만들어 나가며 자연히 발생한다는 것을 알았다. 물론 이 외의 개발에서도 의존이 발생했다. 예를 들어 UserServiceApplication 클래스에 나오는 모듈 간의 의존 관계를 살펴보자(리스트 7-3).

[리스트 7-3] UserApplicationService가 갖는 의존 관계

```java
public class UserApplicationService {
    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
  
  	(...생략...)
}
```

UserApplicationService는 UserRepository 객체를 속성으로 갖도록 정의했다. 따라서 UserApplicationService는 UserRepository에 의존하는 상태다. 따라서 UserApplicationService는 UserRepository에 의존하는 상태다(그림 7-4).



<img src="chapter-07.assets/image-20201202081022077.png" alt="image-20201202081022077" style="zoom:67%;" />

[그림 7-4] 리스트 7-3의 코드가 갖는 의존 관계

사실 UserApplicationService 클래스에는 문제가 있다. 구상 클래스 UserRepository에는 특정 퍼시스턴시 기술에 의존한다는 점이다.

리스트 7-3의 코드만으로 UserRepository에서 다루는 데이터스토어가 관계형 데이터베이스인지 NoSQL 데이터베이스인지 판단하기는 어려우나, UserApplicationService가 이 중 한 가지와 엮여 있다는 것은 분명하다. 소프트웨어가 건강하게 성장해 가려면 개발 및 테스트 중 아무때나 부담 없이 코드를 실행할 수 있는 것이 중요하다. 특정 데이터베이스와 밀접하게 엮에 있으면 이것이 불가능하다. 코드를 실행하기 위해 데이터베이스를 갖추고, 여기에 필요한 테이블을 만들어야 한다. 어떤 로직을 다룰지에 따라 데이터베이스에 미리 데이터를 넣어둬야 할 수도 있다. 단지 코드를 실행해 보기 위해 그 정도의 노력이 드는 것이다.

이런 문제를 해결하는 데는 5장에서 배운 리포지토리가 효과적이다. UserApplicationService가 구상 클래스 UserRepository 대신 리포지토리 인터페이스를 참조하게 한다.

[리스트 7-4] 리포지토리 인터페이스를 참조하는 애플리케이션 서비스

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

이제 UserApplicationService는 추상 타입(인터페이스를 추상 타입이라고도 한다) IUserRepository를 구현한 클래스라면 어떤 구상 클래스라도 인자로 받을 수 있다. 즉 UserApplicationService는 UserRepository라는 구상 클래스, 더 나아가 특정 데이터베이스와 엮이지 않는 것이다. 이렇게 수정된 UserApplicationService는 예를 들어 테스트에 사용되는 인메모리 리포지토리를 인자로 전달해 단위 테스트를 실시할 수 있다. 그리고 다른 데이터스토어를 사용하는 새로운 리포지토리를 구현해 주 로직을 수정하지 않고도 데이터스토어를 교체할 수도 있다.

리스트 7-4에 실린 코드의 의존 관계를 그림 7-5에 다이어그램으로 나타냈다.



<img src="chapter-07.assets/image-20201202081624525.png" alt="image-20201202081624525" style="zoom:67%;" />

[그림 7-5] 추상 타입에 의존

추상 타입을 사용하면 기존에 구상 타입을 향하던 의존 관계 화살표가 추상 타입을 향하게 된다. 이런 식으로 의존 관계의 방향을 제어해 모든 모듈이 추상 타입에 의존하게끔 하면 비즈니스 로직이 특정 구현에서 해방될 수 있다.



### 7.3 의존 관계 역전 원칙이란 무엇인가

의존 관계 역전 원칙(Dependency Inversion Principle)은 다음과 같이 정의한다.

​	A. 추상화 수준이 높은 모듈이 낮은 모듈에 의존해서는 안되며 두 모듈 모두 추상 타입에 의존해야 한다.
​	B. 추상 타입이 구현의 세분 사항에 의존해서는 안된다. 구현의 세부 사항이 추상 타입에 의존해야 한다.

의존 관계 역전 원칙은 소프트웨어를 유연하게 하며, 기술적 요소가 비즈니스 로직을 침범하는 일을 막기 위해서는 필수적이다. 지금 잘 이해해두기 바란다.



#### 7.3.1 추상 타입에 의존하라

프로그램에는 추상화 수준이라는 개념이 있는데, 추상화 수준은 입출력으로부터의 거리를 뜻한다. 추상화 수준이 낮다는 것은 기계와 가까운 구체적인 처리를 말하며, 추상화 수준이 높다는 것은 사람과 가까운 추상적인 처리를 말한다.

예를 들어 UserPository를 다루는 UserApplicationService의 처리 내용보다는 데이터스토어를 다루는 UserRepository의 처리 내용이 기계와 더 가깝다. 추상화 수준을 따지면 UserRepository가 UserApplicationService보다 더 낮다. 추상 타입을 사용하지 않았다면 UserApplicationService는 특정 기술 기반에 비해 추상화 수준이 높은 모듈이면서도 데이터 스토어를 다루는 낮은 추상화 수준의 모듈에 의존하는 셈이다. 이 상황은 '추상화 수준이 높은 모듈이 낮은 모듈에 의존해서는 안된다'는 원칙에 위배된다.

UserApplicationService가 추상타입 IUserRepository를 참조하게 하면(리스트 7-4) 이 의존 관계는 그림 7-6과 같이 변화한다.



<img src="chapter-07.assets/image-20201203065201932.png" alt="image-20201203065201932" style="zoom:67%;" />

[그림 7-6] 의존 관계의 변화

추상 타입을 도입하면 UserApplicationService와 UserRepository 두 클래스 모두 추상 타입인 IUserRepository를 향한 의존 관계를 갖는다. 높은 추상화 수준의 모듈이 낮은 추상화 수준의 모듈(UserRepository)에 의존하는 상황도 해소되고 '두 모듈 모두 추상타입에 의존할 것'이라는 원칙도 지켜진다. 본래 구상 타입의 구현이 의존하던 것이 추상 타입을 의존하게 되면서 의존 관계가 역전된다.

일반적으로 추상 타입은 자신을 사용할 클라이언트가 요구하는 정의다. 즉, IUserRepository는 말하자만 UserApplicationService를 위해 존재하는 것이다. 그리고 IUserRepository를 준수해 UserRepository를 구현하면 방침의 주도권을 UserApplicationService에게 넘긴다는 뜻이 된다. '추상 타입이 구현의 세부사항에 의존해서는 안된다 구현의 세부 사항이 추상타입에 의존해야 한다'는 원칙은 이런식을 준수된다.



#### 7.3.2 주도권을 추상 타입에 둬라

전통적인 소프트웨어 개발 기법에는 추상화 수준이 높은 모듈이 낮은 모듈에 의존하는 형태로 만들어지는 경향이 있었다. 다시 말하면 추상 타입이 세부 사항에 의존하는 형태다.

추상 타입이 세부사항에 의존하면 낮은 추상화 수준의 모듈에서 일어난 변경이 높은 추상화 수준의 모듈까지 영향을 미치게 되는 이상한 상황이 발생한다. 중요도가 높은 도메인 규칙은 항상 추상화 수준이 높은 쪽에 기술되는데, 낮은 추상화 수준의 모듈에서 일어난 변경 때문에 더 중요한, 추상화 수준이 높은 모듈을 수정해야 하는 상황(예를 들어, 데이터스토어 변경 때문에 비즈니스 로직을 변경하는)이 일어나는 것이다. 이는 바람직한 상황이 아니다.

주체가 되는 것은 추상화 수준이 높은 모듈, 추상 타입이어야 한다. 추상화 수준이 낮은 모듈이 주체가 되어서는 안된다.

추상화 수준이 높은 모듈은 낮은 모듈을 이용하는 클라이언트다. 클라이언트가 할 일은 어떤 처리를 호출하는 선언이다. 앞서 설명했듯이 인터페이스는 구현할 처리를 클라이언트에 선언하는 것이며 주도권은 인터페이스를 사용할 클라이언트에 있다. 추상화 수준이 낮은 모듈을 인터페이스와 함께 구현하면 좀 더 중요도가 높은 고차원적 개념에 주도권을 넘길 수 있다.



### 7.4 의존 관계 제어하기

상황에 따라 UserApplicationService가 테스트용 인메모리 리포지토리를 사용해야 할 수도 있고, 운영용 관계형 데이터베이스에 접속해야 할 수도 있다. 개발 중이라면 테스트용 리포지토리를 주로 사용할 것이고, 배포 빌드라면 운영용 리포지토리를 사용할 것이다. 이때 더 중요한 것은 무엇을 사용하느냐가 아니고 원하는 것을 선택하게 제어하는 것이다. 지금부터 의존 관계를 제어하는 수단을 알아보자.

우선 흔하지 않은 예를 먼저 살펴볼 것이다. 리스트 7-5는 일단 테스트용 인메모리 리포지토리를 사용하는 것만을 목적으로 하는 짧은 코드다.

[리스트 7-5] 생성자 메서드에서 인메모리 리포지토리 생성하기

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService() {
        this.userRepository = new InMemoryUserRepository();
    }
}
```

리스트 7-5의 코드를 보면 userRepository가 추상 타입으로 정의되어 있지만, 생성자 메서드 안에서 구상 클래스의 객체를 만들면서 InMemoryUserRepository에 의존 관계가 발생한다. 이로부터 발생하는 문제는 이미 완성된 코드에 추가로 수정이 필요해지는 단순한 문제다. 어느 정도 동작하는 수준까지 개발이 끝난 시점 혹은 원하는 테스트를 끝낸 다음이 될지는 모르겠으나, 어쨌든 늦어도 릴리스 시점에는 운영용 리포지토리를 사용하게끔 코드를 수정해야 한다.

[리스트 7-6] 운영용 리포지토리로 교체하기

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService() {
      //this.userRepository = new InMemoryUserRepository();
      this.userRepository = new UserRepository();
    }
  
  	(...생략...)
}
```

거기다 이런 작업이 이곳만으로 끝난다는 보장은 없다. 개발 중 앞에서와 같은 코드를 허용했다면 분명 비슷한 코가 다른 곳에도 있을 것이다. 이들 모두 빠짐없이 운영용 리포지토리로 교체 수정해야 한다. 수정 자체는 매우 단순한 작업이지만, 개발자가 좋아하지 않는 번거롭고 피곤한 작업이다.

또한 빠짐없이 수정을 마치고 코드를 릴리스했더라도, 인메모리 리포지토리를 다시 사용해야 할 상황이 생길 수도 있다. 이를테면 버그가 발생해 원인을 파악해야 할 경우가 그렇다. 버그가 발생하면 이 버그 상황을 재현하기 위한 데이터베이스를 준비하는 데도 수고가 든다. '에러를 발생시키면서도 무결성을 유지하는 데이터'를 만들기란 쉬운 일이 아니다.

이때 테스트용 리포지토리로 교체해 프로그램의 동작을 확인하면 편리할 것이다. 그러나 이때도 개발자를 기다리는 것은 운영용 리포지토리를 테스트용 리포지토리로 다시 교체하는 단순한 작업이다.

Service Locator 패턴과 IoC Container 패턴으로 이 문제를 해결할 수 있다. 



### 7.4.1 Service Locator 패턴

Service Locator 패턴은 ServiceLocator 객체에 의존 해소 대상이 되는 객체를 미리 등록해 둔 다음, 인스턴스가 필요한 곳에서 ServiceLocator를 통해 인스턴스를 받아 사용하는 패턴이다.

설명만으로는 잘 이해되지 않을 것이다. 구체적인 예를 살펴보자. 리스트 7-7은 Service Locator 패턴이 적용된 UserApplicationService다.

[리스트 7-7] ServiceLocator 패턴이 적용된 예

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService() {
        this.userRepository = ServiceLocator.resolve();
    }
}
```

생성자 메서드에서 IUserRepository에 대한 의존을 해소하기 위해 ServiceLocator에 인스턴스 요청을 보낸다. 이 요청을 통해 반환되는 실제 인스턴스는 시작 스크립트 등을 이용해 미리 등록된 인스턴스다(리스트 7-8).

[리스트 7-8] ServiceLocator에 의존 해소를 위한 정보를 미리 등록하기

```java
ServiceLocator.register<IUserRepository, InMemoryUserRepository>();
```

리스트 7-8과 같이 인스턴스를 미리 등록해 두고 IUserRepository에 대한 의존 해소 요청이 들어오면 InMemoryUserRepository의 인스턴스를 만들어 반환한다. 만약 운영용 리포지토리를 사용하고 싶다면 ServiceLocator를 리스트 7-9와 같이 수정하면 된다.

[리스트 7-9] ServiceLocation의 설정을 운영용 리포지토리로 교체하기

```java
ServiceLocator.register<IUserRepository, UserRepository>();
```

IUserRepository를 필요로 하는 객체는 모두 ServiceLocator를 통해 인스턴스를 받으므로 의존 관계가 설정된 시작 스크립트만 수정하면 간단하게 리포지토리를 교체할 수 있다.

이렇듯 ServiceLocator에 의존 관계 해소를 맡기면 InMemoryUserRepository나 UserRepository의 인스턴스를 만드는 코드가 사라지므로 애플리케이션의 핵심을 담당하는 로직을 수정하지 않아도 리포지토리의 구현체를 교체할 수 있다(그림 7-7).



 <img src="chapter-07.assets/image-20201203073457129.png" alt="image-20201203073457129" style="zoom:67%;" />



[그림 7-7] ServiceLocator를 이용한 의존 관계 해소

ServiceLocator에 등록된 의존 관계 설정은 운영용과 테스트용을 나눠 일괄적으로 관리하면 편리하다. 시작 스크립트 등에서 프로젝트 구성 설정을 키로 삼아 용도에 맞는 의존 관계 설정으로 교체되게 한다.



<img src="chapter-07.assets/image-20201203074100869.png" alt="image-20201203074100869" style="zoom:67%;" />

[그림 7-8] 시작 스크립트를 이용한 의존 관계 설정 교체

Service Locator 패턴은 처음부터 커다란 설정을 만들 필요가 없어서 최초 도입이 쉽다. 한편으로는 Service Locator 패턴을 안티 패턴으로 보는 사람들도 있다. 그 이유는 크게 다음 두 가지다.

* 의존 관계를 외부에서 보기 어렵다.

* 테스트 유지가 어렵다.

  

###### 의존 관계를 외부에서 보기 어렵다

Service Locator 패턴을 적용한 경우 대부분 생성자 메서드는 하나다. 그 이유는 Service Locator에서 필요한 인스턴스를 제공하기 때문이다. 이때 외부에서 클래스 정의를 보면 리스트 7-10과 같다.

[리스트 7-10] 외부에서 본 클래스 정의

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService();
    public void register(UserRegisterCommand command);
}
```

이 정의를 보고 나면 개발자는 UserApplicationService의 인스턴스를 만들고 register메서드를 호출할 것이다. 그것 말고는 이 객체에 할 수 있는 일이 없기 때문이다. 그러나 실제로 인스턴스를 만들어 메서드를 호출해 모녀 에러가 발생하고 프로그램이 강제 종료된다. 그 이유는 UserApplicationService의 생성자 메서드가 ServiceLocator에 IUserRepository에 대한 의존 관계 해소를 요청하기 때문이다(리스트 7-11).

[리스트 7-11] 리스트 7-10의 생성자 메서드

```java
public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService() {
        // IUserRepository의 의존 관계 해소 대상이 설정되어 있지 않으므로 예외 발생
        this.userRepository = ServiceLocator.resolve();
    }
  
  	(...생략...)
}
```

ServiceLocator에 의존 관계 해소를 위한 설정이 미리 돼 있지 않기 때문에 UserApplicationService가 의존 관계를 해소하는 데 실패한다.

클래스 정의만 보고 'UserApplicationService가 바르게 동작하려면 IUserRepository를 요청했을때 전달할 객체를 미리 등록해야 한다'라는 정보를 알 수 없다는 것은 바람직한 것은 아니다.

ServiceLocator에 UserRepository를 등록해 UserApplicationService를 바르게 동작하게 했다는 것은 UserApplicationService의 구현 내용을 봤거나 초능력자가 아니고서는 불가능하다. 물론 주석을 통해 따로 정보를 제공할 수도 있겠으나, 주식이 실제 코드와 일치하지 않는 경우가 존재하는 이상 최선의 해결책이 못 된다.



###### 테스트 유지가 어렵다

뛰어난 개발자는 '사람은 실수하는 존재'라는 점을 잘 알고 있으며, 그 대표적인 예가 자기 자신이라는 것도 잘 아는 사람이다. 테스트는 이런 실수의 가능성을 미연에 방지하는 도구다. 모든 실수를 방지하지는 못하지만, 착각하거나 의도하지 않은 동작을 발견할 수 있다. 리스트 7-12는 UserApplicationService를 테스트하는 테스트 스크립트의 일부분이다.

[리스트 7-12] 테스트를 준비하는 스크립트

```java
ServiceLocator.register(new InMemoryUserRepository());
UserApplicationService userApplicationService = new UserApplicationService();
```

UserApplicationService를 구현한 당시에는 이 코드가 문제없이 동작했다. 테스트는 개발자의 실수를 바로잡아 준다는 점에서 유용하다. 이렇게 제 역할을 다하는 코드라도 시간이 지남에 따라 변화가 필요하다. UserApplicationService도 예외가 아니다(리스트 7-13).

[리스트 7-13] UserApplicationService에 일어난 변화

```java
public class UserApplicationService {
    private final IUserRepository userRepository;
		// 새로운 속성이 추가됨
  	private final IFooRepository fooRepository;
  
    public UserApplicationService() {
        this.userRepository = ServiceLocator.resolve();
      	// ServiceLocator를 통해 필요한 인스턴스를 받음
      	this.fooRepository = ServiceLocator.resolve();
    }
  
  	(...생략...)
}
```

변화한 코드에 새로운 의존 관계가 추가됐다. 그러나 리스트 7-12 테스트 코드에는 fooRepository에 대한 의존 관계 해소 정보가 등록되지 않았다. 이 때문에 테스트가 깨진다.

하지만 테스트가 깨지는 것 차제는 그리 큰 문제가 아니다. UserServiceApplication을 변경하면 이를 테스트하는 테스트 코드에도 변경이 필요해지는 것은 당연한 일이다. 진짜 문제는 테스트를 실행할 때까지 테스트가 깨진 것을 깨닫지 못하는 것이다.

개발자의 입장에서 테스트는 나를 돕는 도구지만, 끊임없는 관리가 필요한 귀찮은 존재이기도 하다. 그런 만큼 테스트를 유지하려면 일정 수준의 강제력이 필요하다. 지금 같은 의존 관계의 변경이 생겼을 대 바로 테스트 코드에도 반영하게 강제할 수 없다면 오래지 않아 테스트가 깨질 것이다.



#### 7.2.2 IoC Container 패턴

IoC Container(DI Container)가 무엇인지 이해하려면 먼제 Dependency Injection 패턴에 대해 알아야 한다.

Dependency Injection 패턴은 '의존 관계 주입'이라고 번역할 수 있다. 직역에 가까운 번역이라 이해될 듯 말듯 아리송하기만 하다. 구체적인 예를 보며 이해해 보자. 리스트 7-14는 UserApplicationService에 InMemoryRepository를 주입하는, 다시 말해 Dependency Injection을 하는 코드다.

[리스트 7-14] 의존 관계 주입

```java
IUserRepository userRepository = new InMemoryUserRepository();
UserApplicationService userApplicationService = new UserApplicationService(userRepository);
```

이 방식은 의존 관계를 주입하는 데 생성자 메서드를 사용하므로 생성자 주입이라고도 한다. 지금까지 예제 코드에서 여러 번 본 패턴이다. 이외에도 메서드를 이용해 의존 관계를 주입하는 메서드 인젝션 등 다양한 패턴이 존재한다. 의존하는 모듈을 외부에서 주입한다는 점에서는 모든 패턴이 같다.

Dependency Injection 패턴을 적용하면 의존 관계를 변경했을 때 테스트 코드 수정을 강제할 수 있다. 예를 들어 리스트 7-15의 코드처럼 UserApplicationService에 새로운 의존 관계가 추가됐다고 하자.

[리스트 7-15] 새로운 의존 관계가 추가된 UserApplicationService

```java
public class UserApplicationService {
    private final IUserRepository userRepository;
    // IFooRepository에 대한 의존 관계가 새로 추가됨
  	private final IFooRepository fooRepository;
  
  	// 생성자 메서드를 통해 의존 관계를 주입함.
    public UserApplicationService(IUserRepository userRepository, IFooRepository fooRepository) {
        this.userRepository = userRepository;
        this.fooRepository = fooRepository;
    }
  
  	(...생략...)
}
```

UserApplicationService에 새로운 의존 관계가 추가되면서 생성자 메서드에도 인자가 추가됐다. 그로 인해 테스트 코드에서 UserApplicationService의 인스턴스를 만드는 코드가 컴파일 에러를 일으킨다(리스트 7-16).

[리스트 7-16] 테스트에 컴파일 에러가 발생함

```java
IUserRepository userRepository = new InMemoryUserRepository();
// 2번째 인자로 IFooRepository의 구현체가 전달되지 않았으므로 컴파일 에러 발생
UserApplicationService userApplicationService = new UserApplicationService(userRepository);
```

테스트를 실행하려면 먼저 컴파일 에러부터 수정해야 하는데, 바로 이 강제력이 큰 역할을 한다.

그러나 편리함의 반대 면에는 의존하는 객체를 만드는 코드를 여기저기 작성해야 하는 불편함이 있다. 예를 들어 개발 중 인메모리 리포지토리로 프로그램을 실행하고 있었다면 운영 환경으로 넘어갈 때는 데이터베이스를 쓰는 리포지토리로 바꿔야 한다. 이런 경우 리스트 7-16에서 보듯이 리포지토리의 인스턴스를 만드는 곳을 모두 찾아 리포지토리를 교체할 필요가 있다.

이런 문제를 해결해 주는 것이 IoC Container 패턴이다. 

...



### 7.5 정리

이번 장에서 프로그램과 떼려야 뗄 수 없는 개념인 의존과 의존 관계를 제어하는 방법을 알아봤다.

의존 관계는 소프트웨어를 만드는 과정에서 자연스럽게 발생한다. 그러나 의존 관계를 잘 다루지 못하면 손대기가 어려울 정도로 유연성을 잃은 소프트웨어가 된다.

소프트웨어는 원래 유연해야 한다. 사용자가 처한 환경의 변화에 맞춰 변화하며 지속적으로 사용자에게 도움을 줄 수 있어야 한다. 그렇기 때문에 '소프트'웨어인 것이다.

의존 관계를 두려워할 필요는 없다. 의존 관계가 발생하는 것은 막을 수 없어도 그 방향성은 개발자가 절대적으로 제어 가능하기 때문이다. 이번 장에서 배운 내용에 따라 도메인을 중심으로 삼고 주요 로직이 기술적 요소와 의존 관계를 갖지 않게 하면서 소프트웨어의 유연성을 확보해 나가기 바란다.

  

  

 