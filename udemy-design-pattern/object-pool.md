> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 오브젝트 풀 패턴

## 오브젝트 풀은 무엇인가?

* 시스템에서 인스턴스 생성 비용이 높고 짧은 기간 동안 많은 객체가 필요하다면 오브젝트 풀을 사용할 수 있다.
* 미리 객체를 생성할 수 있고 메모리 캐시에 사용하지 않는 인스턴스를 다시 모을 수 있다. 객체가 다시 필요할 때 캐시에서 꺼내서 준다.
* 가장 복잡한 패턴 중 하나이며 효과적으로 구현할 수 있다.




## UML





## 오브젝트 풀 구현방법

* 오브젝트 풀에 대한 클래스를 만들면서 시작한다.
    * 풀(pool)에는 객체에 대해 thread-safe해야 한다.
    * 객체를 얻고 반환하는 메소드를 제공해야 하고 풀(pool)은 사용하기 전에 객체를 초기화해야 한다.
    * 재사용 가능한 객체는 상태를 초기화하기 위해 release 코드로 초기화해야 한다.
    * 풀(pool)이 비어있거나 객체 사용이 가능할 때 까지 기다릴 때 새로운 객체 풀(pool)을 만들지 결정해야 한다. 객체가 외부 고정된 리소스에 영향이 있는지에 따라 달라진다.





## 오브젝트 풀 구현

Poolable 인터페이스가 있고 reset() 메소드 하나를 가지고 있다.

```java
public interface Poolable {

    // state reset
    void reset();
}
```

Image 인터페이스가 있고 Poolable을 상속한다.
```java
// 재사용 가능한 추상 클래스를 나타낸다.
public interface Image extends Poolable {

    void draw();

    Point2D getLocation();

    void setLocation(Point2D location);
}
```

그 다음으로 Bitmap 클래스를 만들자.

```java
public class Bitmap implements Image {

    private Point2D location;

    private String name;

    public Bitmap(String name) {
        this.name = name;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + name + " @ " + location);
    }

    @Override
    public Point2D getLocation() {
        return location;
    }

    @Override
    public void setLocation(Point2D location) {
        this.location = location;
    }

    @Override
    public void reset() {
        location = null;
        System.out.println("Bitmap is reset");
    }
}
```

객체의 Pool을 관리하는 ObjectPool 클래스를 작성하자.

```java
public class ObjectPool<T extends Poolable> {

    private BlockingQueue<T> availablePool;

    public ObjectPool(Supplier<T> creator, int count) {
        this.availablePool = new LinkedBlockingQueue<>();
        for (int i=0; i<count; i++) {
            availablePool.offer(creator.get());
        }
    }

    public T get() {
        try {
            return availablePool.take();
        } catch (InterruptedException ex) {
            System.err.println("take() was interrupted");
        }

        return null;
    }

    public void release(T obj) {
        obj.reset();
        try {
            availablePool.put(obj);
        } catch (InterruptedException e) {
            System.err.println("put() was interrupted");
        }
    }
}
```

클라이언트 코드를 작성하자.

```java
public class Client {

    public static final ObjectPool<Bitmap> bitmapPool = new ObjectPool<>(() -> new Bitmap("Logo.bmp"), 5);

    public static void main(String[] args) {
        Bitmap b1 = bitmapPool.get();
        b1.setLocation(new Point2D(10, 10));
        Bitmap b2 = bitmapPool.get();
        b2.setLocation(new Point2D(-10, 0));

        b1.draw();
        b2.draw();

        bitmapPool.release(b1);
        bitmapPool.release(b2);
    }
}
```

실행하면 아래와 같다.

```	sh
Drawing Logo.bmp @ Point2D [x = 10.0, y = 10.0]
Drawing Logo.bmp @ Point2D [x = -10.0, y = 0.0]
Bitmap is reset
Bitmap is reset
```






## 구현 시 고려사항

* 객체상태를 초기화하는 작업이 어렵지 않아야 한다. 그렇지 않으면 성능적 이점을 읽게 될 것이다.

* 객체 선행 캐싱; 객체를 미리 생성해 놓는다는 것은 이러한 객체로 하여금 코드를 빠르게 실행할 수 있어 도움이 될 수 있다.  하지만 시작 시점에 시간과 메모리 사용을 증가 시킬수도 있다.

