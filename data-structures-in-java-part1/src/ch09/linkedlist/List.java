package ch09.linkedlist;

public interface List<T> {

    void insert(T data);
    void remove(T data);
    void traverse();
    int size();
}
