> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 데코레이터 패턴

## 데코레이터는 무엇인가?

* 우리가 기존에 사용하던 객체를 동적으로 강화하고자 할때 데코레이터 패턴을 사용할 수 있다.
* 데코레이터는 그 내부에 객체를 감싸고 감싸여진 객체와 동일 인터페이스를 제공한다. 그래서 원래 객체의 클라이언트는 변경할 필요가 없다.
* 데코레이터는 기존 클래스의 기능을 확장하는데 서브클래싱의 방법을 제공한다.




## UML





## 데코레이터 구현방법

* 컴포넌트로 시작한다.
    * 컴포넌트는 클라이언트에 필요하거나 이미 사용되고 있는 인터페이스를 정의한다.
    * 구체적인 컴포넌트는 컴포넌트를 구현한다.
    * 데코레이터를 정의한다. 데코레이터는 컴포넌트를 구현하고 또한 구체적인 컴포넌트로의 참고를 필요로 한다.
    * 데코레이터 메소드에서 구체적인 컴포넌트 인스턴스에서 제공하는 것 외에 추가적인 행위를 제공할 수 있다.

* 데코레이터는 또한 추상 클래스가 될수 있으며 제공된 기능에 서브클래스를 의존한다.




## 데코레이터 구현

Message 인터페이스가 있다.

```java
// 기본 인터페이스
public interface Message {

    String getContent();
}
```

Message를 구현한 TextMessage

```java
// 구체적인 컴포넌트. 데코레이터로 싸여질 객체
public class TextMessage implements Message {

    private String message;

    public TextMessage(String message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return message;
    }
}
```

다음으로 TextMessage를 데코레이트할 객체를 만들어보자. message 내용을 HtmlEscape하는 데코레이터이다.

```java
public class HtmlEncodedMessage implements Message {

    private Message message;

    public HtmlEncodedMessage(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return StringEscapeUtils.escapeHtml4(message.getContent());
    }
}
```

다음은 메시지 내용을 Base64Encoding을 하는 데코레이터이다.

```java
public class Base64EncodedMessage implements Message {

    private Message message;

    public Base64EncodedMessage(Message message) {
        this.message = message;
    }

    @Override
    public String getContent() {
        return Base64.getEncoder().encodeToString(message.getContent().getBytes());
    }
}
```

이를 사용하는 클라이언트 코드는 아래와 같다.

```java
public class Client {

    public static void main(String[] args) {
        Message message = new TextMessage("The <FORCE> is strong with this one!");
        System.out.println(message.getContent());

        Message decorator = new HtmlEncodedMessage(message);
        System.out.println(decorator.getContent());

        decorator = new Base64EncodedMessage(message);
        System.out.println(decorator.getContent());
    }
}
```



## 구현 시 고려사항

* 기본 컴포넌트에서 확장되는 데코레이터와 구체 클래스가 있기 때문에 , 데코레이터는 모든 상태를 필요로 하지 않기 때문에 기반 클래스에는 많은 상태를 가지지 않도록 해라.
* 데코레이터 메소드의 equals와 hashCode에 신경써야 한다. 데코레이터를 사용할 때, 데코레이트로 쌓여지는 객체가 데코레이터 없이 동일객체인지 정해야 한다.
* 데코레이터는 재귀 컴포지션을 사용할 수 있어시 이러한 패턴이 단지 조그만 기능을 하는 많은 양의 객체 생성을 유발할 수 있다. 이러한 객체를 사용하는 코드는 디버깅하기 어려워진다.



## 디자인 고려사항

* 데코레이터는 상속보다 보다 유연하고 강력하다. 상속은 컴파일 시점에 변경할 수 없지만  데코레이터는 런타임 시 행동을 동적으로 구성할 수 있다.
* 데코레이터는 객체 위의 있는 껍질같은 것이다. 그것들이 객체 본연의 기능에 조그만한 동작으로 추가할 수 있게 한다. 동작의 의미를 변경하지 않는다.



## 데코레이터 예제

* Java I/O 패키지에 있는 클래스들이 데코레이터 패턴의 예이다.
* 예를 들면, java.io.BufferedOutputStream 클래스는 java.io.OutputStream 객체를 감싸고 파일 쓰기 동작에 버퍼링을 더한다. 이러한 것들이 쓰기 횟수를 줄여서 디스크 I/O 성능을 향상시킨다.



## 컴포지트 패턴과 비교

| 데코레이터                                                   | 컴포지트                                      |
| ------------------------------------------------------------ | --------------------------------------------- |
| 기존 객체의 행동에 추가하려는 의도                           | 컴포지트는 객체 그룹에 의미가 있다.           |
| 데코레이터는 하나의 컴포넌트만 가진 컴포지트로 생각될 수 있다. | 컴포지트는 집합으로 많은 컴포넌트를 지원한다. |



## 위험요소

* 시스템에 조그만 기능만을 가진 많은 클래스가 추가될 가능성이 있다. 많은 객체가 생겨나고 또 다른 객체가 그 내부에 생긴다.
* 때로는 신입사원이 상속의 대체자로서 시작할 수 있다. 데코레이터는 기존 객체의 얇은 껍질로 생각해라.



## 빠른 요약

* 우리는 기존 객체에 조그만 행동을 추가할 때 데코레이터를 사용한다.
* 데코레이터는 장식하거나 가지고 있는 객체와 동일한 인터페이스를 가지고 있다.
* 데코레이터는 컴포지션을 사용하여 행위를 동적으로 구성할 수 있다. 데코레이터는 원래 객체를 차례로 감싸는 또 다른 데코레이터를 감쌀수 있다.
* 객체의 클라이언트는 데코레이터의 존재를 알지 못한다.







