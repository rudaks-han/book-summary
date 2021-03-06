# 12장 다형성

* 상속의 목적은 코드 재사용이 아니다.
* 상속은 타입 계층을 구조화하기 위해 사용해야 한다.
* 상속을 이용해 자식 클래스를 추가하려 한다면 스스로에게 다음과 같은 질문을 해보기 바란다.
    * 상속을 사용하려는 목적인 단순히 코드를 재사용하기 위해서인가?
    * 아니면 클라이언트 관점에서 인스턴스들을 동일하게 행동하는 그룹으로 묶기 위해서인가?
    * 첫 번째 질문에 대한 답이 '예'라면 상속을 사용하지 말아야 한다.



## 01 다형성

* 객체지향 프로그램을 작성하기 위해서는 항상 데이터와 행동이라는 두 가지 관점을 함께 고려해야 한다.
* 상속
    * 부모 클래스에서 정의한 데이터를 자식 클래스의 인스턴스에 자동으로 포함시킬 수 있다. 이것이 데이터 관점의 상속이다.
    * 부모 클래스에서 정의한 일부 메서드 역시 자동으로 자식 클래스에 포함시킬 수 있다. 이것이 행동 관점의 상속이다.
    * 이는 데이터와 행동을 자동으로 공유할 수 있는 재사용 메커니즘으로 보일 것이다.
    * 하지만 이 관점은 상속을 오해한 것이다.
* 상속의 목적은 코드 재사용이 아니다.
* 상속은 프로그램을 구성하는 개념들을 기반으로 다형성을 가능하게 하는 타입 계층을 구축하기 위한 것이다.



## 02 상속의 양면성





### 상속을 사용한 강의 평가





### 데이터 관점의 상속





### 행동 관점의 상속





## 03 업캐스팅과 동적 바인딩

### 같은 메시지, 다른 메시지





### 업캐스팅





### 동적 바인딩





## 04 동적 메서드 탐색과 다형성





### 자동적인 메시지 위임





### 동적인 문맥





### 이해할 수 없는 메시지





### self대 super





## 05 상속 대 위임

### 위임과 self 참조





### 프로토타입 기반의 객체지향 언어

 