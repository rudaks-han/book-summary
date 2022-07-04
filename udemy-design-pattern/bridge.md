> 이 내용은 udemy의 design pattern 강의를 정리한 것입니다.
>
> https://www.udemy.com/course/design-patterns-in-java-concepts-hands-on-projects/



# 브릿지 패턴

## 브릿지는 무엇인가?

* 구현부와 추상화 부분은 일반적으로 서로 상속형태로 커플링 되어 있다.
* 브릿지 패턴을 사용하여 서로 영향을 주지 않고 변경할 수 있도록 디커플링 할 수 있다.
* 두개의 개별 상속 구조를 만들고 할 수 있다. (구현부와 추상화 각각)
* 이러한 두 개의 구조를 연결하기 위해 컴포지션을 사용한다.




## UML





## 브릿지 구현방법

* 클라이언트가 필요로 하는 구현부를 정의하면서 시작
    * 일반적인 기본 동작을 결정하고 추상부에 정의한다.
    * 보다 더 추상화할 수도 있고 더 구체적인 동작을 정의할 수도 있다.
    * 그리고 나서 구현부를 정의한다. 구현 메소드는 추상부와 일치해야 하는 것은 아니다. 하지만 추상부는 구현 메소드를 사용하여 동작을 할 수 있다.
    * 그리고 하나 이상의 구체적인 구현부를 작성한다.

* 추상부는 메소드를 실제 사용하는 구현부의 인스턴스를 구성함으로서 만들어진다.



## 브릿지 구현

추상부, First In First Out 컬렉션을 나타낸다.
```java
public interface FifoCollection<T> {

    void offer(T element);

    T poll();
}
```

구현부. 구현부는 추상 구조와 전혀 관련이 없는 자신만의 구조를 정의하고 있다.

```java
public interface LinkedList<T> {

    void addFirst(T element);

    T removeFirst();

    void addLast(T element);

    T removeLast();

    int getSize();
}

```

FifoCollection을 구현한 Queue

```java
// refined abstraction
public class Queue<T> implements FifoCollection<T> {

    private LinkedList<T> list;

    public Queue(LinkedList<T> list) {
        this.list = list;
    }

    @Override
    public void offer(T element) {
        this.list.addLast(element);
    }

    @Override
    public T poll() {
        return this.list.removeFirst();
    }
}
```

구현부
node를 사용하는 전통적인 LinkedList이다.
thread-safe하지 않다.

```java
public class SinglyLinkedList<T> implements LinkedList<T> {

    private class Node {
        private Object data;
        private Node next;
        private Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private int size;
    private Node top;
    private Node last;

    @Override
    public void addFirst(T element) {
        if (this.top == null) {
            this.last = this.top = new Node(element, null);
        } else {
            this.top = new Node(element, this.top);
        }
    }

    @Override
    public T removeFirst() {
        if (this.top == null) {
            return null;
        }
        T temp = (T) this.top.data;
        if (this.top.next != null) {
            this.top = this.top.next;
        } else {
            this.top = null;
            this.last = null;
        }
        size--;
        return temp;
    }

    @Override
    public void addLast(T element) {
        //
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
```

이를 사용하는 Client 코드

```java
public class Client {

    public static void main(String[] args) {
        FifoCollection<Integer> collection = new Queue<>(new SinglyLinkedList<>());
        collection.offer(10);
        collection.offer(40);
        collection.offer(99);

        System.out.println(collection.poll());
        System.out.println(collection.poll());
        System.out.println(collection.poll());
        //
        System.out.println(collection.poll());
    }
}
```





## 구현 시 고려사항

* 하나의 구현부를  만드는 경우에 abstract 구현부를 만들지 않아도 된다.
* 추상화는 생성자에서 사용할 구체적인 구현재에 따라 달라지고 다른 클래스에서 결정하도록 위임할 수도 있다. 이전 접근방법에서 추상화는 구체적인 구현부를 모르고 보다 디커플링된 상태를 제공한다.



## 디자인 고려사항

* 브릿지는 추상부와 구현부를 독립적으로 변경할 수 있도록 확장성을 제공해준다. 시스템을 모듈화하는데 독립적으로 build, package를 할 수 있다.
* 원하는 구현을 하기 위해 추상 객체를 만드는데 추상 팩토리를 사용함으로써 추상부와 구현부를 디커플링 할 수 있다.



## 브릿지 예제

* 브릿지패턴의 흔한 예제가 JDBC API이다. 좀 더 명확히 말하자면 java.sql.Driver인터페이스의 java.sql.DriverManager가 브릿지 패턴을 나타낸다.
* 또 다른 예제는 Collections.newSetFromMap() 메소드이다. 이 메소드는 map 객체에서 Set을 리턴한다.

```java
private static class SetFromMap<E> extends AbstractSet<E>
        implements Set<E>, Serializable
    {
        private final Map<E, Boolean> m;  // The backing map
        private transient Set<E> s;       // Its keySet

        SetFromMap(Map<E, Boolean> map) {
            if (!map.isEmpty())
                throw new IllegalArgumentException("Map is non-empty");
            m = map;
            s = map.keySet();
        }

        public void clear()               {        m.clear(); }
        public int size()                 { return m.size(); }
        public boolean isEmpty()          { return m.isEmpty(); }
        public boolean contains(Object o) { return m.containsKey(o); }
        public boolean remove(Object o)   { return m.remove(o) != null; }
        public boolean add(E e) { return m.put(e, Boolean.TRUE) == null; }
        public Iterator<E> iterator()     { return s.iterator(); }
        public Object[] toArray()         { return s.toArray(); }
        public <T> T[] toArray(T[] a)     { return s.toArray(a); }
        public String toString()          { return s.toString(); }
        public int hashCode()             { return s.hashCode(); }
        public boolean equals(Object o)   { return o == this || s.equals(o); }
        public boolean containsAll(Collection<?> c) {return s.containsAll(c);}
        public boolean removeAll(Collection<?> c)   {return s.removeAll(c);}
        public boolean retainAll(Collection<?> c)   {return s.retainAll(c);}
}
```



## 어댑터와 비교

| 브릿지                                                  | 어댑터                                                       |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| 추상부와 구현부를 독립적으로 구성하는데 그 의도가 있다. | 어댑터는 관련없는 클래스를 협업하도록 하는데 목적이 있다.    |
| 브릿지는 구현부와 추상부를 다르게 구성할 수 있다.       | 어댑터는 일반적으로 레거시 시스템이 새 코드로 통합되어야 하는 곳에서 사용할 수 있다. |



## 위험요소

* 브릿지 디자인 패턴을 이해하고 구현하기가 꽤 복잡하다.
* 브릿지 디자인 패턴을 사용하기 전에 많은 사고와 이해하기 쉽도록 설계해야 한다.
* 충분한 설계과정이 있어야 한다. 레거시 코드에 브릿지를 추가하는 것은 어렵다. 심지어 진행중인 프로젝트에 브릿지를 추가하는 일은 많은 양의 재작업이 필요할지도 모른다.



## 빠른 요약

* 추상부와 구현부를 디커플릿할 경우에 브릿지 디자인 패턴을 사용한다.
* 브릿지 패턴은 추상부와 구현부의 개별 상속구조를 정의하고, 그리고 이 두가지를 컴포지션을 통해 이 두개를 연결한다.
* 구현부는 추상부의 메소드와 일치하는 메소드를 정의할 필요는 없다.







