
![image-20201210082035071](chapter-04.assets/image-20201210082035071.png "original")



### 4.1 서비스란?

서비스란 무엇일까?

서비스업이라는 업종명을 들어본 적이 있을 것이다. 소프트웨어 시스템을 서비스를 제공한다고 표현하기도 한다. 또 '서비스하다'라는 동사로 사용되는 경우도 있다. 모두 서비스라는 단어가 쓰였지만, 의미는 제각각이다. 서비스라는 단어가 친숙하기는 하지만, 막상 '서비스가 무엇인가'라는 질문을 받는다면 선뜻 답하기가 어렵다.

소프트웨어 개발에서 말하는 서비스는 클라이언트를 위해 무언가를 해주는 객체를 말한다. 그러나 이 '무언가'의 범위가 매우 넓고 다양하기 때문에 도리어 혼란스럽다. 심지어 도메인 주도 설계에만 초점을 맞춰도 서비스가 들어간 용어끼리 의미가 서로 다른 경우도 있어 상당히 큰 혼란을 일으킨다.

도메인 주도 설계에서 말하는 서비스는 크게 두 가지로 나뉜다. 첫 번째는 도메인을 위한 서비스고 두 번째는 애플리케이션을 위한 서비스다. 서비스의 의미가 혼란스러운 이유는 이 두 가지를 혼동하기 때문이다. 앞으로는 이 두 가지를 확실히 구분하기 위해 전자를 도메인 서비스, 후자를 애플리케이션 서비스라고 부르겠다. 애플리케이션 서비스는 6장에서 더 자세히 설명한다. 서비스라는 단어가 자주 나올 텐데, 어떤 의미로 쓰였는지 주의하며 읽기 바란다.



### 4.2 도메인 서비스란?

값 객체나 엔티티 같은 도메인 객체에는 객체의 행동을 정의할 수 있다. 예를 들어 사용자명으로 사용할 수 있는 문자열의 길이나 문자의 종류에 제한이 있다면 이러한 지식은 사용자명을 나타내는 값 객체에 정의될 것이다.

그러나 시스템에는 값 객체나 엔티티로 구현하기 어색한 행동도 있다. 도메인 서비스는 이런 어색함을 해결해주는 객체다.

우선 값 객체나 엔티티에 정의하기 어색한 행동이란 어떤 것인지 알아본 다음, 도메인 서비스가 이를 어떻게 해결하는지 설명하겠다.



#### 4.2.1 값 객체나 엔티티에 정의하기 어색한 행동

현실에서는 동명이인이 충분히 있을 수 있지만, 시스템에서는 사용자명을 중복으로 사용할 수 없게 하는 경우는 많다. 사용자명에 중복을 허용하지 않는 것은 도메인의 규칙이며 따라서 도메인 객체에 행동으로 정의돼야 한다. 그렇다면 이 규칙은 구체적으로 어떤 객체에 구현돼야 할까?

우선 사용자에 대한 사항은 사용자를 나타내는 객체에 담는다는 지극히 당연한 논리적 사고에 따라 User 클래스에 사용자명의 중복 여부를 확인하는 행위를 추가해보자(리스트 4-1).

[리스트 4-1] 사용자명의 중복 여부를 확인하는 코드를 User 클래스에 추가하기

```java
public class User {
    private UserId id;
    private UserName name;

    public User(UserId id, UserName name) {
	      if (id == null)
            throw new IllegalArgumentException("id : " + id);
	      if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = id;
        this.name = name;
    }

  	// 사용자명 중복 여부 확인 코드 추가
    public boolean exists(User user) {
        // 사용자명 중복을 확인하는 코드
      	(...생략...)
    }
}
```

객체의 정의만 봐서는 문제가 없어 보이지만, 사실 이 코드는 자연스럽지 못한 코드다. 실제로 이 메서드를 이용해 중복을 확인하는 과정을 따라가 보자(리스트 4-2).

