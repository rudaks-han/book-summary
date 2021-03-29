package section10.queue;

public class Queue<T extends Comparable<T>> {

    private Node<T> firstNode;

    private Node<T> lastNode;

    private int count;

    public void enqueue(T data) {
        count++;

        if (firstNode == null) {
            Node<T> newNode = new Node<>(data);
            firstNode = newNode;
            lastNode = firstNode;
        } else {
            Node<T> newNode = new Node<>(data);
            lastNode.setNextNode(newNode);
            lastNode = newNode;
        }
    }

    public T dequeue() {
        count--;

        T data = firstNode.getData();
        firstNode = firstNode.getNextNode();

        if (isEmpty()) {
            this.lastNode = null;
        }

        return data;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int size() {
        return this.count;
    }
}
