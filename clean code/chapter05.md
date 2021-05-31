# 5장 형식 맞추기

뚜껑을 열었을 때 독자들은

* 코드가 깔끔하고
* 일관적이며
* 꼼꼼하다고 감탄하면 좋겠다.
* 질서 정연하다고 탄복하면 좋겠다.
* 모듈을 읽으며 두 눈이 휘둥그래 놀라면 좋겠다.
* 전문가가 짰다는 인상을 심어주면 좋겠다.

프로그래머라면 형식을 깔깜하게 맞춰 코드를 짜야 한다. 팀으로 일한다면 팀이 합의해 규칙을 정하고 모두가 그 규칙을 따라야 한다.



##### 형식을 맞추는 목적

코드 형식은 중요하다! 너무 중요해서 무시하기 어렵다.
코드 형식은 의사소통의 일환이다. 의사소통은 전문 개발자의 일차적인 의무다.

어쩌면 '돌아가는 코드'가 전문 개발자의 일차적인 의무라 여길지도 모르겠다.
오늘 구현한 기능이 다음 버전에서 바뀔 확률은 아주 높다.
코드가 바뀌어도 맨 처음 잡아놓은 구현 스타일과 가독성 수준은 유지보수 용이성과 확장성에 계속 영향을 미친다. 원래 코드는 사라질지라도 개발자의 스타일과 규율은 사라지지 않는다.



##### 적절한 행 길이를 유지하라

소스 코드는 얼마나 길어야 적당할까?

* jUnit, FitNesse, Time and Money: 대다수 200줄 미만
* Tomcat, Ant: 절반 이상이 200줄이 넘어선다
* Fitnesse: 평균 65줄

우리에게 무엇을 말하느냐고? 대부분 200줄 정도 파일로도 커다란 시스템을 구축할 수 있다는 사실이다. 일반적으로 큰 파일보다 작은 파일이 이해하기 쉽다.



###### 신문 기사처럼 작성하라

아주 좋은 신문 기사를 떠올려보라. 독자는 위에서 아래로 읽는다.
첫 문단은 전체 기사 내용을 요약한다. 세세한 사실은 숨기고 커다란 그림을 보여준다. 쭉 읽으며 내려가면 세세한 사실이 조금씩 드러난다. 날짜, 이름, 발언, 주장, 기타 세부사항이 나온다.

소스 파일도 신문 기사처럼 작성한다.

* 이름은 간단하면서도 설명이 가능하게 짓는다. 
* 이름만 보고도 올바른 모듈을 살펴보고 있는지 판단할 정도로 신경써서 짓는다.
* 소스파일 첫 부분은 고차원 개념과 알고리즘을 설명한다.
* 아래로 내려갈수록 의도를 세세하게 묘사한다.
* 마지막에는 가장 저차원 함수와 세부 내역이 나온다.



###### 개념은 빈 행으로 분리하라

빈 행은 새로운 개념을 시작한다는 시각적 단서다. 코드를 읽어내려가다 보면 빈 행 바로 다음 줄에 눈길이 멈춘다.



###### 세로 밀집도

서로 밀접한 코드 행은 세로로 가까이 놓여야 한다는 뜻이다.



###### 수직 거리

서로 밀접한 개념은 세로로 가까이 둬야 한다.



**변수 선언.** 변수는 사용하는 위치에 최대한 가까이 선언한다.

**인스턴스 변수.** 인스턴스 변수는 클래스 맨 처음에 선언한다.

**종속 함수.** 한 함수가 다른 함수를 호출한다면 두 함수는 세로로 가까이 배치한다. 또한 가능하다면 호출하는 함수를 호출되는 함수보다 먼저 배치한다. 그러면 프로그램이 자연스럽게 읽힌다.

**개념적 유사성.** 어떤 코드는 서로 끌어당긴다. 개념적인 친화도가 높기 때문이다. 친화도가 높을수록 코드를 가까이 배치한다.



###### 세로 순서

호출되는 함수를 호출하는 함수보다 나중에 배치한다. 그러면 소스 코드 모듈이 고차원에서 저차원으로 자연스럽게 내려간다.



##### 가로 형식 맞추기

한 행은 가로로 얼마나 길어야 적당할까?

100자나 120자에 달해도 나쁘지 않다. 그 이상은 솔직히 주의부족이다.

예전에는 오른쪽으로 스크롤할 필요가 절대로 없게 코드를 짰다. 하지만 요즘 모니터는 아주 크다. 글꼴 크기를 줄여서 200자로 맞추긴 하지만 가급적 그렇게 하지 마라.
개인적으로 120자 정도로 행 길이를 제한한다.



###### 가로 공백과 밀집도

```java
private void measureLine(String line) {
    lineCount++;
    int lineSize = line.length();
    totalChars += lineSize;
    lineWidthHistogram.addLine(lineSize, lineCount);
    recordWidestLine(lineSize);
}
```

* 할당 연산자를 강조하려고 앞뒤에 공백을 줬다.
* 함수 이름과 이어지는 괄호 사이에는 공백을 넣지 않았다. 



연산자 우선순위를 강조하기 위해서도 공백을 사용한다.

