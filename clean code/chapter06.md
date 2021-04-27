# 6 객체와 자료 구조

##### 자료 추상화



구체적인 Vehicle 클래스

```java
public interface Vehicle {
    double getFuelTankCapacityInGallons();
    double getGallonsOfGasoline();
}
```

추상적인 Vehicle 클래스

```java
public interface Vehicle {
    double getPercentFuelRemaining();
}
```

자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 편이 좋다.



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

위의 코드는 다음과 같이 나누는 편이 좋다.

```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```



###### 잡종 구조

###### 구조체 감추기



##### 자료 전달 객체

자료 구조체의 전형적인 형태는 공개 변수만 있고 함수가 없는 클래스다. 이런 자료 구조체를 때로는 자료 전달 객체(Data Transfer Object, DTO)라 한다.



###### 활성 레코드



























