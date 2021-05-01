# 8장 경계

```java
Map sensors = new HashMap();

Sensor s = (Sensor) sensors.get(sensorId);
```

이 코드를 다음 처럼 제네릭을 사용하면 코드 가독성이 크게 높아진다.

```java
Map<String, Sensor> sensors = new HashMap<Sensor>();
Sensor s = sensors.get(sensorId);
```

하지만 위 방법도 "Map<String, Sensor>가 사용자에게 필요하지 않는 기능까지 제공한다"는 문제는 해결하지 못한다.

더 깔끔하게 사용한 코드다.

```java
public class Sensors {
    private Map sensors = new HashMap();
    
    public Sensor getById(String id) {
        return (Sensor) sensors.get(id);
    }
}
```

Map을 여기저기 넘기지 말라는 말이다. Map 같은 경계 인터페이스를 이용할 때는 이를 이용하는 클래스나 클래스 계열 밖으로 노출되지 않도록 주의한다.



##### 경계 살피고 익히기

외부 코드를 사용하면 적은 시간이 더 많은 기능을 출시하기 쉬워진다.

외부 코드를 익히기는 어렵다. 우리 코드를 작성해 외부 코드를 호출하는 대신 먼저 간단한 테스트 케이스를 작성해 외부 코드를 익히면 어떨까? 짐 뉴커크는 이를 **학습 테스트**라 부른다.



##### log4j 익히기



##### 학습 테스트는 공짜 이상이다

학습 테스트는 이해도를 높여주는 정확한 실험이다. 투자하는 노력보다 얻는 성과가 더 크다.



##### 아직 존재하지 않는 코드를 사용하기



##### 깨끗한 경계

변경이 대표적인 예다. 

소프트웨어 설계가 우수하다면 변경하는데 많은 투자와 재작업이 필요하지 않다. 엄청난 시간과 노력과 재작을 요구하지 않는다.

경계에 위치하는 코드는 깔끔하게 분리한다. 또한 기대치를 정의하는 테스트 케이스도 작성한다.









