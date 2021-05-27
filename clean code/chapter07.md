# 7장 오류 처리

여기 저기 흩어진 오류 처리 코드 때문에 실제 코드가 하는 일을 파악하기가 거의 불가능하다는 의미다. 오류 처리는 중요하다. 하지만 <u>오류 처리 코드로 인해 프로그램 논리를 이해하기 어려워진다면 깨끗한 코드라 부르기 어렵다.</u>



###### 오류 코드보다 예외를 사용하라

```java
public class DeviceController {
    public void sendShutDown() {
        DeviceHandle handle = getHandle(DEV1);
        // 디바이스 상태를 점검한다
        if (handle != DeviceHandle.INVALID) {
            // 레코드 필드에 디바이스 상태를 저장한다.
            retrieveDeviceRecord(handle);
            // 디바이스가 일시정지 상태가 아니라면 종료한다.
            if (record.getStatus() != DEVICE_SUSPENDED) {
                pauseDevice(handle);
                clearDeviceWorkQueue(handle);
                closeDevice(handle);
            } else {
                logger.log("Device suspended. Unable to shut down");
            }
        } else {
            logger.log("Invalid handle for: " + DEV1.toString());
        }
    }
}
```

위와 같은 방법을 사용하면 호출자 코드가 복잡해진다. 그래서 오류가 발생하면 예외를 던지는 편이 낫다. 그러면 호출자 코드가 더 깔끔해진다. 논리가 오류 처리 코드와 뒤섞이지 않으니까.

다음은 오류를 발견하면 예외를 던지는 코드다.

```java
public class DeviceController {
  ...
  public void sendShutDown() {
    try {
      tryToShutDown();
    } catch (DeviceShutDownError e) {
      logger.log(e);
    }
  }
  
  public void tryToShutDown() throws DeviceShutDownError {
    ...
  }
}
```

코드가 확실히 깨끗해지지 않았는가! 단순히 보기만 좋아지지 않았다. 코드 품질도 나아졌다.
디바이스를 종료하는 알고리즘과 오류를 처리하는 알고리즘을 분리했기 때문이다.



##### Try-Catch-Finally 문부터 작성하라

테스트케이스 작성 시 try, catch, finally로 작성하라.



##### 미확인(unchecked) 예외를 사용하라

자바 첫 버전이 확인된 예외를 선보였던 당시는 확인된 예외가 멋진 아이디어로 여겨졌다.
확인된 예외는 OCP를 위반한다. 하위 단계에서 코드를 변경하면 상위 단계 메서드 선언부를 전부 고쳐야 한다는 말이다.
때로는 확인된 예외는 유용하다. 아주 중요한 라이브러리를 작성한다면 모든 예외를 잡아야 한다. 하지만 일반적인 애플리케이션은 의존성이라는 비용이 이익보다 크다.



##### 예외에 의미를 제공하라

예외를 던질 때는 전후 상황을 충분히 덧붙인다. 그러면 오류가 발생한 원인과 위치를 찾기가 쉬워진다.
오류 메시지에 정보를 담아 예외와 함께 던진다. 실패한 연산 이름과 실패 유형도 언급한다.



##### 호출자를 고려해 예외 클래스를 정의하라

애플리케이션에서 오류를 정의할 때 프로그래머에게 가장 중요한 관심사는 오류를 잡아내는 방법이 되어야 한다.

```java
ACMEPort port = new ACMEPort(12);

try {
  port.open();
} catch (DeviceResponseException e) {
  reportPortError(e);
  logger.log("Device response exception", e);
} catch (ATM1212UnlockedExcepiton e) {
  reportPortError(e);
  logger.log("Unlock exception", e);
} catch (GMXError e) {
  reportPortError(e);
  logger.log("Device response exception");
} finally {
  ...
}
```

위 코드는 중복이 심하지만 그리 놀잡지 않다. 대다수 상황에서 우리가 오류를 처리하는 방식은 비교적 일정하다.
호출하는 라이브러리 API를 감싸면서 예외 유형 하나를 반환하면 된다.

```java
LocalPort port = new LocalPort(12);
try {
  port.open();
} catch (PortDeviceFailure e) {
  reportError(e);
  logger.log(e.getMessage(), e);
} finally {
  ...
}
```

