package rudaks.ch03.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class FunctionExample {

    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> results = new ArrayList<>();

        for (T t: list) {
            results.add(f.apply(t));
        }

        return results;
    }

    public static void main(String[] args) {
        List<Integer> l = map(Arrays.asList("lamdas", "in", "action"),
                (String s) -> s.length());

        System.out.println(l);
    }
}
