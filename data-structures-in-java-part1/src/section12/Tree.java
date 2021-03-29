package section12;

public interface Tree<T> {
    void insert(T data);

    void remove(T data);

    void traversal();

    T getMax();

    T getMin();
}
