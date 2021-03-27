package section06.linkedlist;

public class Node<T extends Comparable> {

    private T data;

    private Node<T> nextNode;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setNextNode(Node<T> node) {
        this.nextNode = node;
    }

    public Node<T> getNextNode() {
        return this.nextNode;
    }

    @Override
    public String toString() {
        return data + "";
    }
}