[리스트 4-2] 리스트 4-1에 실린 코드를 사용한 사용자명 중복 확인

```java
UserId userId = new UserId("id");
UserName userName = new UserName("smith");
User user = new User(userId, userName);

// 새로 만든 객체에 중복 여부를 묻는 상황이 됨
boolean duplicateCheckResult = user.exists(user);
System.out.println(duplicateCheckResult); // true? false?
```

사용자명 중복을 확인하는 처리는 User 클래스에 정의돼 있으니 결국 자기 자신에게 중복여부를 묻는 상황이 된다. 이런 코드는 많은 경우에 개발자를 혼란스럽게 하는 부자연스러운 코드다. 자신의 사용자명 중복 여부를 확인하는 일을 해당 객체에 맡긴다면 그 결과로 참을 반환해야 할까? 아니면 거짓을 반환해야 할까?

중복 여부 확인을 새로 생성한 객체에 맡기면 개발자가 혼란을 일으키기 쉽다. 그러니 접근법을 조금 바꿔보자. 예를 들어 사용자명 중복을 확인하는 목적으로만 사용되는 전용 인스턴스를 만든다면 어떨까(리스트 4-3)?

```java
UserId userId = new UserId("id");
UserName userName = new UserName("smith");
User user = new User(userId, userName);

// 새로 만든 객체에 중복 여부를 묻는 상황이 됨
boolean duplicateCheckResult = user.exists(user);
System.out.println(duplicateCheckResult); // true? false?

4-3
UserId checkId = new UserId("check");
UserNmae checkName = new UserName("checker");
User checkObject = new User(checkId, checkName);

UserId userId = new UserId("id");
UserName userName = new UserName("smith");
User user = new User(userId, userName);

// 사용자명 중복 확인용 객체에 중복 여부를 문의함
User duplicateCheckResult = checkObject.exists(user);
System.out.println(duplicateCheckResult);
```

리스트 4-3의 코드를 보면 자기 자신에게 자신의 사용자명 중복 여부를 묻지 않아도 된다는 부자연스러움은 사라졌다. 그러나 사용자명 중복 확인을 위해 만든 checkObject가 사용자를 나타내는 객체이면서 사용자가 아니라는 점에서 여전히 부자연스러움이 남아있다. 올바른 코드라면 이런 부자연스러운 객체가 존재해서는 안 된다.

엔티티로 구현한 사용자 객체에 사용자명 중복 처리를 구현하는 것은 부자연스러운 코드의 전형적인 예다. 이러한 부자연스러움을 해결해주는 것이 도메인 서비스다.



#### 4.2.2 부자연스러움을 해결해주는 객체

도메인 서비스도 일반적인 객체와 다를 것이 없다. 사용자에 대한 도메인 서비스는 리스트 4-4와 같이 정의한다.

[리스트 4-4] 사용자에 대한 도메인서비스 정의

```java
public class UserService {
    public boolean exists(User user) {
        // 사용자명 중복을 확인
      	(...생략...)
    }
}
```

도메인 서비스는 자신의 행동을 바꿀 수 있는 인스턴스만의 값을 갖지 않는다는 점에서 값 객체나 엔티티와 다르다.

중복을 확인하는 구체적인 구현 내용은 좀 더 나중에 다루겠다. 지금은 중복을 확인하는 메서드가 UserService 클래스 안에 정의돼 있다는 것만 알면 된다.

이렇게 정의한 사용자 도메인 서비스를 이용해 실제로 사용자명 중복을 확인해보자(리스트 4-5).

[리스트 4-5] 사용자 도메인 서비스를 이용해 사용자명 중복을 확인하기

```java
UserService userService = new UserService();

UserId userId = new UserId("id");
UserName userName = new UserName("john");
User user = new User(userId, userName);

// 도메인 서비스에 요청하기
boolean duplicateCheckResult = userService.exists(user);
System.out.println(duplicateCheckResult);
```

