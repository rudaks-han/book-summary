# 4장 문서 관리 시스템

## 4.2 목표

* 문서 관리 기능 설계의 핵심은 상속 관계, 즉 어떻게 클래스를 상속하거나 인터페이스를 구현하는가에 달렸다.
* 문서 관리 기능을 제대로 설계하려면 리스코프 치환 원칙을 알아야 한다.
* 언제 상속을 사용해야 하는지와 관련해 '상속보다 조합' 원칙도 알아야 한다.



## 4.3 문서 관리 시스템 요구 사항

문서 관리 시스템은 기존 환자 정보 파일을 읽어 색인을 추가하고 검색할 수 있는 형태의 정보로 변환해야 한다. 그녀는 다음과같은 세 가지 형식의 문서를 다룬다.

* 리포트: 환자의 수술과 관련된 상담 내용을 기록한 본문이다.
* 우편물: 특정 주소로 발송되는 텍스트 문서다.
* 이미지: 차이와 잇몸 엑스레이 사진을 저장한다. 용량이 크다.



## 4.4 설계 작업

문서 관리 시스템은 필요에 따라 문서를 임포트해 내부 문서 저장소에 추가한다. 두 메시더를 포함하는 DocumentManagementSystem 클래스를 만들어 이 요구사항을 구현한다.

* void importFile(String path)
* List< Document > contents()



### 4.4.1 임포터

파일의 확장자로 파일을 어떻게 임포트할지 결정할 수 있다. 지금까지 우편물은 .letter, 리포트는 .report, 이미지는 .jpg인 전용 확장자를 사용해왔다.

확장자 switch 문 예제

```java
switch (extension) {
  case "letter":
    // 우편물 임포트 코드
    break;
  
  case: "report":
    // 레포트 임포트 코드
    break;
    
  case "jpg":
    //이미지 임포트 코드
    break;
    
  default:
    throw new UnknownFileTypeException("For file: " + path);
}
```

* 위 코드로 문제를 해결할 수 있지만 확장성은 부족하다. 
* 다른 종류의 파일을 추가할 때마다 switch문에 다른 항목을 추가해 구현해야 하기 때문이다. 
* 메인 클래스를 깔끔하고 단순하게 유지하기 위해 다양한 문서를 임포트하는 클래스로 분리하면, 각각의 임포트 동작을 따로 처리하므로 찾기 쉽고, 이해하기 쉬운 코드를 만들 수 있다.
* 파일을 임포트하려는 파일은 어떻게 표현해야 할까?
    * 파일 경로를 단순히 String으로 표현하거나 java.io.File 처럼 파일을 가리키는 클래스를 사용하는 다양한 방법이 있다.
    * 강한 형식의 원칙을 적용하기 좋은 상황이다.
    * String 대신 파일을 가리키는 전용 형식을 이용하므로 오류가 발생할 범위를 줄인다.



```java
interface Importer {
  Document importFile(File file) throws IOException;
}
```



### 4.4.2 Document 클래스

* Docuement 클래스를 정의한다.

* 각 문서는 검색할 수 있는 다양한 속성을 포함한다.

* 문서의 종류에 따라 포함하는 속성이 달라진다.

* 가장 간단한 방법은 Map<String, String>으로 속성 이름을 값과 매핑하는 방법이다. 

* 응용 프로그램에 직접 Map<String, String>을 사용하지 않는 이유가 뭘까?  

* 응용프로그램의 유지보수성과 가동성을 고려해야 하는 일이다.

* 응용프로그램에 직접 Map<String, String>을 사용하지 않는 이유가 뭘까?

* 한 문서를 모델링하려고 새 도메인 클래스를 소개하는 것은 식은 죽 먹기처럼 간단히 결정할 수 있는 일이 아니라 응용프로그램의 유지보수성과 가독성을 고려해야 하는 일이다.

    

