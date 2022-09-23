# chapter 5. 가독성 높은 코드를 작성하라

가독성은 본질적으로 주관적인 것이며 그것이 정확히 무엇을 의미하는지 확실하게 정의하기 어렵다.

가독성의 핵심은 개발자가 코드의 기능을 빠르고 정확하게 이해할 수 있도록 하는 것이다.



## 5.1 서술형 명칭 사용

토스터라는 단어는 부엌에 있는 가전제품을 고유하게 식별하면서 그것이 무엇을 하는지도 많은 힌트를 준다. 즉, 그것은 무엇인가를 굽는 것이다. 만약 토스터를 토스터라고 부르는 대신 '객체 A'라고 주장한다면 '객체 A'가 정확히 무엇이고 무엇을 하는지 기억하기 쉽지 않을 것이다.



### 5.1.1 서술적이지 않은 이름은 코드를 읽기 어렵게 만든다.

```java
public class T {
    Set<String> pns = new HashSet<>();
    int s = 0;

    private boolean f(String n) {
        return pns.contains(n);
    }

    private int getS() {
        return this.s;
    }

    private int s(List<T> ts, String n) {
        for (T t : ts) {
            if (t.f(n)) {
                return t.getS();
            }
        }

        return -1;
    }
}
```

만약 이 코드가 무엇을 하는지 설명하라는 요청을 받는다면 뭐라고 답하겠는가?



### 5.1.2 주석문으로 서술적인 이름을 대체할 수 없다.

이를 개선할 수 있는 한 가지 방법은 주석문과 문서를 추가하는 것이다. 하지만 다음과 같은 문제가 있다.

* 코드가 훨씬 복잡해 보인다. 코드뿐만 아니라 주석문과 문서도 유지보수해야 한다.
* 코드를 이해하기 위해 위아래로 스크롤해야 한다.
* 함수 s()의 내용을 확인할 때 클래스 T를 살펴보지 않는 한 t.f(n)와 같은 호출이 무엇을 하는지 또는 무엇이 반환되는지 알기 어렵다.

```java
/** 팀을 나타낸다. */
public class T {
    Set<String> pns = new HashSet<>(); // 팀에 속한 선수의 이름
    int s = 0; // 팀의 점수

    /**
     * @param n 플레이어의 이름
     * @return true 플레이어가 팀에 속해 있는 경우
     */
    private boolean f(String n) {
        return pns.contains(n);
    }

    /**
     * @return 팀의 점수
     */
    private int getS() {
        return this.s;
    }

    /**
     * @param ts 모든 팀의 리스트
     * @param n 플레이어의 이름
     * @return 플레이어가 속해 있는 팀의 점수
     */
    private int s(List<T> ts, String n) {
        for (T t : ts) {
            if (t.f(n)) {
                return t.getS();
            }
        }

        return -1;
    }
}
```



### 5.1.3 해결책: 서술적인 이름 짓기

```java
public class Team {
    Set<String> playerNames = new HashSet<>();
    int score = 0;

    private boolean containsPlayer(String playerName) {
        return playerNames.contains(playerName);
    }

    private int getScore() {
        return this.score;
    }

    private int getTeamScoreForPlayer(List<Team> teams, String playerName) {
        for (Team team : teams) {
            if (team.containsPlayer(playerName)) {
                return team.getScore();
            }
        }

        return -1;
    }
}
```

* 변수, 함수 및 클래스가 별도로 설명할 필요가 없이 자명하다.
* Team.containPlayer(playName)과 같은 호출이 무엇을 하는지 무슨 값을 반환하는지 Team 클래스를 확인하지 않더라도 분명하게 알 수 있다.



## 5.2 주석문의 적절한 사용

코드 내에서 주석문이나 문서화는 다음과 같은 다양한 목적을 수행할 수 있다.

* 코드가 무엇을 하는지 설명
* 코드가 왜 그 일을 하는지 설명
* 사용 지침 등 기타 정보 제공



### 5.2.1 중복된 주석문은 유해할 수 있다.

코드가 수행하는 작업을 설명하는 주석문

