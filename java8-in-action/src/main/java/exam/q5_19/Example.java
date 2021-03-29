package exam.q5_19;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) {
        Stream.of("Java 8", "Lamdbas", "In", "Action")
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