* 우선 응용 프로그램의 컴포넌트 이름을 구체적으로 지어야 함의 중요성은 아무리 강조해도 지나치지 않다. 
* 의사소통은 왕이다! 
* 훌륭한 소프트웨어 개발팀은 유비쿼터스 언어로 자신의 소프트웨어를 작성한다. 
* 아바즈 선생님이 고객과 대화할 때 사용하는 용어를 응용프로그램의 코드와 같은 의미로 사용하면 유지보수가 쉬워진다.
* 동료나 고객과 대화할 때 소프트웨어의 다양한 기능을 어떤 공통 언어로 약속한다. 
* 이때 사용한 어휘를 코드로 매핑하면 코드의 어떤 부분을 바꿔야 하는지 쉽게 알 수 있다. 이를 발견성(discoverability)이라 한다.

> 유비쿼터스 언어는 에릭 에번스가 집필한 도메인 주도 설계에서 처음 등장했다. 유비쿼터스 언어란 개발자와 사용자 모두가 사용할 수 있도록 설계, 공유된 공통 언어를 말한다.



* 강한 형식을 이용하면 데이터의 사용 방법을 규제할 수 있다. 
* 예를 들어 Docoment 클래스는 불변 클래스, 즉 클래스를 생성한 다음에는 클래스의 속성을 바꿀 수 없다. 
* Document가 HashMap<String, String>을 상속받도록 설계를 결정한 개발자도 있을 것이다. 
* HashMap은 Document 모델링에 필요한 모든 기능을 포함하므로 처음에는 이 결정이 좋아보일 수 있다. 
* 하지만 이런 설계 방법에는 몇 가지 문제가 있다.
* 소프트웨어를 설계할 때 필요한 기능은 추가하면서 동시에 불필요한 기능은 제한해야 한다. 
* Document 클래스가 HashMap을 상속하면서 응용프로그램이 Document 클래스를 바꿀 수 없도록 결정한다면 이전에 불변성으로 얻을 수 있는 모든 이득이 단번에 사라진다.

요약하자면 도메인 클래스를 이용하면 개념에 이름을 붙이고 수행할 수 있는 동작과 값을 제한하므로 <u>발견성을 개선하고 버그 발생 범위를 줄일 수 있다</u>.

```java
public class Document {
  private final Map<String, String> attributes;
  
  Document(final Map<String, String> attributes) {
    this.attributes = attributes;
  }
  
  public String getAttribute(final String attributeName) {
    return attributes.get(attributeName);
  }
}
```



### 4.4.3 Document 속성 및 계층

* Document 클래스는 속성에 String을 사용했다.
* 강한 형식과는 거리가 먼 결정이지 않은가?
* 문자열보다는 정수값이 필요할 수도 있지만 문서 관리 시스템에서는 이런 기능이 필요하지 않을 뿐이다.
* 2장에서 KISS 를 설명했다. KISS란 단순할수록 좋다는 의미다.
* 어쩔 수 없이 상황이 복잡해질 수 있지만 그래도 되도록 단순하게 일을 처리하려는 노력이 필요하다.
* 누군가 '어쩌면 X가 필요할지 몰라요' 또는 'Y도 할 수 있으면 좋을 텐데요'라고 말한다면 단호히 '아니요'라고 대답하자.
* <u>좋은 의도를 가진 확장성, 반드시 필요한 기능보다는 있으면 좋은 기능의 코드를 추가하다보면 결국 설계가 얼룩지고 복잡해진다.</u>



### 4.4.4 임포터 구현과 등록

ImageImporter

```java
class ImageImporter implements Importer {
    @Override
    public Document importFile(final File file) throws IOException {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(PATH, file.getPath());

        final BufferedImage image = ImageIO.read(file);
        attributes.put(WIDTH, String.valueOf(image.getWidth()));
        attributes.put(HEIGHT, String.valueOf(image.getHeight()));
        attributes.put(TYPE, "IMAGE");

        return new Document(attributes);
    }
}
```

