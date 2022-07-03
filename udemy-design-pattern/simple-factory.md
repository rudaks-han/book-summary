> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 심플 팩토리 패턴

## 심플 팩토리가 해결하려고 하는 문제는?

어떤 조건에 의해 여러가지 타입이 초기화될 수 있는 경우에 사용된다.

``` java
if (key.equalsIgnoreCase("pudding")) {
  // pudding 객체 생성
} else if (key.equalsIgnoreCase("cake")) {
  // cake 객체 생성
}
```



## 심플 팩토리는 무엇인가?

* 객체 생성 로직을 여러 클래스로 나누고 클래스의 static 메소드를 사용한다.
* 어떤 사람들은 심플 팩토리가 객체 생성 과정을 단순히 캡슐화만 하기 때문에 디자인 패턴으로 생각하지 않는다. 메소드에서 복잡한 것은 없다.
    * 보통 심플 팩토리를 팩토리 메소드 패턴과 혼동하고 있다.

* 일반적으로 객체 생성할 때 한가지 이상 선택사항이 있고 클래스 선택할 때 어떤 로직을 사용할 때 사용한다.



## 심플 팩토리 구현방법

* 심플 팩토리의 클래스를 분리함으로써 시작한다.
    * 객체 인스턴스를 리턴하는 메소드 추가
        * 이 메소드는 일반적으로 static이며 초기화할 클래스를 결정할 인스턴스를 인수로 받는다.
        * 객체 초기화에 사용될 추가적인 인수를 제공할 수도 있다.



## 심플 팩토링 구현

여러가지 종류의 게시판을 지원하는 Post 클래스를 생성한다.

```java
@Getter
@Setter
public abstract class Post {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;
}
```

Post를 상속하는 BlogPost를 만든다.

```java
@Getter
@Setter
public class BlogPost extends Post {

    private String author;

    private String[] tags;
}
```

NewsPost를 만든다.

```java
@Getter
@Setter
public class NewsPost extends Post {

    private String headLine;

    private LocalDateTime newsTime;
}
```

ProductPost를 만든다.

```java
@Getter
@Setter
public class ProductPost extends Post {

    private String imageUrl;

    private String name;
}
```

여러가지 Post를 만드는 역할을 하는 PostFactory를 만든다.

```java
public class PostFactory {

    public static Post createPost(String type) {
        switch (type) {
            case "blog":
                return new BlogPost();
            case "news":
                return new NewsPost();
            case "product":
                return new ProductPost();
            default:
                throw new IllegalArgumentException("Post type is not exists: " + type);
        }
    }
}
```

실제 사용하는 Client 클래스를 구현하자.

```java
public class Client {

    public static void main(String[] args) {
        Post blogPost = PostFactory.createPost("blog");
        System.out.println(blogPost); // com.example.designpattern.simplefactory.BlogPost@45ee12a7
        Post newsPost = PostFactory.createPost("news");
        System.out.println(newsPost); // com.example.designpattern.simplefactory.NewsPost@45ee12a7
    }
}
```



## 구현 고려사항

* 심플 팩토리는 기존 클래스에 단순히 메소드가 될 수 있다. 개별 클래스를 추가함으로써 코드의 일부를 심플 팩토리를 손쉽게 사용할 수 있게 해준다.
* 심플 팩토리 그차제로는 어떤 상태도 필요로 하지 않고 static 메소드로 만드는 것이 가장 낫다.



## 디자인 고려사항

* 심플 팩토리는 객체를 구성하기 위해서 빌더와 같은 다른 디자인 패턴을 사용할 수도 있다.
* 서브 클래스에서 심플 팩토리를 구체화하는 경우에는 팩토리 메소드 패턴을 대신 사용할 필요도 있다.



## 심플 팩토리 예제

* java.text.NumberFormat 클래스는 심플 팩토리를 사용하는 `getInstance` 메소드를 가지고 있다.

```java
private static NumberFormat getInstance(LocaleProviderAdapter adapter,
                                            Locale locale, int choice) {
        NumberFormatProvider provider = adapter.getNumberFormatProvider();
        NumberFormat numberFormat = null;
        switch (choice) {
        case NUMBERSTYLE:
            numberFormat = provider.getNumberInstance(locale);
            break;
        case PERCENTSTYLE:
            numberFormat = provider.getPercentInstance(locale);
            break;
        case CURRENCYSTYLE:
            numberFormat = provider.getCurrencyInstance(locale);
            break;
        case INTEGERSTYLE:
            numberFormat = provider.getIntegerInstance(locale);
            break;
        }
        return numberFormat;
    }
```



## 팩토리 메소드 패턴과 비교

| 심플 팩토리                                                  | 팩토리 메소드                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 클라이언트 코드로 부터 객체 생성 로직을 분리. 일반적으로 static 메소드로 | 팩토리 메소드는 객체 생성을 서브 클래스로 위임하려고 할 때 더 유용하다. |
| 심플 팩토리는 생성하려는 객체의 모든 클래스를 알고 있다.     | 팩토리 메소드에서는 생성하는 모든 서브 클래스에 대해서 미리 알지 못한다. |



## 위험요소

* 객체 생성을 결정할 때 사용되는 심플 팩토리의 기준은 시간이 지남에 따라 더 복잡해질 수있다. 만일 여러분의 코드가 그런 경우라면 팩토리 디자인 패턴을 사용해라.



## 빠른 요약

* 심플 팩토리는 객체 생성과정을 개별 메소드로 캡슐화한다.
* 객체 생성시 객체 종류를 명시하기 위해서 메소드에 추가적인 인수를 전달할 수 있다.
