package exam.q5_20;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) {
        List<Integer> results = Stream.iterate(0, i -> i + 2)
                .limit(5)
                .collect(Collectors.toList());

        results.stream().forEach(System.out::println);
    }
}
