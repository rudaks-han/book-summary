> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 추상 팩토리 패턴

## 추상 팩토리는 무엇인가?

* 추상 팩토리는 객체를 구성하는 2개 이상의 객체를 가지고 있고 클라이언트 코드에서 생성될 수 있는 여러 개의 타입이 생성될 경우에 사용된다.

* 그래서, 구성요소를 생성하는 코드를 클라이언트 코드로부터 분리한다.

    


## UML



## 추상 팩토리 구현방법

* 클래스의 구성 요소를 정의함으로서 시작한다.
* * 추상 팩토리를 추상 클래스나 인터페이스로 생성
    * 추상 팩토리는 객체를 만들기 위해 추상 메소드를 정의한다.
    * 유형 별 개별 구성요소의 팩토리 구현체를 제공 
* 추상 팩토리는 팩토리 메소드 패턴을 사용한다. 추상 팩토리를 여러 개의 팩토리 메소드를 가진 객체로 생각할 수 있다.






## 추상 팩토리 구현

Instance의 인터페이스를 정의

```java
public interface Instance {

    enum Capacity {
        micro, small, large
    }

    void start();

    void attachStorage(Storage storage);

    void stop();
}
```

Storage를 정의

```java
public interface Storage {

    String getId();
}
```

instance와 Storage를 생성하는 추상 팩토리 생성

```java
// 각 유형에 정의된 메소드의 추상 팩토리
public interface ResourceFactory {

    Instance createInstance(Instance.Capacity capacity);

    Storage createStorage(int capMib);
}
```

다음은 aws와 gcp 각각에 대한 구체클래스를 만들어보자.

우선 aws이다.

```java
public class Ec2Instance implements Instance {

    public Ec2Instance(Capacity capacity) {
        // ec2 instance 유형. aws API를 사용
        System.out.println("Created Ec2Instance");
    }

    @Override
    public void start() {
        System.out.println("Ec2Instance started");
    }

    @Override
    public void attachStorage(Storage storage) {
        System.out.println("Attched " + storage + " to Ec2Instance");
    }

    @Override
    public void stop() {
        System.out.println("Ec2Instance stopped");
    }

    @Override
    public String toString() {
        return "Ec2Instance";
    }
}
```

```java
public class S3Storage implements Storage {

    public S3Storage(int capacityInMib) {
        // aws s3 api 사용
        System.out.println("Allocated " + capacityInMib + " on S3");
    }

    @Override
    public String getId() {
        return "S31";
    }

    @Override
    public String toString() {
        return "S3 Storage";
    }
}
```

aws의 instance와 storage를 만드는 팩토리 정의

```java
public class AwsResourceFactory implements ResourceFactory {

    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new Ec2Instance(capacity);
    }

    @Override
    public Storage createStorage(int capMib) {
        return new S3Storage(capMib);
    }
}
```

다음으로 gcp

```java
public class GoogleComputeEngineInstance implements Instance {

    public GoogleComputeEngineInstance(Capacity capacity) {
        // GCP 인스턴스 유형. GCP API를 사용
        System.out.println("Created Google Compute Engine instance");
    }

    @Override
    public void start() {
        System.out.println("Compute Engine instance started");
    }

    @Override
    public void attachStorage(Storage storage) {
        System.out.println("Attched " + storage + " to Compute engine instance");
    }

    @Override
    public void stop() {
        System.out.println("Compute engine instance stopped");
    }
}
```

```java
public class GoogleCloudStorage implements Storage {

    public GoogleCloudStorage(int capacityInMib) {
        // gcp api를 사용
        System.out.println("Allocated " + capacityInMib + " on Google Cloud Storage");
    }

    @Override
    public String getId() {
        return "gcpcs1";
    }

    @Override
    public String toString() {
        return "Google cloud storage";
    }
}
```

gcp의 instance와 storage를 만드는 팩토리

```java
public class GoogleResourceFactory implements ResourceFactory {

    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new GoogleComputeEngineInstance(capacity);
    }

    @Override
    public Storage createStorage(int capMib) {
        return new GoogleCloudStorage(capMib);
    }
}
```



이를 사용하는 클라이언트 코드를 작성해보면 아래와 같다.

```java
public class Client {

    private ResourceFactory factory;

    public Client(ResourceFactory factory) {
        this.factory = factory;
    }

    public Instance createServer(Instance.Capacity capacity, int storageMib) {
        Instance instance = factory.createInstance(capacity);
        Storage storage = factory.createStorage(storageMib);
        instance.attachStorage(storage);

        return instance;
    }

    public static void main(String[] args) {
        Client aws = new Client(new AwsResourceFactory());
        Instance i1 = aws.createServer(Instance.Capacity.micro, 20480);
        i1.start();
        i1.stop();

        System.out.println("********************************");

        Client gcp = new Client(new GoogleResourceFactory());
        i1 = gcp.createServer(Instance.Capacity.micro, 20480);
        i1.start();
        i1.stop();
    }
}
```





## 구현 시 고려사항

* 팩토리는 싱글톤처럼 구현될 수 있고 일반적으로 하나의 인스턴스만 필요하다.
* 새로운 구성요소 유형을 추가하는 것은 팩토리 구현에서 처럼 기반 팩토리의 변경을 필요로 한다.
* 클라이언트 코드에 객체를 생성할 수 있는 구체적인 팩토리를 제공한다.



## 디자인 고려사항

* 객체 생성을 제한하여 동일하게 동작하도록 하고 있다면 추상 팩토리는 좋은 선택지가 된다.
* 추상 팩토리는 팩토리 메소드 패턴을 사용한다.
* 객체 생성이 비용이 많이 드는 작업이라면 객체 생성하는 작업에 팩토리 구현보다 프로토타입 패턴으로 바꿀 수도 있다.



## 추상 팩토리 예제

* javax.xml.DocumentbuilderFactory가 추상팩토리 패턴의 좋은 예이다.
* 하지만 이 구현은 GoF 추상 팩토리와 100% 일치하지 않는다. 이 클래스는 실제 팩토리 클래스 객체를 리턴하는 정적 newInstance() 메서드를 가지고 있다.
* newInstance() 메소드는 팩토리 클래스를 찾고, 팩토리 객체를 생성하기 위한 방법으로 클래스패스 스캐닝, system properties, external property 파일을 사용한다. 그래서 정적 메소드일지라도 팩토리 클래스를 변경할 수 있다.



## 위험요소

* 팩토리 메소드 보다 구현하기 더 복잡하다.
* 새로운 구성요소를 추가하는 것은 팩토리의 모든 구현부와 기반 팩토리의 변경을 가져온다.
* 개발을 시작할 때 시각화하기 어렵고 보통은 팩토리 메소드로서 시작한다.
* 추상 팩토리 패턴은 제품 군에 관련된 문제를 해결하는데 좋다.

