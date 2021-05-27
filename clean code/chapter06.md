# 6장 객체와 자료 구조

변수를 비공개(private)로 정의하는 이유가 있다. 남들이 변수에 의존하지 않게 만들고 싶어서다.



##### 자료 추상화

두 클래스 모두 2차원 점을 표현한다. 그런데 한 클래스는 구현을 외부로 노출하고 다른 클래스는 구현을 완전히 숨긴다.

```java
public class Point {
  public double x;
  public double y;
}
```

```java
public interface Point {
  double getX();
  double getY();
  void setCatesian(double x, double y);
  double getR();
  double getTheta();
  void setPolar(double r, double theta);
}
```

추상 인터페이스를 제공해 사용자가 구현을 모른 채 자료의 핵심을 조작할 수 있어야 진정한 의미의 클래스다.

다음은 자동차 연료 상태를 구체적인 숫자 값으로 알려준다.

[목록 6-3] 구체적인 Vehicle 클래스

```java
public interface Vehicle {
    double getFuelTankCapacityInGallons();
    double getGallonsOfGasoline();
}
```

[목록 6-4] 추상적인 Vehicle 클래스

```java
public interface Vehicle {
    double getPercentFuelRemaining();
}
```

목록 6-4가 더 좋다. 자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 편이 좋다.



##### 자료/객체 비대칭

객체는 추상화 뒤로 자료를 숨긴 채 자료를 다루는 함수만 공개한다.

자료 구조는 자료를 그대로 공개하며 별다른 함수는 제공하지 않는다.



##### 디미터 법칙

모듈은 자신이 조작하는 객체의 속사정을 몰라야 한다는 법칙이다.
객체는 자료를 숨기고 함수를 공개한다. 즉, 객체는 조회 함수로 내부 구조를 공개하면 안 된다는 의미다.

디미터 법칙은 "클래스 C의 메서드 f는 다음과 같은 객체의 메서드만 호출해야 한다"고 주장한다.

* 클래스 C
* f가 생성한 객체
* f 인수로 넘어온 객체
* C 인스턴스 변수에 저장된 객체

낯선 사람은 경계하고 친구랑만 놀라는 의미다.

다음 코드는 디미터 법칙을 어기는 듯이 보인다.

```java
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```



###### 기차 충돌

흔히 위와 같은 코드를 기차 충돌(train wreck)이라 부른다. 다음과 같이 나누는 편이 좋다.

```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```



###### 잡종 구조

절반은 객체, 절반은 자료 구조인 잡종 구조가 나온다.

> 객체는 자료를 다루는 함수만 공개한다.
>
> 자료구조는 자료를 그대로 공개하며 별 다른 함수를 제공하지 않는다.



###### 구조체 감추기



##### 자료 전달 객체

자료 구조체의 전형적인 형태는 공개 변수만 있고 함수가 없는 클래스다. 이런 자료 구조체를 때로는 자료 전달 객체(Data Transfer Object, DTO)라 한다.
좀 더 일반적인 형태는 '빈(bean)' 구조다. 일종의 사이비 캡슐화로, 일부 OO 순수주의자나 만족시킬 뿐 별다른 이익을 제공하지 않는다.



###### 활성 레코드

활성 레코드는 DTO의 특수한 형태다. 공개 변수가 있거나 비공개 변수에 조회/설정 함수가 있는 자료 구조이다.
활성 레코드에 비즈니스 규칙 메서드를 추가해 이런 자료 구조를 객체로 취급하는 개발자가 흔하다. 하지만 이는 바람직하지 않다.



##### 결론

객체는 동작을 공개하고 자료를 숨긴다. 그래서 기존 동작을 변경하지 않으면서 새 객체 타입을 추가하기는 쉬운 반면, 기존 객체에 새 동작을 추가하기는 어렵다.
자료 구조는 별다른 동작 없이 자료를 노출한다. 그래서 기존 자료 구조에 새 동작을 추가하기는 쉬우나, 기존 함수에 새 자료 구조를 추가하기는 어렵다.

























