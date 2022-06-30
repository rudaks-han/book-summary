> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 싱글톤(Singleton) 패턴

## 싱글톤은 무엇인가?

* 싱글톤 클래스는 단일 지점을 통해 접근할 수 있는 오직 하나의 인스턴스만 가지고 있다. (메서드/필드)
* 이 패턴이 해결하려는 주된 문제는 이 클래스의 하나의 인스턴스만 존재한다는 것을 확인하는 것이다.
* 싱글톤에 추가하는 어떤 상태도 애플리케이션의 전역 상태의 일부가 된다.



## 싱글톤 구현 방법

* 인스턴스 생성 제어
    * 클래스 생성자는 전역에서 접근되어서는 안된다.
    * 서브 클래싱/상속은 허용되지 않는다.
* 인스턴스 정보 기록
    * 클래스 자체에 기록
* 싱글톤 인스턴스로의 접근
    * public static 메소드로 접근
    * final public static 필드로 인스턴스를 노출할 수는 있으나 모든 싱글톤 구현에서 맞는 방법은 아니다.



* 싱글톤을 구현하는 두가지 방법
    * 빠른(Early) 초기화 - Eager 싱글톤
        * 클래스가 로드될 때 싱글톤 생성
    * 느린(Lazy) 초기화 - Lazy 싱글톤
        * 처음 사용될 때 싱글톤 생성



## 싱글톤 구현

### Eager 초기화 방법

Eager 초기화를 사용하는 방법은 인스턴스를 클래스에서 미리 생성해 놓고 이를 접근할 수 있는 메소드를 제공하는 방법이다.

```java
/**
 * 싱글톤 인스턴스로 eager 초기화를 사용한다.
 */
public class EagerRegistry {

    /**
     * 생성자를 private으로 만듦으로써 클래스 외부에서 객체 초기화를 못하게 하고 또한 상속을 제한할 수 있다.
     */
    private EagerRegistry() {
        // 초기화 코드
    }

    /**
     * 싱글톤 인스턴스. eager 초기화 싱글톤
     */
    private static final EagerRegistry INSTANCE = new EagerRegistry(); // 미리 생성해 놓는다.

    /**
     * 이 메소드는 외부 세계에 싱글톤 인스턴스를 리턴한다.
     */
    public static EagerRegistry getInstance() {
        return INSTANCE;
    }
}
```

이를 사용하는 Client 코드는 아래와 같다.

```java
public class Client {

    public static void main(String[] args) {
        EagerRegistry registry = EagerRegistry.getInstance(); // 인스턴스 생성
        EagerRegistry registry2 = EagerRegistry.getInstance(); // 또 다른 인스턴스 생성

        System.out.println(registry == registry2); // true <= 항상 같은 인스턴스
    }
}
```



### Lazy 초기화 방법

 * 이 클래스는 Double Checked Locking 사용하는, 전통적인 싱글톤 방법을 나타낸다.
 * 또한 lazy 초기화 싱글톤이다.
 * 이 구현방법이 volatile과 double check locking을 사용한 lazy 초기화로 멀티쓰레드 이슈를 해결할 수는 있지만, volatile 키워드는 Java 1.5 이상에서만 동작한다.

```java
public class LazyRegistryWithDCL {

    /**
     * private 생성자로 하여금 외부에서 객체 생성을 못하게 하고 서브클래싱을 불가능하게 한다.
     */
    private LazyRegistryWithDCL() {
    }

    // volatile 키워드는 CPU 캐쉬값을 사용하지 않고 메모리를 접근하도록 한다.
    private static volatile LazyRegistryWithDCL INSTANCE;

    /**
     * double checking locking을 사용한다.
     * @return
     */
    public static LazyRegistryWithDCL getInstance() {
        if (INSTANCE == null) {
            synchronized (LazyRegistryWithDCL.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LazyRegistryWithDCL();
                    // 기본적으로 멀티 쓰레드 환경에서는 CPU 레지스터에 변수 값을 저장한다.
                }
            }
        }

        return INSTANCE;
    }
}
```

이를 사용하는 Client 코드는 아래와 같다.

```java
public class Client {

    public static void main(String[] args) {
        LazyRegistryWithDCL lazySignleton1 = LazyRegistryWithDCL.getInstance();
        LazyRegistryWithDCL lazySignleton2 = LazyRegistryWithDCL.getInstance();

        System.out.println(lazySignleton1 == lazySignleton2); // true
    }
}
```



### Holder로 Lazy 초기화 방법

 * Holder 클래스를 사용한 lazy 초기화 싱글톤 패턴
 * 이 방법은 동기화를 신경쓸 필요없는 lazy 초기화 방법이다.
 * INSTANCE는 getInstance()를 사용할 때까지는 생성이 되지 않는다.

