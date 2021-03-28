package section09.stackwithlinkedlist;

public class Stack<T> {

    private Node<T> head;

    private int count;

    public void push(T data) {
        if (this.head == null) {
            this.head = new Node<>(data);
        } else {
            Node<T> node = new Node<>(data);
            node.setNextNode(head);
            head = node;
        }

        this.count++;
    }

    public T pop() {
        if (this.head == null) {
            return null;
        }

        T item = head.getData();
        head = head.getNextNode();

        count--;

        return item;
    }
}
