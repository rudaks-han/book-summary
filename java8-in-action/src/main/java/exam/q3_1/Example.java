package exam.q3_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Example {
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> results = new ArrayList<>();

        for (T t: list) {
            if (predicate.test(t)) {
                results.add(t);
            }
        }

        return results;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = filter(list, (Integer i) -> i % 2 == 0);

        result.forEach(System.out::println);
    }
}