도메인 서비스를 이용하니 자기 자신에게 중복 여부를 확인하거나 중복 확인에만 사용되고 버려질 인스턴스를 만들 필요가 없어졌다. 리스트 4-5의 코드는 개발자에게 혼란을 주지 않는 자연스러운 코드다.

값 객체나 엔티티에 정의하기 부자연스러운 처리를 도메인 서비스에 정의하면 자연스러운 코드를 만들 수 있다.



### 4.3 도메인 서비스를 남용한 결과

엔티티나 값 객체에 정의하기 부자연스러운 처리는 도메인 서비스에 정의하면 된다. 이때 중요한 것은 '부자연스러운 처리'에만 한정해야 한다는 점이다. 그렇지 않으면 모든 처리가 도메인 서비스에 정의되는 결과를 낳을 수 있다.

예를 들어 사용자명 변경 처리를 엔티티가 아닌 도메인 서비스에 정의하면 리스트 4-6과 같은 코드가 된다.

[리스트 4-6] 도메인 서비스에 정의된 사용자명 수정 처리

```java
public class UserService {
    public void changeName(User user, UserName userName) {
        if (user == null)
            throw new IllegalArgumentException("user: " + user);
        if (userName == null)
            throw new IllegalArgumentException("userName: " + userName);

        user.setName(userName);
    }
}
```

리스트 4-6은 의도한 대로 사용자명을 변경하는 코드다. 언뜻 보면 올바른 코드로 보일 수도 있지만, 이대로라면 User 클래스의 코드가 어떻게 될지 생각해 보자(리스트 4-7).

[리스트 4-7] 리스트 4-6에서 사용될 User 클래스의 정의

```java
public class User {
    private final UserId id;
    private UserName name;

    public User(UserId id, UserName name) {
        this.id = id;
        this.name = name;
    }

    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
    }
}
```

모든 처리를 도메인 서비스에 구현하면 엔티티에는 게터와 세터만 남게 된다. 아무리 숙련된 개발자라도 이러한 코드만으로는 사용자 객체의 처리 내용이나 적용되는 도메인 규칙을 발견하기 어렵다.

생각 없이 모든 처리 코드를 도메인 서비스로 옮기면 다른 도메인 객체는 그저 데이터를 저장할 뿐, 별다른 정보를 제공할 수 없는 객체가 되는 결과를 낳는다.

도메인 객체가 원래 포함했어야 할 지식이나 처리 내용을 모두 도메인 서비스나 애플리케이션 서비스에 빼앗겨 자신이 제공할 수 있는 정보가 없는 도메인 객체를 빈혈 도메인 모델이라고 한다. 이런 객체는 데이터와 행위를 함께 모아 놓는다는 객체 지향 설계의 기본 원칙을 정면으로 거스르는 것이다.

사용자명 변경 처리는 원래대로 하면 User 클래스에 정의해야 할 내용이다(리스트 4-8).

[리스트 4-8] User 클래스에 정의된 사용자명 변경 처리

```java
public class User {
    private final UserId id;
    private UserName name;

    public User(UserId id, UserName name) {
        this.id = id;
        this.name = name;
    }

    public void changeName(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name: " + name);

        user.setName(name);
    }
}
```



#### 4.3.1 도메인 서비스는 가능한 한 피할 것

앞서 보았듯이, 모든 행위를 도메인 서비스에 구현하는 것도 가능하다. 마음만 먹는다면 얼마든지 도메인 객체를 모두 빈혈 도메인 객체로 만들 수 있다.

물론 도메인 서비스로 옮기지 않으면 어색한 행위도 있다. 어떤 행위를 값 객체나 엔티티에 구현할지 아니면 도메인 서비스에 구현할지 망설여진다면 우선 엔티티나 값 객체에 정의하는 것이 좋으며 도메인 서비스에 행위를 구현하는 것은 가능한 한 피해야 한다.

