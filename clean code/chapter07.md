# 7 오류 처리

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

위와 같은 방법을 사용하면 호출자 코드가 복잡해진다.



##### Try-Catch-Finally 문부터 작성하라



##### 미확인(unchecked) 예외를 사용하라

확인된 예외는 OCP를 위반한다.



##### 예외에 의미를 제공하라



##### 호출자를 고려해 예외 클래스를 정의하라



##### 정상 흐름을 정의하라



##### null을 반환하지 마라

```java
public void registerItem(Item item) {
    if (item != null) {
        ItemRegistry registry = persistentStore.getItemRegistry();
        if (registry != null) {
            ...
        }
    }
}
```

위 코드는 나쁜 코드다. 누구 하나라도 null 확인을 빼먹는다면 애플리케이션이 통제 불능에 빠질지도 모른다.



##### null을 전달하지 마라

메서드에서 null을 반환하는 방식도 나쁘지만 메서드로 null을 전달하는 방식은 더 나쁘다.

애초에 null을 넘기지 못하도록 금지하는 정책이 합리적이다. 즉, 인수로 null이 넘어오면 코드에 문제가 있다는 말이다.



##### 결론

오류 처리를 프로그램 논리와 분리하면 독립적인 추론이 가능해지며 코드 유지보수성도 크게 높아진다.





















