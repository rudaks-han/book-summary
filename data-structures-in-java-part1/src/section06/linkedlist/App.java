package section06.linkedlist;

public class App {

    public static void main(String[] args) {
        LinkedList<String> names = new LinkedList<>();

        names.insert("김지훈");
        names.insert("한경만");
        names.insert("박은규");

        names.traverse();
        names.remove("한경만");
        names.traverse();
    }
}
