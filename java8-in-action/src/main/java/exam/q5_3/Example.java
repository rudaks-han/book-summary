package exam.q5_3;

import java.util.Arrays;
import java.util.List;

public class Example {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("Java8", "Lamdas", "In", "Action");

        words.stream().map(word -> word.length()).forEach(System.out::println);
    }
}