```java
String generateId(String firstName, String lastName) {
  // "{이름}.{성}"의 형태로 ID를 생성한다.
  return firstName + "." + lastName;
}
```



### 5.2.2 주석문으로 가독성 높은 코드를 대체할 수 없다.

주석문이 있는 이해하기 어려운 코드

```java
String generateId(String[] data) {
  // data[0]는 유저의 이름이고 data[1]은 성이다.
  // "{이름}.{성}"의 형태로 ID를 생성한다.
  return data[0] + "." + data[1];
}
```



가독성이 더 좋아진 코드

```java
String generateId(String [] data) {
  return firstName(data) + "." + lastName(data);
}

String firstName(String [] data) {
  return data[0];
}

String lastName(String [] data) {
  return data[1];
}
```



### 5.2.3 주석문은 코드의 이유를 설명하는 데 유용하다.

다음과 같은 경우에는 주석문을 사용해 코드가 존재하는 이유를 설명하면 좋다.

* 제품 또는 비즈니스 의사 결정
* 이상하고 명확하지 않은 버그에 대한 해결책
* 의존하는 코드의 예상을 벗어나는 동작에 대처



코드가 존재하는 이유를 설명하는 주석문

```java
public class User {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final Version signupVersion;

    public User(String username, String firstName, String lastName, Version signupVersion) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signupVersion = signupVersion;
    }

    private String getUserId() {
        if (signupVersion.isOlderThan("2.0")) {
            // (v2.0 이전에 등록한) 레거시 유저라는 이름으로 ID가 부여된다.
            // 자세한 내용은 #4218 이슈를 보라.
            return firstName.toLowerCase() + "." + lastName.toLowerCase();
        }
        // (v2.0 이후로 등록한) 새 유저는 username으로 ID가 부여된다.
        return username;
    }
}
```



### 5.2.4 주석문은 유용한 상위 수준의 요약 정보를 제공할 수 있다.

클래스에 대한 상위 수준의 문서화

```java
/**
 * 스트리밍 서비스의 유저에 대한 자세한 사항을 갖는다.
 * 
 * 이 클래스는 데이터베이스에 직접 연결하지 않는다. 대신 메모리에 저장된 값으로 
 * 생성된다. 따라서 이 클래스가 생성된 이후에 데이터베이스에서 이뤄진 변경 사항을 
 * 반영하지 않을 수 있다.
 */
public class User {
  ...
}
```



### 5.3 코드 줄 수를 고정하지 마라.

### 5.3.1 간결하지만 이해하기 어려운 코드는 피하라

간결하지만 이해하기 어려운 코드

```java
private boolean inIdValid(int id)  {
  return countSetBits(id & 0x7FFF) % 2 == (id & 0x8000) >> 15);
}
```



### 5.3.2 해결책: 더 많은 줄이 필요하더라도 가독성 높은 코드를 작성하라.



## 5.4 일관된 코딩 스타일을 고수하라

### 5.4.1 일관적이지 않은 코딩 스타일은 혼동을 일으킬 수 있다.

일관성 없는 명명 스타일

```java
public class GroupChat {
  ...
    
    void end() {
    	connectionManager.terminateAll(); // connectionManager는 인스턴스 변수라고 가정한다.
  }
}
```

connectionManager는 인스턴스 변수가 아니라 클래스 이름이고 terminateAll()은 그 클래스의 정적 함수다.



### 5.4.2 해결책: 스타일 가이드를 채택하고 따르라.

일관된 명명 스타일

```java
public class GroupChat {
  ...
    
    void end() {
    	ConnectionManager.terminateAll(); // connectionManager는 인스턴스 변수라고 가정한다.
  }
}
```



## 5.5 깊이 중첩된 코드를 피하라

일반적으로 코드는 다음과 같이 서로 중첩되는 블록으로 구성된다.

* 함수가 호출되면 그 함수가 실행되는 코드가 하나의 블록이 된다.
* if 문의 조건이 참일 때 실행되는 코드는 하나의 블록이 된다.
* for 루프의 각 반복 시 실행되는 코드는 하나의 블록이 된다.



