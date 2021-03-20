package rudaks.ch03.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateExample {

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for (T s: list) {
            if (p.test(s)) {
                results.add(s);
            }
        }

        return results;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "");

        Predicate<String> notEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> notEmpty = filter(list, notEmptyStringPredicate);

        System.out.println(notEmpty);
    }
}
