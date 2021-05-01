# 4장 주석

> 나쁜 코드에 주석을 달지 마라. 새로 짜라.
>
> - 브라이언 W. 커니핸, P.J.플라우거

잘 달린 주석은 그 어떤 정보보다 유용하다. 경솔하고 근거 없는 주석은 코드를 이해하기 어렵게 만든다. 오래되고 조잡한 주석은 거짓과 잘못된 정보를 퍼뜨려 해악을 미친다.

주석은 '순수하게 선하지' 못하다. 사실상 주석은 기껏해야 필요악이다.
프로그래밍 언어 자체가 표현력이 풍부하다면, 아니 우리에게 프로그래밍 언어를 치밀하게 사용해 의도를 표현할 능력이 있다면, 주석은 거의 필요하지 않으리라. 아니, 전혀 필요하지 않으리라.

우리는 코드로 의도를 표현하지 못해, 그러니깐 실패를 만회하기 위해 주석을 사용한다. <u>주석은 언제나 실패를 의미한다.</u>
때때로 주석 없이는 자신을 표현할 방법을 찾지 못해 할 수 없이 주석을 사용한다.

그러므로 주석이 필요한 상황에 처하면 곰곰히 생각하기 바란다. 상황을 역전해 코드로 의도를 표현할 방법은 없을까?
<u>주석을 달 때마다 자신에게 표현력이 없다는 사실을 푸념해야 마땅하다.</u>

주석은 오래될수록 코드에서 멀어진다. 오래될수록 완전히 그릇될 가능성도 커진다. 이유는 단순하다. 프로그래머들이 주석을 유지하고 보수하기란 현실적으로 불가능하니까.

프로그래머들이 주석을 엄격하게 관리해야 한다고, 그래서 복구성과 관련성과 정확성이 언제나 높아야 한다고 주장할지도 모르겠다. 그 의견에 동의한다.
하지만 나라면 코드를 깔끔하게 정리하고 표현력을 강화하는 방법으로, 그래서 애초에 주석이 필요 없는 방향으로 에너지를 쏟겠다.

부정확한 주석은 아예 없는 주석보다 훨씬 더 나쁘다. 부정확한 주석은 독자를 현혹하고 오도한다. 부정확한 주석은 결코 이뤄지지 않을 기대를 심어준다.
진실은 한곳에만 존재한다. 바로 코드다. 코드만이 자기가 하는 일을 진실되게 말한다. <u>그러므로 우리는 주석을 가능한 줄이도록 꾸준히 노력해야 한다.</u>



##### 주석은 나쁜 코드를 보완하지 못한다.

코드에 주석을 추가하는 일반적인 이유는 코드 품질이 나쁘기 때문이다.
모듈을 짜고 보니 짜임새가 엉망이고 알아먹기 어렵다. 그래서 자신에게 이렇게 말한다.
"이런! 주석을 달아야겠다!" 아니다! 코드를 정리해야 한다!

표현력이 풍부하고 깔끔하며 주석이 거의 없는 코드가, 복잡하고 어수선하며 주석이 많이 달린 코드보다 훨씬 좋다.
<u>자신이 저지른 난장판을 주석으로 설명하려 애쓰는 대신에 그 난장판을 깨끗이 치우는 데 시간을 보내라!</u>



##### 코드로 의도를 표현하라!

어느 코드가 더 나은가?

```java
// 직원에게 복지 혜택을 받을 자격이 있는지 검사한다.
if ((employee.flags && HOURLY_FLAGS) && (employee.age > 65))
```

다음 코드

```java
if (employee.isEligibleForFullBenefits())
```

몇 초만 더 생각하면 코드로 대다수 의도를 표현할 수 있다.



##### 좋은 주석

###### 법적인 주석

각 소스 파일 첫 머리에 주석으로 들어가는 저작권 정보와 소유권 정보는 필요하고도 타당하다.

```java
// Copyright (C) 2003,2004,2005 by Object  Mentor, Inc. All rights reserved.
// GNU General Public License 버전 2 이상을 따르는 조건으로 배포한다.
```



###### 정보를 제공하는 주석

때로는 기본적인 정보를 주석으로 제공하면 편리하다.

```java
// 테스트 중인 Responder 인스턴스를 반환한다.
protected abstract Responder responderInstance();
```

위 코드는 함수 이름을 responderBeingTested로 바꾸면 주석이 필요 없어진다.

```java
// kk:mm:ss EEE, MMM dd, yyyy 형식이다.
Pattern timeMatcher = Pattern.compile(
"\\d*:\\d*:\\d* \\w* \\d*, \\d*");
```

이왕이면 시각과 날짜를 변환하는 클래스를 만들어 코드로 옮겨주면 더 좋고 더 깔끔하겠다.



###### 의도를 설명하는 주석

// 오른쪽 유형이므로 정렬 순위가 더 높다.

// 스레드를 대량 생성하는 방법으로 어떻게든 경쟁 조건을 만들려 시도한다.



###### 의미를 명료하게 밝히는 주석

