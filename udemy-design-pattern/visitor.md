> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 비지터 패턴

## 비지터는 무엇인가?

* 비지터 패턴은 기존 클래스를 수정하지 않고 새로운 동작으로 정의할 수 있다.
* 객체 구조에서 모든 노드를 접근하는 객체(비지터)처럼 이 패턴을 생각해라.
* 비지터가 객체 구조에서 특정 객체를 접근할 때마다 인수를 넘겨서 비지터의 특정 메소드를 호출한다.
* 새로운 동작이 필요할때마다 클래스의 동작으로 구현하고 객체 구조를 접근하는 비지터의 서브클래스를 만든다. 
* 객체 그 자체는 비지터가 인수를 넘기를 "accept" 비지터를 구현한다. 객체는 특별히 생성된 비지터의 메소드에 대해 알고 있고 accept 메소드의 메소드를 호출한다.




## UML





## 비지터 구현방법

* 만들려는 각 클래스에 "visit" 메소드를 정의하는 비지터 인터페이스를 만든다.
* 비지터에섯 제공된 기능을 만드는 클래스들은 비지터를 accept하는 "accept" 메소드를 정의한다. 이 메소드들은 파라미터 타입으로 비지터를 사용하여 정의되고 비지터를 구현하는 어떤 클래스를 이 메소드에 넘길 수 있다.
* Accept 메소드 구현에서 클래스에 정의된 비지터에서 메소드를 호출할 것이다.
* 다음으로, 한개 이상의 클래스에섯 비지터 인터페이스를 구현한다. 각 구현은 클래스의 특정 기능을 제공한다. 다른 기능을 만들고 싶다면 비지터의 새 구현을 생성한다.




## 비지터 구현

```java
public interface Employee {

    int getPerformanceRating();

    void setPerformanceRating(int rating);

    Collection<Employee> getDirectReport();

    int getEmployeeId();

    void accept(Visitor visitor);
}
```

```java
@Getter
public abstract class AbstractEmployee implements Employee {

    private int performanceRating;

    private String name;

    private static int employeeIdCounter = 101;

    private int employeeId;

    public AbstractEmployee(String name) {
        this.name = name;
        employeeId = employeeIdCounter++;
    }

    @Override
    public void setPerformanceRating(int rating) {
        this.performanceRating = rating;
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return Collections.emptyList();
    }
}
```

```java
@Getter
public class Programmer extends AbstractEmployee {

    private String skill;

    public Programmer(String name, String skill) {
        super(name);
        this.skill = skill;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

```java
@Getter
public class ProjectLead extends AbstractEmployee {

    private List<Employee> directReports = new ArrayList<>();

    public ProjectLead(String name, Employee...employees) {
        super(name);
        Arrays.stream(employees).forEach(directReports::add);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return directReports;
    }
}
```

```java
@Getter
public class Manager extends AbstractEmployee {

    private List<Employee> directReports = new ArrayList<>();

    public Manager(String name, Employee...employees) {
        super(name);
        Arrays.stream(employees).forEach(directReports::add);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return directReports;
    }
}
```

```java
@Getter
public class VicePresident extends AbstractEmployee {

    private List<Employee> directReports = new ArrayList<>();

    public VicePresident(String name, Employee...employees) {
        super(name);
        Arrays.stream(employees).forEach(directReports::add);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Collection<Employee> getDirectReport() {
        return directReports;
    }
}
```

```java
public class PrinterVisitor implements Visitor {
    @Override
    public void visit(Programmer programmer) {
        String msg = programmer.getName() + " is a " + programmer.getSkill() + " programmer.";
        System.out.println(msg);
    }

    @Override
    public void visit(ProjectLead lead) {
        String msg = lead.getName() + " is a Project Lead with " + lead.getDirectReports().size() + " programmers reporting.";
        System.out.println(msg);
    }

    @Override
    public void visit(Manager manager) {
        String msg = manager.getName() + " is a Manager with " + manager.getDirectReport().size() + " leads reporting.";
        System.out.println(msg);
    }

    @Override
    public void visit(VicePresident vicePresident) {
        String msg = vicePresident.getName() + " is a Vice President with " + vicePresident.getDirectReports().size() + " managers reporting.";
        System.out.println(msg);
    }
}
```

클라이언트 사용

```java
public class Client {

    public static void main(String[] args) {
        Employee employee = buildOrganization();
        Visitor visitor = new PrinterVisitor();
        visitOrgStructure(employee, visitor);
    }

