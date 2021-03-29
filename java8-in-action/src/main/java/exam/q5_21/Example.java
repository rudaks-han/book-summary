package exam.q5_21;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) {
        List<int[]> results = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .collect(Collectors.toList());

        results.stream()
                .map(result -> result[0])
                .forEach(System.out::println);
    }
}
