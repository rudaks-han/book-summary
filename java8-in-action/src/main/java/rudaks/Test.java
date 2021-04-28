package rudaks;

public class Test {

    public int count;

    public static void main(String[] args) {
        Test t = new Test();
        t.count = 0;

        System.out.println(t.count++);
        System.out.println(++t.count);
    }
}

