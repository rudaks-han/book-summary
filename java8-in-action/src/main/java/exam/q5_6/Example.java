package exam.q5_6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Example {

    public static void main(String[] args) {
        List<Integer> number1 = Arrays.asList(1,2,3);
        List<Integer> number2 = Arrays.asList(3,4);

        List<int[]> results = number1.stream()
                .flatMap(i -> number2.stream().map(j -> new int[]{i, j}))
                .collect(Collectors.toList());

        results.forEach((int[] result) -> System.out.println("(" + result[0] + "," + result[1] + ")"));
    }
}
