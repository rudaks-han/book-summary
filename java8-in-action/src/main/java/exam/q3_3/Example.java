package exam.q3_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Example {

    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        List<R> results = new ArrayList<>();

        for (T t: list) {
            results.add(function.apply(t));
        }

        return results;
    }

    public static void main(String[] args) {
        List<Integer> list = map(
            Arrays.asList("Java", "In", "Action"),
            (String s) -> s.length()
        );

        list.forEach(System.out::println);
    }
}
