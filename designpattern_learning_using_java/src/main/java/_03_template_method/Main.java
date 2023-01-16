package _03_template_method;

public class Main {

    public static void main(String[] args) {
        // 'H'를 가진 CharDisplay 인스턴스를 하나 만든다.
        AbstractDisplay d1 = new CharDisplay('H');

        // "Hello world."를 가진 StringDisplay 인스턴스를 하나 만든다.
        AbstractDisplay d2 = new StringDisplay("Hello, world.");

        d1.display();
        d2.display();
    }
}