```java
public class Quadratic {
  public static double root1(double a, double b, double c) {
    double determinant = determinant(a, b, c);
    return (-b + Math.sqrt(determinant)) / (2*a);
  }
  
  public static double root2(int a, int b, int c) {
    double determinant = determinant(a, b, c);
    return (-b - Math.sqrt(determinant)) / (2*a);
  }
  private static double determinant(double a, double b, double c) {
    return b*b - 4*a*c;
  }
}
```

승수 사이에는 공백이 없다 .곱셈은 우선순위가 가장 높기 때문이다.



###### 가로 정렬

```java
public class FitNesseExpediter implements ResponseSender {
  private		Socket					socket;
  private		InputStream			  input;
  private		outputStream		  input;
  private		Request					request;
  private		Response			    response;
  private		FitnesseContext		context;
  
  public FitNesseExpediter(Socker			      s,
                        				     FitNesseContext context) throws Exception {
  	this.context = 					    context;
 	 	socket =						   	  s;
  	input = 								s.getInputStream();
	  output =						   	  s.getOutputStream();
	  requestParsingTimeLimit = 1000;
	}
}
```

이렇게 정렬하는게 별로 유용하지 못하다. 코드가 엉뚱한 부분을 강조해 진짜 의도가 가려지기 때문이다.
위 선언부를 읽다보면 변수 유형은 무시하고 변수 이름부터 읽게 된다.

그래서 아래코드 처럼 정렬해야 한다.

```java
public class FitnesseExpediter implements ResponseSender {
  private Socket socket;
  private InputStream input;
  private outputStream input;
  private Request request;
  private Response response;
  private FitnesseContext context;
  
  public FitNesseExpediter(Socker s, FitNesseContext context) throws Exception {
  	this.context = context;
 	 	socket = s;
  	input = s.getInputStream();
	  output = s.getOutputStream();
	  requestParsingTimeLimit = 1000;
	}
}
```



###### 들여쓰기

소스 파일은 윤곽도(outline)와 비슷하다. 
파일 전체에 적용되는 정보가 있고, 파일 내 개별 클래스에 적용되는 정보가 있고, 클래스 내 각 메서드에 적용되는 정보가 있고, 블록 내 블록에 재귀적으로 적용되는 정보가 있다.

이렇듯 범위로 이뤄진 계층을 표현하기 위해 우리는 코드를 들여쓴다.

* 클래스 내 메서드는 클래스보다 한 수준 들여쓴다.
* 메서드 코드는 메서드 선언보다 한 수준 들여쓴다. 
* 블록 코드는 블록을 포함하는 코드보다 한 수준 들여쓴다.

다음 두 코드는 문법과 의미가 동일하다.

```java
public class FitNesseServer implements SocketServer { private FitNesseContext context;  public FitNesseServer(FitNesseContext context) { this.context = context;  }  public void serve(Socket s) { serve(s, 10000); }  public void serve(Socket s, long requestTimeout) { try { FitNesseExpediter sender = new FitNesseExpediter(s, context);
 sender.setRequestParsingTImeLimit(requestTimeout); sender.start(); } catch (Exception e) { e.printStackTrace(); } } }
```

```java
public class FitNesseServer implements SocketServer {
  private FitNesseContext context;
  public FitNesseServer(FitNesseContext context) {
    this.context = context;
  }
  
  public void serve(Socket s) {
    serve(s, 10000);
  }
  
  public void serve(Socket s, long requestTimeout) {
    try {
      FitNesseExpediter sender = new FitNesseExpediter(s, context);
      sender.setRequestParsingTImeLimit(requestTimeout);
      sender.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
```

들여쓰기한 파일은 구조가 한눈에 들어온다. 반면, 들여쓰기 하지 않은 코드는 열심히 분석하지 않는 한 거의 불가능하다.

들여쓰기 무시하기. 때로는 간단한 If문, 짧은 while문, 짧은 함수에서 들여쓰기 규칙을 무시하고픈 유혹이 생긴다. 
이런 유혹에 빠질 때마다 나는 항상 원점으로 돌아가 들여쓰기를 넣는다.

```java
public class CommentWidget extends TextWidget {
  public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?";
  
  public CommentWidget(ParentWidget parent, String text) { super(parent, text);}
  public String render() throws Exception { return ""; }
}
```

대신, 다음과 같이 들여쓰기로 범위를 제대로 표현한 코드를 선호한다.

```java
public class CommentWidget extends TextWidget {
  public static final String REGEXP = "^#[^\r\n]*(?:(?:\r\n)|\n|\r)?";
  
  public CommentWidget(ParentWidget parent, String text) { 
    super(parent, text);
  }
  
  public String render() throws Exception { 
    return ""; 
  }
}
```



###### 가짜 범위

때로는 빈 while문이나 for 문을 접한다. 나는 이런 구조를 좋아하지 않기에 가능한 한 피하려 애쓴다.

```java
while (dis.read(buf, 0, readBufferSize) != -1)
;
```



##### 팀 규칙

프로그래머라면 각자 선호하는 규칙이 있다. 하지만 팀에 속한다면 자신이 선호해야 할 규칙은 바로 팀 규칙이다.
팀은 한 가지 규칙에 합의해야 한다. 그리고 모든 팀원은 그 규칙을 따라야 한다. 그래야 소프트웨어가 일관적인 스타일을 보인다.





