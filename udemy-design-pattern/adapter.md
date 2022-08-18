> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 어댑터 패턴

## 어댑터는 무엇인가?

* 우리는 클라이언트가 필요로 하는 기능을 제공하는 객체를 가지고 있다. 하지만 클라이언트 코드는 다른 인터페이스 객체를 사용하기 때문에 이 객체를 사용할 수 없다.
* 어댑터 디자인 패턴을 사용하여 우리는 그 객체를 클라이언트 인터페이스에 적용함으로서 기존 객체가 동작하게 할 수 있다.
* 이 패턴은 또한 기존 객체를 wrapping하기 때문에 래퍼(wrapper)라고 불린다.




## UML





## 어댑터 구현방법

* 어댑터에 대한 클래스를 만들면서 시작한다.
    * 어댑터는 클라이언트가 기대하는 인터페이스를 구현해야 한다.
    * 우선 기존 클래스에서 확장을 통해서 클래스 어댑터를 만들것이다.
    * 클래스 어댑터 구현에서는, 우리는 단순히 메소드를 어댑티(adaptee)로부터 상속받은 다른 메소드드로 향하게 할것이다.
    * 다음으로 객체 어댑터에서는, 타겟 인터페이스를 구현할 것이고 어댑터에서 어댑티를 생성자 인수로 받을 것이다.

* 객체 어댑터는 어댑티를 생성자 인수로 가져야 하거나 덜 사용하는 방법으로 생성자에서 특정 어댑티로 초기화할 수도 있다.



## 어댑터 구현

### 클래스 어댑터

시스템에서 사용되는 기존 클래스 Employee (Adaptee)

```java
@Getter
@Setter
public class Employee {

    private String fullName;

    private String jobTitle;

    private String officeLocation;
}
```

 새 클라이언트 코드에서 필요로 하는 타겟 인터페이스

```java
public interface Customer {

    String getName();

    String getDesignation();

    String getAddress();
}
```

 클래스 어댑터, 양방향 어댑터로 동작한다.

```java
public class EmployeeClassAdapter extends Employee implements Customer {
    @Override
    public String getName() {
        return this.getFullName();
    }

    @Override
    public String getDesignation() {
        return this.getJobTitle();
    }

    @Override
    public String getAddress() {
        return this.getOfficeLocation();
    }
}
```

Customer 인터페이스를 필요로 하는 클라이언트 코드

```java
public class BusinessCardDesigner {

    public String designCard(Customer customer) {
        String card = "";
        card += customer.getName();
        card += "\n" + customer.getDesignation();
        card += "\n" + customer.getAddress();
        return card;
    }
}
```

Main 코드

```java
public class Main {

    public static void main(String[] args) {
        // Class/Two-way adapter 사용하기
        EmployeeClassAdapter adapter = new EmployeeClassAdapter();
        populateEmployeeData(adapter);
        BusinessCardDesigner designer = new BusinessCardDesigner();
        String card = designer.designCard(adapter);
        System.out.println(card);
    }

    private static void populateEmployeeData(Employee employee) {
        employee.setFullName("Elliot Alderson");
        employee.setJobTitle("Security Engineer");
        employee.setOfficeLocation("Allsafe Cybersecurity, New York City, New York");
    }
}
```



### 객체 어댑터

시스템에서 사용된 기존 클래스 (Adaptee)

```java
@Getter
@Setter
public class Employee {

    private String fullName;

    private String jobTitle;

    private String officeLocation;
}
```

새 클라이언트 코드에서 필요로 하는 타겟 인터페이스

```java
public interface Customer {

    String getName();

    String getDesignation();

    String getAddress();
}
```

```java
public class EmployeeObjectAdapter implements Customer {

    private Employee adaptee;

    public EmployeeObjectAdapter(Employee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String getName() {
        return adaptee.getFullName();
    }

    @Override
    public String getDesignation() {
        return adaptee.getJobTitle();
    }

    @Override
    public String getAddress() {
        return adaptee.getOfficeLocation();
    }
}
```

```java
public class Main {

    public static void main(String[] args) {
        // Object Adapter 사용하기
        Employee employee = new Employee();
        populateEmployeeData(employee);
        EmployeeObjectAdapter objectAdapter = new EmployeeObjectAdapter(employee);
        card = designer.designCard(objectAdapter);
        System.out.println(card);
    }

    private static void populateEmployeeData(Employee employee) {
        employee.setFullName("Elliot Alderson");
        employee.setJobTitle("Security Engineer");
        employee.setOfficeLocation("Allsafe Cybersecurity, New York City, New York");
    }
```



