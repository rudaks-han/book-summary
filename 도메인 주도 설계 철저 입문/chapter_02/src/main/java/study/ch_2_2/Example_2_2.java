package study.ch_2_2;

public class Example_2_2 {
    private static void ex_2_7() {
        String greet = "안녕하세요";
        System.out.println(greet); // 안녕하세요가 출력됨
        greet = "Hello";
        System.out.println(greet); // Hello가 출력됨
    }

    public static void ex_2_11() {
        // 숫자값 수정
        int num = 0;
        num = 1;

        // 문자값 수정
        char c = '0';
        c = 'b';

        // 문자열 수정
        String greet = "안녕하세요";
        greet = "hello";
    }

    public static void ex_2_12() {
        FullName fullName = new FullName("masanobu", "naruse");
        fullName = new FullName("masanobu", "sato");
    }

    public static void ex_2_13() {
        System.out.println(0 == 0); // true
        System.out.println(0 == 1); // false
        System.out.println('a' == 'a'); // true
        System.out.println('a' == 'b'); // false
        System.out.println("hello" == "hello"); // true
        System.out.println("hello" == "안녕하세요"); // false
    }

    public static void ex_2_14() {
        FullName nameA = new FullName("masanobu", "naruse");
        FullName nameB = new FullName("masanobu", "naruse");

        // 두 인스턴스를 비교
        System.out.println(nameA.equals(nameB));
    }

    public static void ex_2_15() {
        FullName nameA = new FullName("masanobu", "naruse");
        FullName nameB = new FullName("john", "smith");

        boolean compareResult = nameA.getFirstName() == nameB.getFirstName() && nameA.getLastName() == nameB.getLastName();
        System.out.println(compareResult);
    }

    public static void ex_2_17() {
        FullName nameA = new FullName("masanobu", "naruse");
        FullName nameB = new FullName("john", "smith");

        boolean compareResult = nameA.equals(nameB);
        System.out.println(compareResult);

        // 연산자들 오버라이드를 활용할 수도 있다.
        boolean compareResult2 = nameA == nameB;
        System.out.println(compareResult2);
    }

    public static void main(String[] args) {
    }
}