* 속성명은 Attributes 클래스에 정의된 상수다.
* 예를 들어 Path를 path라고 잘못 사용할 수 있다.

자바에서 상수를 정의하는 방법

```java
public static final String PATH = "path";
```



임포터 등록

```java
private final Map<String, Importer> extensionToImporter = new HashMap<>();

public DocumentManagementSystem() {
  extensionToImporter.put("letter", new LetterImporter());
  extensionToImporter.put("report", new ReportImporter());
  extensionToImporter.put("jpg", new ImageImporter());
}
```



## 4.5 리스코프 치환 원칙(LSP)

리스코프 치환원칙: 간편하게 자식 클래스는 부모로부터 물려받은 행동을 유지해야 한다고 생각하자.

#### 하위형식에서 선행조건을 더할 수 없음

* LSP란 부모가 지정한 것보다 더 많은 선행조건을 요구할 수 없음을 의미한다.
* 예를 들어 부모가 문서의 크기를 제한하지 않았다면, 여러분의 문서의 크기가 100KB보다 작아야 한다고 요구할 수 없다.

#### 하위형식에서 후행조건을 약화시킬 수 없음

* 후행조건은 어떤 코드를 실행한 다음에 만족해야 하는 규칙이다.
* 예를 들어 유효한 파일에 importFile()을 실행했다면 contents()가 반환하는 문서 목록에 그 파일이 반드시 포함되어야 한다.

#### 슈퍼형식의 불변자는 하위형식에서 보존됨

* 불변자란 밀물과 썰물처럼 항상 변하지 않는 어떤 것을 가리킨다.
* 부모 클래스에서 유지되는 모든 불변자는 자식 클래스에서도 유지되어야 한다.

#### 히스토리 규칙

* 기본적으로 자식 클래스는 부모가 허용하지 않은 상태 변화를 허용하지 않아야 한다.
* Document는 바꿀 수 없는 불변 클래스다.
* Document 클래스를 인스턴스화한 다음에는 어떤 속성도 삭제, 추가, 변경할 수 없다.



## 4.6 대안

* 임포터의 클래스 계층을 만들고 인터페이스 대신 가장 상위에 Importer 클래스를 만드는 방법을 선택할 수도 있다. 
* 인터페이스와 클래스는 서로 다른 기능을 제공한다. 
* 인터페이스는 여러 개를 한 번에 구현할 수 있는 반면, 클래스는 일반 인스턴스 필드와 메서드를 갖는다.
* 다양한 임포트를 사용하도록 계층을 만든다. 
* 쉽게 망가질 수 있는 상속 기반의 클래스를 피해야 한다고 설명했듯이 인터페이스를 이용하는 것이 클래스를 이용하는 것보다 명백하게 좋은 선택이다.
* 모든 상황에서 클래스보다 인터페이스가 좋다는 얘기가 아니므로 오해하지 말자.
* 문제를 해결하려는 도메인에 상태와 많은 동작이 관련되어 있어서 강력한 **is a 관계**를 모델링해야 하는 상황이라면 클래스 기반의 상속이 더 적절하다.



### 4.6.2 영역, 캡슐화 선택하기

* 구현 중인 패키지의 세부 정보를 외부로 노출했다면 리팩터링이 어려워진다.
* 클래스가 외부로 노출되지 않도록 패키지 영역을 적적으로 적용하면 내부 설계를 쉽게 바꿀 수 있다.



## 4.7 기존 코드 확장과 재사용

청구서 예제

---

Dear Joe Bloggs

Here is you invoice for the dental treatment that you received.

Amount: $100

Regards,

Dr Avaj

Awesome Dentist

---

우편물 예제

---

Dear Joe Bloggs

123 Fake Street
Westminster
London
United Kingdom

We are writing to you to confirm the re-scheduling of your appointment with Dr. Avaj from 29th December 2016 to 5th January 2017.

Regards,

Dr Avaj

Awesome Dentist

---