도메인 서비스를 남용하면 데이터와 행위가 단절돼 로직이 흩어지기 쉽다. 로직이 흩어지면 소프트웨어가 변화에 대응하는 유연성이 저해돼 심각하게 정체된다. 소프트웨어의 변경에 대한 유연성을 확보하려면 중복되는 코드를 제거하기 위한 노력을 한시도 포기해서는 안 된다.



### 4.4 엔티티/값 객체와 함께 유스케이스 수립하기

도메인 서비스는 값 객체나 엔티티와 함께 사용된다. 도메인 서비스의 사용법을 살펴보기 위해 실제 유스케이스를 세워보자. 이번에 계획할 유스케이스도 역시 사용자, 그중에서도 사용자 생성 처리를 소재로 한다.

사용자 생성 처리의 명세는 단순하다. 클라이언트가 사용자명을 지정해 사용자 생성 처리를 호출한다. 중복이 없는 사용자명이라면 사용자를 생성해 저장한다. 여기서 사용할 데이터스토어는 일반적인 관계형 데이터베이스를 대상으로 한다.



#### 4.4.1 사용자 엔티티 확인

우선 사용자를 나타내는 User 클래스를 정의한다(리스트 4-9).

[리스트 4-9] User 클래스의 정의

```java
@Getter
public class User {
    private UserId id;
    private UserName name;

    public User(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        id = new UserId(UUID.randomUUID().toString());
        this.name = name;
    }
}
```

사용자는 id로 식별되는 엔티티다. 그리고 사용자 생성 처리 중에는 User 클래스의 행동은 필요치 않으므로 주요 메서드는 여기에 정의하지 않았다.

User 클래스를 구성하는 객체도 살펴보자. User 클래스는 UserId 타입의 식별자를 속성으로 갖는다. 또 사용자명을 나타내는 UserName 타입의 속성도 갖고 있다. 이들을 구현한 코드는 리스트 4-10과 같다.

[리스트 4-10] UserId 클래스와 UserName 클래스의 정의

```java
@Getter
public class UserId {
    private String value;

    public UserId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}

@Getter
public class UserName {
    private String value;

    public UserName(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);
        if (value.length() < 3)
            throw new IllegalArgumentException("사용자명은 3글자 이상어야 함: " + value);

        this.value = value;
    }
}
```

UserId와 UserName은 모두 데이터를 래핑 했을 뿐인 단순한 값 객체다. 다만 UserName은 세 글자 미만의 사용자명에 대해 예외를 발생시켜 사용자명이 세 글자 이상이 되게 강제한다.



#### 4.4.2 사용자 생성 처리 구현

사용자 엔티티와 이를 구성하는 객체의 구현을 살펴보았으니 실제 사용자 생성 처리 과정을 살펴볼 차례다. 리스트 4-11은 사용자 생성 처리를 구현한 예다. 우선 코드를 전체적으로 살펴 보자.

[리스트 4-11] 사용자 생성 처리 구현 예

```java
public class Program {
    public void createUser(String userName) throws Exception {
        User user = new User(new UserName(userName));
        UserService userService = new UserService();
        if (userService.exists(user)) {
            throw new IllegalArgumentException(user.getName().getValue() + "은 이미 존재하는 사용자명임");
        }

        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
        pstmt.setString(1, user.getId().getValue());
        pstmt.setString(2, user.getName().getValue());
        pstmt.executeUpdate();
    }
}
```

앞부분의 코드는 주의 깊게 읽지 않아도 우선 사용자를 생성한 다음 중복 확인을 거치는 내용을 이해할 수 있다. 그러나 그 뒤에 오는 처리는 다르다.