### 5.5.1 깊이 중첩된 코드는 일기 어려울 수 있다

깊이 중첩된 if문

```java
private Address getOwnersAddress(Vehicle vehicle) {
  if (vehicle.hasBeenScraped()) {
    return SCRAPYARD_ADDRESS;
  } else {
    Purchase mostRecentPurchase = vehicle.getMostRecentPurchase();
    if (mostRecentPurchase == null) {
      return SHOWROOM_ADDRESS;
    } else {
      Buyer buyer = mostRecentPurchase.getBuyers();
      if (buyers != null) {
        return buyer.getAddress();
      }
    }
  }
  
  return null;
}
```



### 5.5.2 해결책: 중첩을 최소화하기 위한 구조 변경

중첩이 최소화된 코드

```java
private Address getOwnersAddress(Vehicle vehicle) {
  if (vehicle.hasBeenScraped()) {
    return SCRAPYARD_ADDRESS;
  }
  Purchase mostRecentPurchase = vehicle.getMostRecentPurchase();
  if (mostRecentPurchase == null) {
    return SHOWROOM_ADDRESS;
  }
  Buyer buyer = mostRecentPurchase.getBuyers();
  if (buyers != null) {
    return buyer.getAddress();
  }
  return null;
}
```



### 5.5.3 중첩은 너무 많은 일을 한 결과물이다.

너무 많은 일을 하는 함수

```java
private SentConfirmation sendOwnerALetter(Vehicle vehicle, Letter letter) {
  Address ownersAddress = null;
  if (vehicle.hasBeenScraped()) {
    ownersAddress = SCRAPYARD_ADDRESS;
  } else {
    Purchase mostRecentPurchase = vehicle.getMostRecentPurchase();
  }
  if (mostRecentPurchase == null) {
    return SHOWROOM_ADDRESS;
  } else {
    Buyer buyer = mostRecentPurchase.getBuyers();
    if (buyers != null) {
      return buyer.getAddress();
    }
  }
  if (ownersAddress == null) {
    return null;
  }
  return sendLetter(ownersAddress, letter);
  
}
```



### 5.5.4 해결책: 더 작은 함수로 분리

더 작은 함수

```java
private SentConfirmation sendOwnerALetter(Vehicle vehicle, Letter letter) {
  Address ownersAddress = getOwnersAddress(vehicle);
  if (ownersAddresss != null) {
    return sendLetter(ownersAddress, letter);
  }
  return null;
}

private Address getOwnersAddress(Vehicle vehicle) {
  if (vehicle.hasBeenScraped()) {
    ownersAddress = SCRAPYARD_ADDRESS;
  }
  Purchase mostRecentPurchase = vehicle.getMostRecentPurchase();
  if (mostRecentPurchase == null) {
    return SHOWROOM_ADDRESS;
  }
  Buyer buyer = mostRecentPurchase.getBuyers();
    if (buyers == null) {
      return null;
    }
  return buyer.getAddress();
}
```



## 5.6 함수 호출도 가독성이 있어야 한다.

### 5.6.1 매개변수는 이해하기 어려울 수 있다.

```java
sendMessage("hello", 1, true);
```

sendMessage에서 1과 true가 무엇을 의미하는지 알려면 함수 정의를 살펴봐야 한다.

```java
private void sendMessage(String message, int priority, boolean allowRetry) {
  ...
}
```



### 5.6.2 해결책: 명명된 매개변수 사용

```java
sendMessage(message: "hello", priority: 1, allowRetry: true);
```



### 5.6.3 해결책: 서술적 유형 사용

```java
sendMessage("hello", new MessagePriority(1), RetryPolicy.ALLOW_RETRY);
```



### 5.6.4 때로는 훌륭한 해결책이 없다.

```java
BoundingBox box = new BoundingBox(
  /* top= */ 10,
  /* right= */ 50,
  /* bottom= */ 20,
  /* left= */ 5;
)
```



### 5.6.5 IDE는 어떤가?

실제 코드