인수나 반환값이 표준 라이브러리나 변경하지 못하는 코드에 속한다면 의미를 명료하게 밝히는 주석이 유용하다.

assertTrue(a.compareTo(a) == 0) // a == a
assertTrue(a.compareTo(b) != 0) // a != b
assertTrue(ab.compareTo(ab) == 0) // ab == ab



###### 결과를 경고하는 주석

```java
// 여유 시간이 충분하지 않다면 실행하지 마십시오.
public void _testWithReallyBigFile() {
  ...
}
```

```java
public static SimpleDateFormat makeStandardHttpDateFormat() {
  // SimpleDateFormat은 스레드에 안전하지 못하다.
  // 따라서 각 인스턴스를 독립적으로 생성해야 한다.
  SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
  dt.setTimeZone(TimeZone.getTimeZone("GMT"));
  return df;
}
```



###### TODO 주석

'앞으로 할 일'을 // TODO 주석으로 남겨두면 편하다.

```java
// tODO-MdM 현재 필요하지 않다.
// 체크아웃 모델을 도입하면 함수가 필요 없다.
protected VersionInfo makeVersion() throws Exception {
  return null;
}
```

TODO 주석은 프로그래머가 필요하다 여기지만 당장 구현하기 어려운 업무를 기술한다.



###### 중요성을 강조하는 주석

```java
String listItemCount = match.group(3).trim();
// 여기서 trim은 정말 중요하다. trim 함수는 문자열에서 시작 공백을 제거한다.
// 문자열에 시작 공백이 있으면 다른 문자열로 인식되기 때문이다.
new ListItemWidget(this, listItemContent, this.level + 1);
return buildList(text.substring(match.end()));
```



###### 공개 API에서 Javadocs

공개 API를 구현한다면 반드시 훌륭한 Javadocs를 작성한다. 하지만 이 장에서 제시하는 나머지 충고도 명심하기 바란다.



##### 나쁜 주석

###### 주절거리는 주석

특별한 이유 없이 의무감으로 주석을 단다면 시간낭비다. 주석을 달기로 결정했다면 충분한 시간을 들여 최고의 주석을 달도록 노력한다.



###### 같은 이야기를 중복하는 주석

예제는 헤더에 달린 주식이 같은 코드 내용을 그대로 중복한다.

```java
// this.closed가 true일 때 반환되는 유틸리티 메서드다.
// 타임아웃에 도달하면 예외를 던진다.
public synchronized void  waitForClose(final long timeoutMillis) throws Exception {
  
}
```

위와 같이 주석을 달아놓는 목적이 무엇일까? 주석이 코드보다 더 많은 정보를 제공하지 못한다.

다음은 톰캣에서 가져온 코드다.

```java
public abstract class ContainerBase implements Container, Lifecycle, Pipeline, MBeanRegistration, Serializable {
  /**
    * 이 컴포넌트의 프로세서 지연값
    */
  protected int backgroundProcessorDelay = -1;
  
  /**
    * 이 컴포넌트를 지원하기 위한 생명주기 이벤트\
    */
  protected LifecycleSupport lifecycle = new LifecycleSupport(this);
  
  /**
    * 컨테이너와 관련된 Loader 구현
    */
  protected Loader loader = null;
}
```



###### 오해할 여지가 있는 주석

의도는 좋았으나 프로그래머가 딱 맞을 정도로 엄밀하게는 주석을 달지 못하기도 한다.



###### 의무감으로 다는 주석

모든 함수에 Javadocs를 달거나 모든 변수에 주석을 달아야 한다는 규칙은 어리석기 그지없다.

```java
/**
  *
  * @param title CD 제목
  * @param author CD 저자
  * @param tracks CD 트랙 숫자
  * @param durationInMinutes CD 길이(단위: 분)
  */
public void addCD(String title, String author, int tracks, int durationInMinutes) {
  CD cd = new CD();
  cd.title = title;
  cd.author = author;
  cd.tracks = tracks;
  cd.duration = durationInMinutes;
  cdList.add(cd);
}
```



###### 이력을 기록하는 주석

모듈 첫 머리 주석은 지금까지 모듈에 가한 변경을 모두 기록하는 일종의 일지 혹은 로그가 된다.

```java
/*
  * 변경 이력 (11-Oct-2001부터)
  * ------------------------------
  * 11-Oct-2001: 클래스를 다시 정리하고 새로운 패키지인 com.jrefinery.date로 옮겼다(DG);
  * 05-Nov-2001: getDescription() 메서드를 추가했으며 NotableDate class를 제거했다. (DG);
  * 12-Nov-2001: ...
  * ...
  */
 
```



###### 있으나 마나 한 주석

때로는 있으나 마나 한 주석을 접한다.

```java
/**
  * 기본 생성자
  */
protected AnnualDateRule() {
  
}

/** 월 중 일자 */
private int dayOfMonth;

/**
  * 월 중 일자를 반환한다.
  *
  * @return 월 중 일자
  */
public int getDayOfMonth() {
  return dayOfMonth();
}
```