여기서 LocalPort 클래스는 단순히 ACMEPort 클래스가 던지는 예외를 잡아 변환하는 감싸기 클래스일 뿐이다.

```java
public class LocalPort {
  private ACMEPort innerPort;
  
  public LocalPort(int portNumber) {
    intPort = new ACMDPort(portNumber);
  }
  
  try {
    port.open();
  } catch (DeviceResponseException e) {
    throw new PortDeviceFailure(e);
  } catch (ATM1212UnlockedExcepiton e) {
    throw new PortDeviceFailure(e);
  } catch (GMXError e) {
    throw new PortDeviceFailure(e);
  }
  ...
}
```

LocalPort 클래스처럼 ACMEPort를 감싸는 클래스는 매우 유용하다.

흔히 예외 클래스가 하나만 있어도 충분한 코드가 많다. 



##### 정상 흐름을 정의하라

앞 절에서 충고한 지침을 충실히 따른다면 비즈니스 논리와 오류 처리가 잘 분리된 코드가 나온다.



##### null을 반환하지 마라

```java
public void registerItem(Item item) {
    if (item != null) {
        ItemRegistry registry = persistentStore.getItemRegistry();
        if (registry != null) {
          Item existing = registry.getItem(item.getID());
          if (existing.getBillingPeriod().hasRetailOwner()) {
            existing.register(item);
          }
        }
    }
}
```

이런 코드 기반에서 코드를 짜왔다면 나쁘다고 느끼지 않을지도 모르겠다. 하지만 위 코드는 나쁜 코드다. 누구 하나라도 null 확인을 빼먹는다면 애플리케이션이 통제 불능에 빠질지도 모른다.

위 코드에서 둘째 행에 null 확인이 빠졌다는 사실을 눈치챘는가?
위 코드는 null 확인이 누락된 문제라 말하기 쉽다. 하지만 실상은 null확인이 너무 많아 문제다.

다음 코드를 보자.

```java
List<Employee> employees = getEmployee();
if (employees != null) {
  for (Employee e : employees) {
    totalPays += e.getPay();
  }
}
```

위에서 getEmployees는 null도 반환한다. 하지만 반드시 null을 반환할 필요가 있을까? getEmployees를 변경해 빈 리스트를 반환한다면 코드가 훨씬 깔끔해진다.

```java
List<Employee> employees = getEmployee();
  for (Employee e : employees) {
    totalPays += e.getPay();
}
```

다행스럽게 자바에는 Collections.emptyList()가 있어 읽기 전용 리스트를 반환한다.

```java
public List<Employee> getEmployees() {
  if ( .. 직원이 없다면 ..) 
    return Collections.emptyList();
}
```

이렇게 코드를 변경하면 코드도 깔끔해질뿐더라 NullPointerException이 발생할 가능성도 줄어든다.



##### null을 전달하지 마라

메서드에서 null을 반환하는 방식도 나쁘지만 메서드로 null을 전달하는 방식은 더 나쁘다.

다음은 두 지점 사이의 거리를 계산하는 간단한 메서드다.

```java
public class MetricsCalculator {
  public double xProjection(Point p1, Point p2) {
    return (p2.x - p1.x) * 1.5;
  }
  ...
}
```

누군가 인수로 null을 전달하면 어떤 일이 벌어질까?

```java
calculator.xProjection(null, new Point(12, 13));
```

당연히 NullPointerException이 발생한다.

어떻게 고치면 좋을까?

```java
public class MetricsCalculator {
  public double xProjection(Point p1, Point p2) {
    if (p1 == null || p2 = null) {
      throw InvalidArgumentException("Invalid argument for MetricsCalculator.xProjection");
    }
    return (p2.x - p1.x) * 1.5;
  }
  ...
}
```

대다수 프로그래밍 언어는 호출자가 실수로 넘기는 null을 적절히 처리하는 방법이 없다. 즉, 애초에 null을 넘기지 못하도록 금지하는 정책이 합리적이다. 즉, 인수로 null이 넘어오면 코드에 문제가 있다는 말이다.



##### 결론

오류 처리를 프로그램 논리와 분리하면 독립적인 추론이 가능해지며 코드 유지보수성도 크게 높아진다.





















