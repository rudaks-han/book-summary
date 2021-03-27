package ch09.linkedlist;

import java.util.Comparator;

public class Node<T extends Comparator<T>> {
    private T data;

    private Node<T> nextNode;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNextNode() {
        return this.nextNode;
    }

    public void setNextNode(Node<T> node) {
        this.nextNode = node;
    }
}
