package section07;

public class DoublyLinkedList<T extends Comparable> {

    private Node<T> head;

    private Node<T> tail;

    public void insert(T data) {
        Node<T> newNode = new Node(data);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPreviousNode(tail);
            tail.setNextNode(newNode);
            tail = newNode;
        }
    }

    public void traverseForward() {
        Node<T> actualNode = head;

        while (actualNode != null) {
            System.out.println(actualNode);
            actualNode = actualNode.getNextNode();
        }
    }

    public void traverseBackward() {
        Node<T> actualNode = tail;

        while (actualNode != null) {
            System.out.println(actualNode);
            actualNode = actualNode.getPreviousNode();
        }
    }
}