```java
public class LazyRegistryIODH {

    /**
     * 생성자를 private으로 만듦으로써 클래스 외부에서 객체 초기화를 못하게 하고 또한 상속을 제한할 수 있다.
     */
    private LazyRegistryIODH() {
        System.out.println("In LazyRegistryIODH singleton");
    }

    /**
     * 이 클래스는 holder 패턴의 초기화를 제공한다.
     */
    private static class RegistryHolder {
        // INSTANCE는 getInstance()를 사용할 때까지는 생성이 되지 않는다.
        static LazyRegistryIODH INSTANCE = new LazyRegistryIODH();
    }

    /**
     * 이 메소드는 싱글톤 인스턴스를 제공한다.
     */
    public static LazyRegistryIODH getInstance() {
        return RegistryHolder.INSTANCE;
    }
}
```

이를 사용하는 Client 코드는 아래와 같다.

```java
public class Client {

    public static void main(String[] args) {
        LazyRegistryIODH singleton = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton2 = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton3 = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton4 = LazyRegistryIODH.getInstance();
        // In LazyRegistryIODH singleton <== 한번만 호출된다.
    }
}
```

여러번 사용이 되더라도 한번만 생성이 된다.



### Enum을 이용한 싱글톤 생성

 * Ref. Google I/O 2008 Joshua Bloch
 * Java 1.5부터 사용 가능.
 * Java 내부 메커니즘을 사용하여 직렬화를 구현하고 싱글톤을 보장한다.

```java
public enum RegistryEnum {

    INSTANCE;

    public void getConfiguration() {

    }
}
```



## 구현 시 고려사항

* Early/Eager 초기화 방법이 가장 간단하고 선호되는 방법이다. 이 접근 방법을 먼저 사용해라.
* 전통적인 싱글톤 패턴 구현은 DCL(Double Check Locking) 방식과 volatile 필드를 사용한다.
* Lazy 초기화 홀더는 가장 좋은 방식이고 직접 동기화 이슈를 다루지 않아서 구현하기 쉽다.
* enum을 통해 싱글톤을 구현할 수도 있다. 하지만 enum에 대한 사전지식 때문에 싱글톤을 필드로 가지고 있다면 코드리뷰를 진행할 때 사람들을 이해시키기 어려울 수도 있다.
* 만일 간단한 해결책이 있다면 그것을 사용해라.



## 설계 시 고려사항

* 싱글톤 생성은 어떤 파라미터도 필요로 하지 않는다. 만일 생성자 인수가 필요하다면 simple 팩토리나 팩토리 메소드 패턴이 필요할 수도 있다.
* 싱글톤이 변경될 수 있는 전역상태가 아님을 보장해야 한다.



## 싱글톤 패턴 예제

* java.lang.Runtime 클래스는 싱글톤이다.

```java
public class Runtime {
    private static Runtime currentRuntime = new Runtime(); // eager 싱글톤

    public static Runtime getRuntime() {
        return currentRuntime;
    }

  // 이 클래스를 생성할 수 없다.
    private Runtime() {
    }
}
```



## 팩토리 메소드 패턴과 비교

| 싱글톤                                                       | 팩토리 메소드                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 싱글톤 패턴의 주요 목적은 클래스 인스턴스가 하나만 생성됨을 보장하는 것이다. | 팩토리 메소드 패턴의 주요 목적은 객체 생성과 위임을 클라이언트 코드와 격리되어 사용하는 것이다. |
| 싱글톤 인스턴스는 클라이언트의 어떤 매개변수 없이 생성된다.  | 팩토리 메소드는 객체 생성 시 파라미터를 허용한다.            |



## 잠재적인 위험

* 싱글톤 패턴은 의존성을 간과할 수 있다. 전역적으로 접근될 수 있기 때문에 의존관계를 놓치기 쉽다.
* 단위 테스트하기 어렵다. 리턴되는 인스턴스를 쉽게 모킹하기 어렵다.
* 싱글톤을 구현하는 가능 일반적인 방식은 static 변수를 통해서 클래스 로더(JVM이 아닌)에 있는 것이다. 그래서 OSGi나 웹 애플리케이션에서는 실제 싱글톤이 아닐 수도 있다.
* 많은 mutable 전역 상태를 가지고 있는 싱글톤은 싱글톤 패턴을 잘못 사용하는 것을 알려주는 좋은 예이다.



## 빠른 요약

* 싱글톤이좋은 선택이 경우는 별로 없다.
* 애플리케이션 설정 값이 싱글톤 내에서 사용될 수 있다. 일반적으로 이 값들은 시작할 때 파일에서 일고 다른 애플리케이션에서 참조될 수 있다.
* 로깅 프레임워크 또는 싱글톤 패턴을 사용한다.
* 스프링 프레이워크는 기본적으로 모든 빈(bean)들을 싱글톤로 다룬다. 스프링에서 싱글톤 인스턴스를 보장하기 위해 어떤 작업도 필요하지 않다. 스프링이 처리해준다.