리포트 예제

---

Patient: Joe Bloggs

On 5th January 2017 I examined Joe's teeth
We discussed his switch from drinking Coke to Diet Coke
No new problems were noted with his teeth.

---



### 4.7.1 유틸리티 클래스 사용

* 가장 간단한 방법은 유틸리티 클래스를 만드는 것이다. 
* ImportUtil 클래스를 만들어 여러 임포트에서 공유해야 하는 기능을 이 유틸리티 클래스에 구현한다.
* 유틸리티 클래스는 가장 그럭저럭 단순하고 쓸만하지만 객체지향 프로그래밍의 지향점과는 거리가 멀다. 
* 객체지향에서는 클래스로 기능을 마든다. 
* 인스턴스를 만들고 싶다면 무조건 new Thing()을 호출한다.
* 유틸리티 클래스는 이런 예상을 뒤엎으며 보통 어떤 한 의무나 개념과 상관없는 다양한 코드의 모음으로 귀결된다. 
* 시간이 흐를수록 이는 갓 클래스의 모양을 갖춰간다. 
* 즉 여러 의무를 담당하는 한 개의 거대 클래스가 탄생한다.



### 4.7.2 상속 사용

* 각각의 임포터가 TextImport 클래스를 상속받는 방법이다. 
* TextImporter 클래스에 모든 공통 기능을 구현하고 서브클래스에서는 공통 기능을 재사용한다.
* 시간이 흐르고 응용프로그램이 바뀔 때, 응용프로그램을 그에 맞게 바꾸는 것보다는 변화를 추상화하는 것이 더 좋다. 
* 일반적으로 상속 관계로 코드를 재사용하는 것은 좋은 방법이 아니다.



### 4.7.3 도메인 클래스 사용

* 마지막으로 도메인 클래스로 텍스트 파일을 모델링하는 방법이 있다. 
* 먼저 기본 개념을 모델링 한 다음, 기본 개념이 제공하는 메서드를 호출해 다양한 임포터를 만든다. 
* 여기서 기본 개념이 뭘까? 예제에서는 텍스트 파일의 내용을 처리해야 하므로 TextFile 클래스를 사용한다. 
* 새롭거나 창의적이지 않다는 점이 바로 핵심이다.
* 클래스 이름이 매우 단순 명료해 텍스트 파일을 조작하는 함수를 어디에 추가할지 쉽게 알 수 있다.



#### 도메인 클래스 구현

```java
class TextFile {
  private final Map<String, String> attributes;
  private final List<String> lines;
  // 클래스 계속됨...
}
```



## 4.8 테스트 위생

자동화된 테스트는 퇴행(regression)이 발생하는 범위를 줄이며 어떤 동작이 문제를 일으켰는지 이해할 수 있도록 도와준다. 또한 자동화된 테스트가 있으면 자신 있게 코드를 리팩터링할 수 있다. 이런 호화를 누리려면 코드를 많이 구현해보고 유지보수해야 하기 때문이다.

테스트 유지보수 문제를 해결하려면 테스트 위생을 지켜야 한다. 테스트 위생이란 테스트 대상 코드베이스뿐 아니라 테스트 코드도 깔끔하게 유지하며 유지보수하고 개선해야 함을 의미한다.



### 4.8.1 테스트 이름 짓기

이름 짓기에도 여러 안티 패턴이 존재한다. 가령 test1처럼 말도 안 되는 테스트 이름은 최악의 안티 패턴이다. test1은 뭘 테스트하는 걸까?

흔히 발생하는 안티 패턴으로 file, document 처럼 개념이나 명사로 테스트의 이름을 결정하는 것이다. 테스트 이름은 개념이 아니라 테스트하는 동작을 묘사해야 한다. 테스트 중 실행하는 메서드명을 그대로 사용하는 것도 또 다른 안티 패턴이다. 예를 들어 테스트 이름을 importFile로 짓는 실수를 할 수 있다.























