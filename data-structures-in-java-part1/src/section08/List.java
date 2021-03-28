package section08;

public interface List<T extends Comparable>{

    void reverse();

    Node<T> getMiddleNode();

    void insert(T data);

    void remove(T data);

    void traverse();

    int size();
}