후반부의 코드는 앞의 코드와 달리 눈으로 훑어서는 의도를 파악하기 어렵다. 코드를 자세히 뜯어보면 관계형 데이터베이스에 접속하기 위한 접속 문자열을 사용해 데이터스토어에 접속한 다음 SQL문을 통해 사용자 정보를 저장하는 내용이라는 것까지는 알 수 있다. 그 앞의 사용자 생성 및 중복 확인 코드와 비교하면, 대부분의 코드가 데이터스토어에 접근하기 위한 구체적인 과정을 작성한 것이다. 코드 자체는 그리 어려운 코드가 아니지만, 생성한 사용자를 저장한다는 의도를 파악하기 위해서는 코드를 자세히 봐야 한다.

[리스트 4-12] 도메인 서비스의 구현 예

```java
public class UserService {
    public boolean exists(User user) throws SQLException {
        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        pstmt.setString(1, user.getName().getValue());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return true;
        }

        return false;
    }
}
```

사용자명이 중복되는지 확인하려면 데이터스토어에 중복 여부를 물어봐야 한다. 이 때문에 UserService 클래스의 사용자명 중복 확인 처리는 처음부터 끝까지 데이터스토어를 다룬다.

이 코드는 바르게 동작하기는 하지만 유연성이 부족하다. 예를 들어 데이터스토어를 관계형 데이터베이스에서 NoSQL 데이터베이스로 바꿀 필요가 생겼다면 사용자 생성 처리는 본질적으로 아무 변화가 없음에도 코드의 대부분을 수정해야 할 것이다. 특히 UserService 클래스는 모든 코드를 NoSQL 데이터베이스를 다루는 코드로 수정하지 않으면 안 된다.

데이터를 다뤄야 하는 이상 데이터를 실제 읽고 쓰는 처리를 구현하지 않을 수는 없다. 그러나 사용자 생성 처리를 담당하는 코드의 태반이 데이터스토어를 다루는 코드, 그것도 특정한 데이터스토어에 의존적인 코드여야 할까?

물론 그렇지 않다. 사용자 생성 처리의 본질은 '사용자를 생성하는 것'과 '사용자명 중복 여부를 확인하는 것', 그리고 '생성된 사용자 데이터를 저장하는 것'이다. 코드로 나타내야 할 것은 이런 본질적인 내용이지 특정 데이터스토어를 직접 다루는 내용이어서는 안 된다.

소프트웨어 시스템에서 데이터를 저장하는 처리는 꼭 필요하다. 그러나 이와 관련된 코드를 그대로 작성하면 그 처리의 의미가 잘 드러나지 않는다. 다음 장에서 설명할 리포지토리 패턴으로 이러한 문제를 해결할 수 있다.



[칼럼] 도메인 서비스의 기준

> 도메인 서비스는 도메인 모델을 코드상에 나타냈다는 점에서 값 객체, 엔티티와 같다. 이 때문에 도메인 서비스가 입출력을 포함하고 처리를 다뤄서는 안된다고 생각하는 의견도 있다. 이 의견을 따른다면 앞서 봤듯이 '사용자명의 중복'을 확인하는 처리를 도메인 서비스로 구현하는 것은 잘못된 일이 된다.
>
> 데이터스토어는 본래 도메인에는 없는 존재로, 애플리케이션 구축을 위해 추가된 애플리케이션만의 관심사다. 그러므로 도메인 개념이나 지식을 코드로 옮긴 대상인 도메인 객체가 데이터스토어를 직접 다루는 것은 바람직하지 못하다. 도메인 객체는 오로지 도메인 모델만을 나타내야 한다. 하지만 필자의 의견은 이와 다르기 때문에 이 칼럼의 제목을 이렇게 지었다.
>
> 개인적으로 어떤 처리를 도메인 서비스로 만들어야 할지를 판단할 때 그 처리가 도메인에 기초한 것인지를 주요하게 본다. '사용자명 중복'이라는 개념이 도메인에 기초한 것이라면 이를 구현하는 서비스도 도메인 서비스여야 한다. 반대로 애플리케이션을 만들며 필요하게 된 것이라면 도메인 서비스가 아니다. 그런 처리는 애플리케이션 서비스로 정의해야 한다.
>
> 물론 입출력을 가능한 한 도메인 서비스로 다루지 말아야 한다는 점에는 필자도 동의한다. 이 점을 고려한 상태에서 필요하다면 입출력이 포함된 처리를 도메인 서비스로 만들 수도 있다는 얘기다.



