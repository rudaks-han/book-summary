package exam.q5_5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> results = numbers.stream().map(number -> number * number).collect(Collectors.toList());
        System.out.println(results);
    }
}
