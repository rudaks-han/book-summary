package exam.q3_2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Example {

    public static <T> void forEach(List<T> list, Consumer<T> consumer) {
        for (T t: list) {
            consumer.accept(t);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        forEach(list, (Integer i) -> System.out.println(i));
    }
}