    private static Employee buildOrganization() {
        Programmer p1 = new Programmer("Rachel", "C++");
        Programmer p2 = new Programmer("Andy", "java");

        Programmer p3 = new Programmer("Josh", "C3");
        Programmer p4 = new Programmer("Bill", "C++");

        ProjectLead pl1 = new ProjectLead("Tina", p1, p2);
        ProjectLead pl2 = new ProjectLead("Joey", p3, p4);

        Manager m1 = new Manager("Chad", pl1);
        Manager m2 = new Manager("Chad II", pl2);

        VicePresident vp = new VicePresident("Richard", m1, m2);

        return vp;
    }

    private static void visitOrgStructure(Employee employee, Visitor visitor) {
        employee.accept(visitor);
        employee.getDirectReport().stream().forEach(e -> visitOrgStructure(e, visitor));
    }
}
```





## 구현 시 고려사항

* 비지터는 부모를 가지지 않는 객체와 동작한다. 그래서 그런 클래스의 인터페이스를 가지는 것은 선택사항이다. 하지만 이 객체로 비지터를 넘기는 코드는 각 클래스를 알고 있어야 한다.
* 보통 비지터는 작업을 수행하기 위해 객체의 내부상태에 접근해야할 필요가 있다. 그래서 getter/setter를 사용해서 상태를 노출해야 한다.



## 디자인 고려사항

* 이 패턴의 효과는 관련된 기능이 여러 클래스로 나누는 것 대신 하나의 비지터 클래스로 합친다는 것이다. 그래서 새 기능을 추가하는 것이 새 비지터 클래스에 추가하는 것 만큼 단순하다.
* 비지터는 상태를 모을수 있다. 동작과 함께 비지터에 각 객체당 상태를 가질 수 있다. 비지터에 정의된 동작에 대한 객체들에 새 상태를 추가할 필요가 없다.
* 비지터는 컴포지트를 사용해서 구현된 개겣 구조에 새 기능을 추가하는데 사용될 수 있거나 인터프리터 디자인 패턴에 인터프리터 작업을 하는데 사용될 수 있다.



## 비지터 예제

* XML 파싱하는데 사용되는 dom4j 라이브러리는 org.dom4j.Visitor와 org.dom4j.VisitorSupport 인터페이스를 가지고 있다. 이것은 비지터의 예이다. 이 비지터를 구현함으로써 XML 트리의 각 노드를 처리할 수 있다.

* 비지터 패턴의 다른 예제는 java.nio.file.FileVisitor와 SimpleFileVisitor이다.

    ```java
    class DeleteVisitor extends SimpleFileVisitor<Path> {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
      
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        if (e == null) {
          Files.delete(dir);
          return FileVisitResult.CONTINUE;
        } else {
          // directory iteration failed
          throw e;
        }
      }
    }
    ```

    

## 전략패턴과 비교

| 비지터                                                | 전략패턴                                                     |
| ----------------------------------------------------- | ------------------------------------------------------------ |
| 모든 비지터의 서브클래스는 각기 다른 기능을 제공한다. | 전략 패턴에서 각 서브클래스는 동일한 문제를 해결하기 위해 각각의 알고리즘을 나타낸다. |



## 위험요소

* 보통 비지터들은 객체의 상태에 접근할 필요가 있다. 그래서 getter 메소드로 많은 상태를 노출하고 캡슐화를 약화시킬 수 있다.
* 비지터에서 새 클래스를 만드는 것은 모든 비지터 구현에 변경을 가져온다.
* 클래스가 변경이 된다면 변경된 클래스와 동작해야하기 때문에 모든 비지터는 변경되어야 한다.
* 이해하고 구현하는 데 좀 복잡하다.



## 빠른 요약

* 비지터 패턴은 객체를 정의하는 클래스를 수정하지 않고 새 기능을 추가할 수 있다.
* 비지터는 새 기능을 제공하기 위해 클래스의 객체와 동작하는 특정 메소드를 정의한다.
* 이 패턴 사용하기 위해 클래스는 비지터와 이 메소드, 특정 클래스에 정의된 비지터의 객체 클래스 메소드 내에 레퍼런스를 가지는 accept 메소드를 정의한다.
* 새 기능을 추가하는 것은 필요한 기능의 각 클래스를 수정하지 않고 클래스 내에 새로운 비지터를 만들고 새 기능을 구현하는 것을 의미한다.
* 이 패턴은 객체 구조가 있고 다른 클래스 혹은 비지터가 이 구조를 비지터 객체를 넘김으로써 순환되는 곳에서 종종 사용된다.







