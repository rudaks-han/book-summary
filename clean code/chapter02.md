# 2장 의미 있는 이름

##### 들어가면서

소프트웨어에서 이름은 어디나 쓰인다.

* 변수, 함수, 인수와 클래스, 패키지, 소스파일, 디렉토리, jar파일, war파일, ear 파일
* 이 장에서는 이름을 잘 짓는 간단한 규칙을 몇가지 소개한다.



##### 의도를 분명히 밝혀라

* 의도가 분명한 이름이 정말로 중요하다는 사실을 거듭 강조한다.
* <u>좋은 이름을 지으려면 시간이 걸리지만 좋은 이름으로 절약하는 시간이 훨씬 더 많다.</u>

주석이 필요하다면 의도를 분명히 드러내지 못했다는 말이다.

```java
int d; // 경과 시간(단위: 날짜)
```

이름 d는 아무 의미도 드러나지 않는다. 경과시간이나 날짜라는 느낌이 안든다. 측정하려는 값과 단위를 표현하는 이름이 필요하다.

```java
int elapsedTimeInDays;
int daysSinceCreation;
int daysSinceModification;
int fileAgeInDays;
```

의도가 드러나는 이름을 사용하면 코드 이해와 변경이 쉬워진다.

다음 코드는 무엇을 할까?

```java
public List<int []) getThem() {
  List<int []> list1 = new ArrayList<int[]>();
  for (int [] x : theList)
  	if (x[0] == 4)
      	list1.add(x);
  return list1;
}
```

코드가 하는 일을 짐작하게 어렵다. 복잡한 문장은 없다.

문제는 코드의 단순성이 아니라 코드의 함축성이다. 코드 맥락이 코드 자체에 명시적으로 드러나지 않는다.

```java
public Likst<int []> getFlaggedCells() {
  List<int []> flaggedCells = new Arraylist<int []>();
  for (int [] cell : gameBoard)
    	if (cell[STATUS_VALUE] == FLAGGED)
        	flaggedCells.add(cell);
  return flaggedCells;
}
```

이를 조금 더 개선하면

```java
public Likst<Cell> getFlaggedCells() {
  List<Cell> flaggedCells = new Arraylist<Cell>();
  for (Cell cell : gameBoard)
    	if (cell.isFlagged())
        	flaggedCells.add(cell);
  return flaggedCells;
}
```

단순히 이름만 고쳤는데도 함수가 하는 일을 이해하기 쉬워졌다. 바로 이것이 좋은 이름이 주는 위력이다.



##### 그릇된 정보를 피하라

* hp, aix, sco는 변수 이름을 적합하지 않다.

* 실제 List가 아니라면 accountList라 명명하지 않는다.
* 서로 흡사한 이름을 사용하지 않도록 주의한다.
    * 한 모듈에서 XYXControllerForEfficientHandlingOfStrings라는 이름을 사용하고 다른 모듈에서 XYXControllerForEfficientStorageOfStrings라는 이름을 사용한다면 그 차이는?

* 유사한 개념은 유사한 표기법을 사용한다.
* 소문자 L이나 대문자 O변수다.

* 소문자 L은 숫자 1처럼 보이고 대문자 O는 숫자 0처럼 보인다.

```java
int a = 1;
if (O == 1)
  a == O1;
else
  1 = 01;
```



##### 의미 있게 구분하라

```java
public static void copyChars(char a1[], char a2[]) {
  for (int i=0; i < a1.length; i++) {
  	a2[i] = a1[i];
  }
}
```

* 함수 인수 이름으로 source, destination을 사용한다면 코드 읽기가 훨씬 더 쉬워진다.
* 불용어를 추가한 이름 역시 아무런 정보를 제공하지 못한다. 
* Product라는 클래스, ProductInfo, ProductData라 부른다면 Info나 Data는 a, an, the와 마찬가지로 의미가 불분명한 불용어다.
* 불용어를 사용하지 말라는 이야기는 아니다.
* zork라는 변수가 있다는 이유만으로 theZork라 이름 지어서는 안된다는 말이다.
* 불용어는 중복이다. variable, table이라는 단어도 마찬가지다.
* NameString이 Name보다 뭐가 나은가?
* Customer와 CustomerObject의 차이를 알겠는가?

```java
getActiveAccount();
getActiveAccounts();
getActiveAccountInfo();
```

* 명확한 관계가 없다면 변수 moneyAmount와 money는 구분이 안된다.
* customerInfo와 customer, accountData는 account와, theMessage와 message와 구분이 안 된다.
* 읽는 사람이 차이를 알도록 이름을 지어라.



##### 발음하기 쉬운 이름을 사용하라

```java
class DtaRcrd102 {
  private Date genymdhms;
  private final String pszqint = "102";
}
```

젠 와이 엠 디 에이취 엠 에스


```java
public Customer {
  private Date generationTimestamp;
  private Date modificationTimestamp;
  private final String recordId = "102";
}
```



##### 검색하기 쉬운 이름을 사용하라

* MAX_CLASS_PER_STUDENT는 grep으로 찾기가 쉽지만, 숫자 7은 은근히 까다롭다.
* e라는 문자도 변수 이름으로 적합하지 못하다.
* 개인적으로는 간단한 메서드에서 로컬 변수만 한 문자를 사용한다.



```java
for (int j=0; j<34; j++) {
  s += (t[j]*4)/5;
}
```

