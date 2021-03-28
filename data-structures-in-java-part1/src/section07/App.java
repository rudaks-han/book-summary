package section07;

public class App {
    public static void main(String[] args) {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        list.insert("1");
        list.insert("2");
        list.insert("3");

        list.traverseForward();
        list.traverseBackward();


    }
}
