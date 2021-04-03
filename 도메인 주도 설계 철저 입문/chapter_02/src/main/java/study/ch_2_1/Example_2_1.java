package study.ch_2_1;

public class Example_2_1 {

    public void ex_2_1() {
        String fullName = "naruse mananobu";
        System.out.println(fullName); // naruse mananobu라는 값을 출력
    }

    public void ex_2_2() {
        String fullName = "naruse mananobu";
        String[] tokens = fullName.split(" "); // ["naruse", "mananobu"]와 같은 배열이 만들어짐
        String lastName = tokens[0];
        System.out.println(lastName); // naruse가 출력됨
    }

    public void ex_2_3() {
        String fullName = "john smith";
        String[] tokens = fullName.split(" "); // ["john", "smith"]와 같은 배열이 만들어짐
        String lastName = tokens[0];
        System.out.println(lastName); // john이 출력됨
    }

    public static void main(String[] args) {
    }
}
