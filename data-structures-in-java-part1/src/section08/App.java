package section08;

public class App {

    public static void main(String[] args) {
        LinkedList<String> names = new LinkedList<>();

        names.insert("1");
        names.insert("2");
        names.insert("3");
        names.insert("4");
        names.insert("5");
        names.insert("6");
        names.insert("7");
        names.insert("8");
        names.insert("9");

        System.out.println(names.getMiddleNode().toString());
        names.reverse();
        names.traverse();
    }
}