```java
sendMessage("hello", 1, true);
```

IDE가 보여주는 코드

```java
sendMessage(message: "hello", priority: 1, allowRetry: true);
```



## 5.7 설명되지 않은 값을 사용하지 말라

### 5.7.1 설명되지 않은 값은 혼란스러울 수 있다.

```java
public class Vehicle {
  ...
  private double getMassUsTon() { ... }
  private double getSpeedMph() { ... }

  // 차량의 현재 운동에너지를 줄 단위로 반환한다.
  private double getKineticEnergyJ() {
    return 0.5 * getMssUsTon() * 907.1847 * // 미국톤/킬로그램 변환 개수에 대한 설명이 없다.
      Math.pow(getSpeedMph() * 0.44704, 2); // 시간당 마일/초당 미터 변환 개수에 대한 설명이 없다.
  }
}
```



### 5.7.2 해결책: 잘 명명된 상수를 사용하라

잘 명명된 상수

```java
public class Vehicle {
  private const double KILOGRAMS_PER_US_TON = 907.1847;
  private const double METERS_PER_SECOND_PER_MPH = 0.44704;
  ...
  private double getMassUsTon() { ... }
  private double getSpeedMph() { ... }

  // 차량의 현재 운동에너지를 줄 단위로 반환한다.
  private double getKineticEnergyJ() {
    return 0.5 * getMssUsTon() * KILOGRAMS_PER_US_TON * 
      Math.pow(getSpeedMph() * METERS_PER_SECOND_PER_MPH, 2); 
  }
}
```



### 5.7.3 해결책: 잘 명명된 함수를 사용하라

값을 제공하는 잘 명명된 함수

```java
public class Vehicle {
  ...
  // 차량의 현재 운동에너지를 줄 단위로 반환한다.
  private double getKineticEnergyJ() {
    return 0.5 *
      getMssUsTon() * kilogramPerUsTon() * 
      Math.pow(getSpeedMph() * metersPerSecondPerMph(), 2); 
  }
  
 private static double kilogramPerUsTon() {
   return 907.1847;
 }
  
  private static double metersPerSecondPerMph() {
    return 0.44704;
  }
}
```



## 5.8 익명함수를 적절하게 사용하라.

### 5.8.1 익명 함수는 간단한 로직에 좋다.

### 5.8.2 익명 함수는 가독성이 떨어질 수 있다.

### 5.8.3 해결책: 대신 명명 함수를 사용하라

### 5.8.4 익명 함수가 길면 문제가 될 수 있다.

### 5.8.5 해결책: 긴 익명 함수를 여러 개의 명명 함수로 나누라



## 5.9 프로그래밍 언어의 새로운 기능을 적절하게 사용하라

### 5.9.1 새 기능은 코드를 개선할 수 있다

전통적인 자바 코드로 작성된 리스트 필터링

```java
List<String> getNotEmptyStrings(List<String> strings) {
  List<String> nonEmptyStrings = new ArrayList<>();
  for (String str: strings) {
    if (!str.isEmpty()) {
      nonEmptyStrings.add(str);
    }
  }
  return nonEmptyStrings;
}
```



스트림을 사용한 리스트 필터링

```java
List<String> getNotEmptyStrings(List<String> strings) {
  return strings
    .streams()
    .filter(str -> !str.isEmpty())
    .collect(toList());
}
```



### 5.9.2 불분명한 기능은 혼동을 일으킬 수 있다

자바 스트림에 익숙하지 않다면 사용하지 않는 것이 좋을 수도 있다.



### 5.9.3 작업에 가장 적합한 도구를 사용하라

맵의 자료구조에서 어떤 값을 찾아야 한다면

```java
String value = map.get(key);
```

맵을 스트림으로 가져와서 키를 기반으로 필터링함으로써 문제를 해결할 수도 있다.

스트림을 사용하여 맵에서 값을 얻기

```java
Optional<String> value = map
  .entrySet()
  .stream()
  .filter(entry -> entry.getKey().equals(key))
  .map(Entry::getValue)
  .findFirst();
```