### 4.5 물류 시스템의 도메인 서비스 예


도메인 서비스 중에는 데이터스토어와 같은 인프라스트럭처와 엮이지 않고 도메인 객체만 다루는 것도 있다. 오히려 그런 도메인 서비스가 진짜 도메인 서비스라고 할 수 있다. 잠시 원래 주제에서 벗어나 사용자명 중복 확인 외의 다른 도메인 서비스의 예를 살펴보자.

이번에 살펴볼 소재는 물류 시스템이다.

물류 시스템에서는 화물이 직접 배송지로 보내지는 것이 아니라, 현재 거점에서 배송지에 가까운 거점을 거쳐 배송된다(그림 4-1).



<img src="chapter-04.assets/image-20201130221638050.png" alt="image-20201130221638050" style="zoom:50%;" />

[그림 4-1] 물류 배송 과정

이 개념을 코드로 옮겨 보겠다.



#### 4.5.1 거점의 행동으로 정의하기

그림 4-1을 보면 거점이라는 단어가 나온다. 거점은 물류 도메인에서 중요한 개념으로 엔티티 형태로 정의된다(리스트 4-13).

[리스트 4-13] 거점 엔티티

```java
public class PhysicalDistributionBase {

  	(...생략...)
    public Baggage ship(Baggage baggage) {
	    	(...생략...)
    }

    public void receive(Baggage baggage) {
        (...생략...)
    }
}
```

거점은 출고(ship)와 입고(receive)라는 행위를 갖는다. 출고와 입고는 함께 다뤄져야 하는 활동이다. 출고된 적이 없는 가공의 화물이 입고되거나 출고된 채 사라지는 경우가 생겨서는 안 된다. 실제 현장에서는 물리 법칙에 따라 출고와 입고가 확실히 함께 일어나지만, 프로그램에서는 그렇지 않다. 그러므로 출고와 입고가 빠짐없이 함께 일어나도록 '운송'처리를 갖춰야 한다.

운송 처리를 준비하기 전에 운송 처리를 어디에 구현해야 할지 생각해 보자. 거점에서 거점으로 화물이 이동하는 운송은 거점에서 시작된다. 거점에 운송 처리를 정의해 보자(리스트 4-14).

[리스트 4-14] 거점 클래스에 정의된 운송의 행동

```java
public class PhysicalDistributionBase {
		(... 생략 ...)
    public void transport(PhysicalDistributionBase to, Baggage baggage) {
        Baggage shippedBaggage = ship(baggage);
        to.receive(shippedBaggage);

        // 운송 기록 같은 것도 필요할 것이다.
    }
}
```

리스트 4-14에 구현된 처리 내용 자체는 문제없이 완료될 것이다. transport 메서드를 이용하는 한 출고와 입고는 함께 이루어진다.  그러나 거점에서 거점으로 직접 화물이 이동하는 것은 어딘가 찜찜하다. 그리고 현재 구현된 코드는 맥락에 대한 요소를 극도로 생략한 간단한 예제다. 실제라면 리스트 4-14의 주석 내용처럼 운송 기록 같은 처리가 더 필요할 수도 있다. 이들 처리 모두 거점 객체가 수행해야 한다면 어딘가 어색하면서 다루기도 까다로울 것이다.



#### 4.5.2 운송 도메인 서비스 정의하기

운송이라는 행위는 아무래도 특정 객체의 행위로 정의하기에는 들어맞지 않는 부분이 있는듯 하다. 이번에는 거점 대신 운송을 맡을 별도의 도메인 서비스로 정의해 보자(리스트 4-15).

[리스트 4-15] 운송 도메인 서비스