## 구현 시 고려사항

* 어댑터는 타겟 인터페이스와 연결되는 객체와 얼마나 많이 다른가에 달려있다. 만일 메소드 인수가 같거나 비슷하다면 해야할 작업은 거의 없다.
* 클래스 어댑터는 사용하는 것은 어댑티 동작을 오버라이드 하도록 한다. 하지만 어댑티와 다르게 동작하는 어댑터로 동작하기 때문에 피해야 하는 일이 된다. 오류를 수정하는 일이 쉬운 일은 아니다.
* 객체 어댑터를 사용하는 것은 잠재적으로는 어댑티 객체를 서브클래스 중 하나로 변경하게 된다.



## 디자인 고려사항

* 자바에서 클래스 어댑터는 타겟 클래스와 어댑티가 구체적인 클래스 타입이라면 불가능할 수도 있다. 그런 경우 객체 어댑터가 유일한 해결책이다. 또한 자바에서 private 상속은 없기 때문에 객체 어댑터가 더 낫다.
* 클래스 어댑터는 또한 양방향 어댑터라고 부른다. 왜냐하면 타켓 인터페이스와 어댑티에 대한 대리인 역할을 하기 때문이다. 그것은 타겟 인터페이스 있고 어댑티 객체가 있는 곳에 어댑터 객체를 사용할 수 있다.



## 어댑터 예제

* java.io.InputStreamReader와 java.io.OutputStreamWriter 클래스가 객체 어댑터의 예이다.

* 이 클래스는 InputStream/OutputtStream 객체를 Reader/Writer 인터페이스에 적용한다.

    ```java
    public class InputStreamReader extends Reader {
    
        private final StreamDecoder sd;
    
        /**
         * Creates an InputStreamReader that uses the default charset.
         *
         * @param  in   An InputStream
         */
        public InputStreamReader(InputStream in) {
            super(in);
            try {
                sd = StreamDecoder.forInputStreamReader(in, this, (String)null); // ## check lock object
            } catch (UnsupportedEncodingException e) {
                // The default encoding should always be available
                throw new Error(e);
            }
        }
      
        public int read(char cbuf[], int offset, int length) throws IOException {
            return sd.read(cbuf, offset, length);
        }
      
        public int read() throws IOException {
            return sd.read();
        }
    ```

    

## 데코레이터와 비교

| 어댑터                                                       | 데코레이터                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 행동의 변화가 없이 단순히 객체를 또다른 인터페이스에 적용한다. | 언터페이스를 변경하지 않고 객체 행동을 강화한다.             |
| 어댑터는 인터페이스를 변경하기 때문에 다른 어댑터를 적용하는 어댑터인 재귀 컴포지션을 사용하기는 쉽지 않다. | 데코레이터는 인터페이스를 변경하지 않기 때문에 재귀 컴포지션을 사용하거나 쉽게 데코레이터를 사용할 수 있다. 데코레이터는 메인 객체와 구분이 되지 않는다. |



## 위험요소

* 타겟 인터페이스와  사용하고 어댑터를 확장하는 어댑티를 사용하여 자바에서 클래스 어댑터를 만들수 있다. 하지만 코드의 일분 부분에 관련없는 메소드를 노출하는 객체를 만들어낸다. 클래스 어댑터 사용을 피해라. 완전한 경우에만 사용해라.
* 단순 인터페이스 번역 작업같이 어댑터에서 많은 작업을 하기 쉽다. 하지만 이러한 것들은 어댑터에서 어댑티 객체와는 다른 행위를 나타내는 것ㅅ일 수 있다.
* 또 다른 위험요소는 없다. 우리는 단순 인터페이스 번역의 목적으로 유지하기만 하면 괜찮다.



## 빠른 요약

* 필요한 기능을 가진 기존 객체가 있지만 클라이언트 코드는 객체 외에 다른 인터페이스가 필요하다.
* 클래스 어댑터는 어댑터가 어댑티 되는 객체 클래스로 부터 상속하고 클라이언트 코드에서 필요한 인터페이스를 구현하는 방식이다. 이 어댑터 유형은 패해야 한다.
* 객체 어댑터는 컴포지션을 사용한다. 타겟 인터페이스를 구현하고 어댑티의 객체 컴포지션을 싸용한다. 이것은 어댑터엣서 어댑티의 서브클래스를 사용할 수 있게 된다.






