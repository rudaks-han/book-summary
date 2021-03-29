package exam.q4_3;

import java.util.Arrays;
import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<String> titles = Arrays.asList("Java8", "In", "Action");

        titles.forEach(System.out::println);
    }
}
