package exam.q5_4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("Hello", "World");

        List<String> results = words.stream()
            .map(word -> word.split(""))
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());

        System.out.println(results);
    }
}
