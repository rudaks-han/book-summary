package _04_factory;

public class Main {

    public static void main(String[] args) {
        Factory factory = new IDCardFactory();
        Product card1 = factory.create("hong");
        Product card2 = factory.create("kim");
        Product card3 = factory.create("lee");
        card1.use();
        card2.use();
        card3.use();
    }
}