```java
int realDaysPerIdealDay = 4;
const int WORK_DAYS_PER_WEEK = 5;
int sum = 0;
for (int j=0; j < NUMBER_OF_TAKS; j++) {
  int realTaskDays = taskEstimate[j] * realDaysPerIdealDay;
  int realTaskWeeks = (realTaskDays / WORK_DAYS_PER_WEEK);
  sum += realTaskWeeks;
}
```

위의 코드에서 WORK_DAYS_PER_WEEK를 찾기가 얼마나 쉬운지 생각해보라.



##### 인코딩을 피하라

###### 헝가리식 표기법

* 포트란은 첫 글자로 유형을 표현했다.
* 초창기 베이식은 글자 하나에 숫자 하나만 허용했다.
* 과거 윈도 C API는 헝가리식 표기법을 굉장이 중요하게 여겼다.
* 자바 프로그래머는 변수 이름에 타입을 인코딩할 필요가 없다.

```java
PhoneNumber phoneString;
// 타입이 바뀌어도 이름은 바꾸지 않는다.
```



###### 멤버 변수 접두어

멤버 변수에 m_이라는 접두어를 붙일 필요가 없다.

```java
public class Part {
  private String m_dsc; // 설명 문자열
  void setName(String name) {
    m_dsc = name;
  }
}
```



```java
public class Part {
  String description;
  void setDescription(String description) {
    this.description = description;
  }
}
```



###### 인터페이스 클래스와 구현 클래스

* 인터페이스 클래스와 구현 클래스. IShapeFactory와 ShapeFactory?
* 개인적으로 인터페이스 이름은 접두어를 붙이지 않는 편이 좋다고 생각한다.
* 옛날 코드에서 많이 사용하는 접두어 I는 주의를 흐트리고 과도한 정보를 제공한다.
* 차라리 구현 클래스에 붙이는 것이 낫다. (ShapeFactoryImp나 CShapeFactory가 IShapeFactory보다 좋다)



##### 자신의 기억력을 자랑하지 마라

* 문자 하나만 사용하는 변수 이름은 문제가 있다.
* 루프에서 반복 횟수를 세는 변수 i,j,k는 괜찮다. (l은 절대 안된다)
* r이라는 변수가 호스트와 프로토콜을 제외한 소문자 URL이라는 사실을 기억한다면 확실히 똑똑한 사람이다.
* 전문가 프로그래머는 명료함이 최고라는 사실을 이해한다.



##### 클래스 이름

* 클래스 이름과 객체 이름은 명사나 명사구가 적합하다.

* Customer, WikiPage, Account, AddressParser 등이 좋은 예다.
* Manager, Processor, Data, Info 등과 같은 단어는 피하고 동사는 사용하지 않는다.



##### 메서드 이름

* 메서드 이름은 동사나 동사구가 적합하다.
* postPayment, deletePage, save 등이 좋은 예다.
* 접근자, 변경자, 조건자는 javabean 표준에 따라 값 앞에 get, set, is를 붙인다.

```java
String name = employee.getName();
customer.setName("mike");
if (paycheck.isPosted()) ...
```



##### 기발한 이름은 피하라

* HolyHandGrenade라는 함수가 무엇을 하는지 알겠는가? 기발한 이름이지만 DeleteItems가 더 좋다.

* kill대신 whack()이나 abort() 대신 eatMyShort()라 부른다.



##### 한 개념에 한 단어를 사용하라

* 똑같은 메서드를 클래스마다 fetch, retrieve, get으로 부르면 혼란스럽다.

* 동일 코드 기반에 controller, manager, driver 섞어 쓰면 혼란스럽다.



##### 말장난을 하지 마라

* 한 단어를 두 가지 목적으로 사용하지 마라. 다른 개념에 같은 단어를 사용한다면 그것은 말장난에 불과하다.
* add 메서드: 기존 값 두 개를 더하거나 새로운 값을 만든다고 가정
* 새로 작성하는 메서드 집합에 값 하나 추가 <-- 이 메서드를 add라 불러야 할까? Insert, append일까?



##### 해법 영역에서 가져온 이름을 사용하라

* 전산 용어, 알고리즘 이름 ,패턴 이름, 수학 용어 등을 사용해도 괜찮다.

* AccountVisitor, JobQueue



##### 문제 영역에서 가져온 이름을 사용하라.

* 적절한 '프로그래머 용어'가 없다면 문제 영역에서 이름을 가져온다.
* 문제 영역 개념과 관련이 깊은 코드라면 문제 영역에서 이름을 가져와야 한다.



##### 의미 있는 맥락을 추가하라

* state가 주소의 일부라는 것을 알까?
* addrFirstName, addrLastName, addrState라 쓰면 맥락이 좀 더 분명해진다.



##### 불필요한 맥락을 없애라

* 고급 휘발유 충전소(Gas Station Deluxe)라는 애플리케이션을 짠다고 가정하자. 
* 모든 클래스 이름을 GSD로 시작하겠다는 생각은 전혀 바람직하지 못하다.
* accountAccount와 customerAddress는 Address 클래스 인스턴스로는 좋은 이름이나 클래스 이름으로는 적합하지 못하다.
* Address는 클래스 이름으로 적합하다.



##### 마치면서

* 좋은 이름을 선택하려면 설명 능력이 뛰어나야 하고 문화적인 배경이 같아야 한다. 이것이 제일 어렵다.
* 좋은 이름을 선택하는 능력은 기술, 비즈니스, 관리 문제가 아니라 교육 문제다.





