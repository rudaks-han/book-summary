package rudaks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Test {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1, 2);
        Optional<Integer> max = numbers.stream().reduce(Integer::max);

    }
}

