# 12장 다형성

* 상속의 목적은 코드 재사용이 아니다.
    * 상속은 타입 계층을 구조화하기 위해 사용해야 한다.
* 상속을 이용해 자식 클래스를 추가하려 한다면 스스로에게 다음과 같은 질문을 해보기 바란다.
    * 상속을 사용하려는 목적인 단순히 코드를 재사용하기 위해서인가?
    * 아니면 클라이언트 관점에서 인스턴스들을 동일하게 행동하는 그룹으로 묶기 위해서인가?
    * 첫 번째 질문에 대한 답이 '예'라면 상속을 사용하지 말아야 한다.



## 01 다형성

* 다형성(polymorphism)이라는 단어는 그리스어에서 '많은'을 의미하는 'poly'와 '형태'를 의미하는 'morph'의 합성어로 '많은 형태를 가질 수 있는 능력'을 의미한다.
    * 간단하게 말해서 다형성은 여러 타입을 대상으로 동작할 수 있는 코드를 작성할 수 있는 방법이라고 할 수 있다.
* 객체지향 프로그래밍에서 사용되는 다형성은 유니버설(Universal) 다형성과 임시(Ad Hoc) 다형성으로 분류할 수 있다.
    * 유니버설 다형성은 다시 매개변수(Parametric) 다형성과 포함(Inclusion) 다형성으로 분류할 수 있고, 임시 다형성은 오버로딩(Overloading) 다형성과 강제(Coercion) 다형성으로 분류할 수 있다.
* 일반적으로 하나의 클래스 안에 동일한 이름의 메서드가 존재하는 경우를 가리켜 오버로딩 다형성이라고 부른다.

```java
public class Money(
	public Money plus(Money amount) { ... }
	public Money plus(BigDecimal amount) { ... }
	public Money plus(long amount) { ... }
)
```

* 강제 다형성은 언어가 지원하는 자동적인 타입 변환이나 사용자가 직접 구현한 타입 변환을 이용해 동일한 연산자를 다양한 타입에 사용할 수 있는 방식을 가리킨다.
    * 예를 들어 자바에서 이항 연산자인 '+'는 피연산자가 모두 정수일 경우에는 정수에 대한 덧셈 연산자로 동작하지만 하나는 정수형이고 다른 하나는 문제열인 경우에는 연결 연산자로 동작한다.
* 매개변수 다형성은 제네릭 프로그래밍과 관련이 높은데 클래스의 인스턴스 변수나 메서드의 매개변수 타입을 임의의 타입으로 선언한 후 사용하는 시점에 구체적인 타입으로 지정하는 방식을 가리킨다.
    * 예를 들어, 자바의 List 인터페이스는 컬렉션에 보관할 요소의 타입을 임의의 타입 T로 지정하고 있으며 실제 인스턴스를 생성하는 시점에 T를 구체적인 타입으로 지정할 수 있게 하고 있다.
* 포함 다형성은 메시지가 동일하더라도 수신한 객체의 타입에 따라 실제로 수행되는 행동이 달라지는 능력을 의미한다.
    * 포함 다형성은 서브타입(Subtype) 다형성이라고 부른다.

```java
public class Movie {
  private DiscountPolicy discountPolicy;
  
  public Money calculateMovieFee(Screening screening) {
    return fee.minus(discountPolicy.calculateDiscountAmount(screen ing));
  }
}
```



## 02 상속의 양면성

* 객체지향 패러다임의 근간을 이루는 아이디어는 데이터와 행동을 객체라고 불리는 하나의 실행 단위 안으로 통합하는 것이다.
    * 따라서, 객체지향 프로그램을 작성하기 위해서는 항상 데이터와 행동이라는 두 가지 관점을 함께 고려해야 한다.
* 상속 역시 예외는 아니다.
    * 상속을 이용하면 부모 클래스에서 정의한 데이터를 자식 클래스의 인스턴스에 자동으로 포함시킬 수 있다. 이것이 데이터 관점의 상속이다.
    * 데이터뿐만 아니라 부모 클래스에서 정의한 일부 메서드 역시 자동으로 자식 클래스에 포함시킬 수 있다. 이것이 행동 관점의 상속이다.
    * 이는 데이터와 행동을 자동으로 공유할 수 있는 재사용 메커니즘으로 보일 것이다.
    * 하지만 이 관점은 상속을 오해한 것이다.
* 상속의 목적은 코드 재사용이 아니다.
    * 상속은 프로그램을 구성하는 개념들을 기반으로 다형성을 가능하게 하는 타입 계층을 구축하기 위한 것이다.



### 상속을 사용한 강의 평가

#### Lecture 클래스 살펴보기

```
Pass:3 Fail:2, A:1 B:1 C:1 D:0 F:2
```