```java
public class TransportService {
    public void tranport(PhysicalDistributionBase from ,PhysicalDistributionBase to, Baggage baggage) {
        Baggage shippedBaggage = from.ship(baggage);
        to.receive(baggage);

        // 운송 기록 남김
      	(...생략...)
    }
}
```

리스트 4-14의 코드에서 느껴지던 찜찜함이 사라졌다. 이제 배송 기록을 남겨야 할 필요가 생겨도 위화감 없이 처리할 수 있다.

어떤 처리를 객체 안에 정의했을 때 잘 들어맞지 않는 느낌이 든다면 이 처리를 도메인 서비스로 옮기면 자연스럽게 나타낼 수 있다.



[칼럼] 도메인 서비스에 이름을 붙이는 규칙

> 도메인 서비스에 이름을 붙이는 규칙은 다음 세 가지다.
>
> 1) 도메인 개념
> 2) 도메인 개념 + Service
> 3) 도메인 개념 + DomainService
>
> 서비스는 도메인 활동을 대상으로 하는 경우가 많으며, 동사에서 따온 이름을 쓰는 경우가 많다.
>
> 필자는 접미사 Service를 붙이는 두 번째 규칙을 자주 적용한다. 접미사로 DomainService 대신 Service를 쓰는 이유는 도메인 서비스 자체도 서비스이며, 그 성격에 따라 도메인 서비스가 될 뿐이라 생각하기 때문이다. 구체적으로 XxxDomain.Services.XXXService 네임스페이스를 부여해 XxxService가 도메인 서비스임을 보여줄 수 있다.
>
> '사용자명 중복 확인'처럼 특정 객체와 밀접하게 연관된 서비스는 UserService와 같이 도메인 객체 이름에 Service를 붙여 이름을 정하고 이 서비스에 처리를 모아 놓는다. 만약 '사용자명 중복을 확인하는'일을 별도의 서비스로 독립시켜야 한다면 CheckDuplicateUserService라는 클래스를 만드는 경우도 있다.
>
> 첫 번째 규칙처럼 도메인 개념만으로 이름을 정하는 것이 표현으로서는 더 적절하겠으나, 이런 경우 항상 이 클래스가 서비스임을 염두에 둬야 한다.
>
> 세 번째 규칙은 도메인 서비스임을 강조하기 위한 이름이다. 해당 클래스의 코드만으로 알기 쉽다는 점에서 다른 방법보다 낫다.
>
> 도메인 서비스라는 것을 팀원 전원이 함께 인식할 수 있다면 이중 어느 규칙을 사용해도 무방하다.



### 4.6 정리

이번 장에서 도메인 서비스에 대해 알아봤다.

도메인에는 도메인 객체에 구현하기에 자연스럽지 못한 행위가 있다. 이런 행위는 여러 개의 도메인 객체를 가로질러 이뤄지는 처리인 경우가 많다. 도메인 서비스는 이럴 때 활용하는 객체다.

서비스는 이모저모로 편리한 존재다. 도메인 객체에 구현해야 할 행위를 마음만 먹는다면 모두 서비스로 옮길 수 있다. 빈혈 도메인 모델이 생기지 않으려면 어떤 행위를 어디에 구현해야 할지 세심하게 신경 써야 한다. 행위가 빈약한 객체는 절차적 프로그래밍으로 빠지기 쉽기 때문에 도메인 지식을 객체의 행위로 나타낼 기회를 읽게 된다.

지금까지 배운 내용을 통해 값 객체, 엔티티, 도메인 서비스 등 기본적인 도메인 개념을 나타낼 수 있는 수단을 갖췄다. 그리고 이번 장에서 이들을 사용해 유스케이스를 수립하는 경험도 해봤다.

그러나 이와 함께 한 가지 문제점도 살펴봤다. 유스케이스가 처음부터 끝까지 데이터스토어를 다뤄야 한다는 점이다. 다음 장에서 배울 리포지토리는 이러한 문제를 해결해주는 패턴이다.

  

  

 