* 객체 풀의 동기화는 초기화 시간을 고려해야 하고 가능하다면 synchronized 영역에서 초기화를 하지 않아야 한다.

    

## 디자인 고려사항

* 객체 구현은 캐쉬에 파라미터가 사용될 수도 있고 여러 개의 객체를 리턴하며 메소드가 특정 조건을 제공할 수도 있다.
* 풀링 객체는 connection이나 thread와 같이 외부 리소스 작업과 같이  값비싼 초기화를 포함하고 있다면 도움이 된다. 만일 메모리 에러를 가지고 있지 않다면 단순히 메모리를 절약하기 위해 객체를 풀(pool)하지는 마라.
* 객체를 장기간 혹은 잠시동안 너무 주자 생성하지는 마라. 풀(pool)은 실제로 성능에 부정적인 영향을 미친다.



## 오브젝트 풀 예제

* 메모리 할당을 절약하기 위한 객체 풀 사용 & 지금은 GC 비용이 거의 deprecated되었다. JVM&하드웨어가 더 효율이 좋고 더 많은 메모리에 접근하고 있다.

* 하지만 thread, connection과 같은 외부 리소스와 작업할 때 보다 더 일반적인 패턴이다.

* java.util.concurrent.ThreadPoolExecutor가 thread를 풀(pool)하는 객체 풀(pool)의 예이다. 우리는 직접 이 클래스를 사용할 수 있지만, newCachedThreadPool()같은 Executors 메소드를 사용하여 ExecutorService 인터페이스를 통해서 사용한다.

    ```java
    ExecutorService service = Executors.newCachedThreadPool();
    
    service.submit(() -> System.out.println(Thread.currentThread().getName()));
    service.submit(() -> System.out.println(Thread.currentThread().getName()));
    service.submit(() -> System.out.println(Thread.currentThread().getName()));
    
    service.shutdown();
    ```

* apache commons dbcp 라이브러리가 데이터베이스 connection 풀링(pooling)에 사용된다. org.apache.commons.dbcp.BasicDataSource 클래스가 데이터베이스 connection을 풀링하는 객체 풀(pool) 패턴의 예이다. 이 풀은 보통 생성되고 JNDI로 노출되거나 스프링 빈에서 사용된다.



## 프로토타입과 비교

| 오브젝트 풀                                                  | 프로토타입                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 프로그램 실행동안 캐쉬된 객체가 있다.                        | 프로토타입은 필요할 때 만들어지고 캐싱이 필요 없다.          |
| 객체풀에서 객체를 사용하는 코드는 명시적으로 풀에서 객체를 리턴해야 한다. 구현에 따라 풀로 리턴하는데 실행하는 것은 메모리 혹은 리소스 누수를 야기시킬 수 있다. | 객체가 복제가 되면 클라이언트 코드에서 어떤 특별한 처리도 필요 없고 객체는 일반적인 객체와 동일하게 사용될 수 있다. |



## 위험요소

* 클라이언트 코드에서 얼마나 정확히 사용하느냐에 따라 성공여부가 달려있다. 객체를 풀로 반환하는 것은 올바르게 작동하는데 상당히 중요할 수 있다.
* 재사용 가능한 객체는 효과적인 방법으로 초기화하도록 유심히 살펴보아야 한다. 어떤 객체는 이런 요구사항으로 인해 풀링하는데 적합하지 않을 수 있다.
* 클라이언트 코드와 객체 모두 오브젝트 풀 방식으로 사용해야 하기 때문에 기존 코드를 리팩토링하기 어렵다.
    * 풀이 바닥 나고 또 다른 객체를 필요로 할때 어떻게 될지 결정해야 한다. 여유객체가 생길때 까지 대기하거나 새 객체를 생성할 수 있다. 두가지 옵션 모두 이슈가 있다. 대기하는 것은은 성능에 심각한 부정적인 영향을 미칠수도 있다.
    * 객체가 부족하여 새 객체를 생성한다면, 풀 크기를 유지하기 위해서 추가적인 작업을 해야 할지도 있다. 그렇지 않으면 아주 큰 풀을 가지게 될 지도 모른다.




## 빠른 요약

* 