* Pass:3 Fail:2는 강의를 이수한 학생의수와 낙제한 학생의 수를 나타낸 것이고, 뒷 부분의 "A:1 B:1 C:1 D:0 F:2"는 등급별로 학생들의 분포 현황을 나타낸 것이다.

```java
public class Lecture {
    private int pass;
    private String title;
    private List<Integer> scores = new ArrayList<>();

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title;
        this.pass = pass;
        this.scores = scores;
    }

    public double average() {
        return scores.stream()
                .mapToInt(Integer::intValue)
                .average().orElse(0);
    }

    public List<Integer> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public String evaluate() {
        return String.format("Pass:%d Fail:%d", passCount(), failCount());
    }

    public long passCount() {
        return scores.stream()
                .filter(score -> score >= pass).count();
    }

    public long failCount() {
        return scores.size() - passCount();
    }
  
    public static void main(String[] args) {
        Lecture lecture = new Lecture("객체지향 프로그래밍",
                70,
                Arrays.asList(81, 95, 75, 50, 45));
        String evaluation = lecture.evaluate();
        System.out.println(evaluation);
    }
}
```



#### 상속을 이용해 Lecture 클래스 재사용하기

```java
public class GradeLecture extends Lecture {
    private List<Grade> grades;

    public GradeLecture(String name, int pass, List<Grade> grades, List<Integer> scores) {
        super(name, pass, scores);
        this.grades = grades;
    }

    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    private String gradesStatistics() {
        return grades.stream()
                .map(grade -> format(grade))
                .collect(joining(" "));
    }

    public String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .count();
    }
}
```

```java
public class Grade {
    private String name;
    private int upper, lower;

    public Grade(String name, int upper, int lower) {
        this.name = name;
        this.upper = upper;
        this.lower = lower;
    }

    public String getName() {
        return name;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean include(int score) {
        return score >= lower && score <= upper;
    }
}
```

```java
Lecture lecture = new GradeLecture("객체지향 프로그래밍",
                                   70,
                                   Arrays.asList(new Grade("A", 100, 95),
                                                 new Grade("B", 94, 80),
                                                 new Grade("C", 79, 70),
                                                 new Grade("D", 69, 50),
                                                 new Grade("F", 49, 0)),
                                   Arrays.asList(81, 95, 75, 50, 45));

String evaluation = lecture.evaluate();
System.out.println(evaluation);
```



### 데이터 관점의 상속





### 행동 관점의 상속





## 03 업캐스팅과 동적 바인딩

### 같은 메시지, 다른 메시지





### 업캐스팅





### 동적 바인딩

* 전통적인 언어에서 함수를 실행하는 방법은 함수를 호출하는 것이다.
    * 객체지향 언어에서 메서드를 실행하는 방법은 메시지를 전송하는 것이다.
    * 함수 호출과 메시지 전송 사이의 차이는 생각보다 큰데 프로그램 안에 작성된 함수 호출 구문과 실제로 실행되는 코드를 연결하는 메커니즘이 완전히 다르기 때문이다.
* 함수를 호출하는 전통적인 언어들은 호출될 함수를 컴파일타임에 결정한다.
    * 코드를 작성하는 시점에 호출될 코드가 결정된다.
    * 이처럼 컴파일 타임에 호출할 함수를 결정하는 방식을 **정적 바인딩(static biding),** **초기 바인딩(early binding)**, 또는 **컴파일타임 바인딩(compile-time binding)**이라고 부른다.
* 객체지향 언어에서는 메시지를 수신했을 때 실행될 메서드가 런타임에 결정된다.
    * 이처럼 실행될 메서드를 런타임에 결정하는 방식을 **동적 바인딩(dynamic binding)** 또는 **지연 바인딩(late binding)**이라고 부른다.



## 04 동적 메서드 탐색과 다형성

* 객체 지향 시스템은 다음 규칙에 따라 실행할 메서드를 선택한다.
    * 메시지를 수신한 객체는 먼저 자신을 생성한 클래스에 적합한 메서드가 존재하는지 검사한다. 존재하면 메서드를 실행하고 탐색을 종료한다.
    * 메서드를 찾기 못했다면 부모 클래스에서 메서드 탐색을 계속한다. 이 과정은 적합한 메서드를 찾을 때까지 상속 계층을 따라 올라가며 계속된다.
    * 상속 계층의 가장 최상위 클래스에 이르렀지만 메서드를 발견하지 못한 경우 예외를 발생시키면 탐색을 중단한다.



### 자동적인 메시지 위임





### 동적인 문맥





### 이해할 수 없는 메시지





### self대 super





## 05 상속 대 위임

### 위임과 self 참조





### 프로토타입 기반의 객체지향 언어

 