###### 무서운 잡음

때로는 Javadocs도 잡음이다. 다음은 잘 알려진 오픈 소스 라이브러리에서 가져온 코드다.

```java
/** The name */
private String name;

/** The version */
private String version;

/** The licenceName */
private String licenceName;

/** The version */
private String info;
```

잘라서 붙여넣기 오류가 보인다.



###### 함수나 변수로 표현할 수 있다면 주석을 달지 마라.

```java
// 전역 목록 <smodule>에 속하는 모듈이 우리가 속한 하위 시스템에 의존하는가?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))
```

이 코드에서 주석을 없애고 다시 표현하면

```java
ArrayList moduleDependees = smodule.getDependSubsystems();
String ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem))
```



###### 위치를 표시하는 주석

때때로 프로그래머는 소스 파일에서 특정 위치를 표시하려 주석을 사용한다.

```java
// Actions /////////////////////////////
```

위와 같은 배너 아래 특정 기능을 모아놓으면 유용한 경우가 있긴 하다. 하지만 일반적으로 가독성만 낮추므로 제거해야 마땅하다.
너무 자주 사용하지 않는다면 눈에 띄며 주의를 환기한다. 그러므로 반드시 필요할 때만, 아주 드물게 사용하는 편이 좋다.



###### 닫는 괄호에 다는 주석

때로는 프로그래머들이 닫는 괄호에 특수한 주석을 달아놓는다.
괄호를 닫는 주석을 달아야겠다는 생각이 든다면 대신에 함수를 줄이려 시도하자.

```java
public static void main(String [] args) {
  ...
    try {
          while ((line == in.readLine()) != null) {
      	...
    	} // while
    } // try
}
```



###### 공로를 돌리거나 저자를 표시하는 주석

```java
/* 릭이 추가함 */
```

위와 같은 정보를 소스 코드 관리 시스템에 저장하는 편이 좋다.



###### 주석으로 처리한 코드

주석으로 처리한 코드만큼 밉살스러운 관행도 드물다. 다음과 같은 코드는 작성하지 마라!

```java
InputStreamResponse response = new InputStreamResponse();
response.setBody(formatter.getResultStream(), formatter.getByteCount());
// InputStream resultsStream = formatter.getResultStream();
// StreamReader reader = new StreamReader(resultsStream);
// response.setContent(reader.read(formatter.getByteCount()));
```



###### HTML 주석

소스 코드에서 HTML 주석은 혐오 그 자체다. HTML 주석은 편집기/IDE에서조차 읽기 어렵다.

```java
/**
  * 적합성 테스트를 수행하기 위한 과업
  * 이 과업은 적합성 테스트를 수행해 결과를 출력한다.
  * <p />
  * <pre>
  * 용법:
  * &lt;taskdef name=&quot;execute-fitnesse-tests&quot;
  *     classname=&quot;fitnesse.ant.ExecuteFitnesseTestsTask&quot;
  * </pre>
  */
```



###### 전역 정보

주석을 달아야 한다면 근처에 있는 코드만 기술하라. 코드 일부에 주석을 달면서 시스템의 전반적인 정보를 기술하지 마라.

```java
/**
  * 적합성 테스트가 동작하는 포트: 기본값은 <b>8082</b>
  *
  * @param fitnessePort
  */
public void setFitnessePort(int fitnessePort) {
  this.fitnessePort =. fitnessePort;
}
```



###### 너무 많은 정보

주석에다 흥미로운 역사나 관련 없는 정보를 장황하게 늘어놓지 마라.

```java
/*
  RFC 2045.- Multipurpose Internet Mail Extensions (MIME)
  1부: 인터넷 메시지 본체 형식
  6.8절. Base64 내용 전송 인코딩
  인코딩 과정은 입력 비트 중 24비트그룹을 인코딩된 4글자로 구성된
  출력 문자열로 표현한다. 왼쪽에서 오른쪽으로 진행해가며, 3개를 묶어 8비트 입력
  그룹을 형성한다. ..
  ...
  */
```



###### 모호한 관계

주석과 주석이 설명하는 코드는 둘 사이 관계가 명확해야 한다.

```java
/*
  * 모든 픽셀을 담을 만큼 충분한 배열로 시작한다(여기에 필터 바이트를 더한다).
  * 그리고 헤더 정보를 위해 200바이트를 더한다.
  */
this.pngBytes = new byte[((thiswidth + 1) * this.height * 3) + 200];
```

여기서 필터 바이트란 무엇인가? +1과 관련이 있을까? 아니면 *3과 관련이 있을까? 한 필셀이 한 바이트인가? 200을 추가하는 이유는?



###### 함수 헤더

짧은 함수는 긴 설명이 필요 없다. 짧고 한 가지만 수행하며 이름을 잘 붙인 함수가 주석으로 헤더를 추가한 함수보다 훨씬 좋다.



 ###### 비공개 코드에서 javadocs

공개하지 않을 코드라면 Javadocs는 쓸모가 없다.















