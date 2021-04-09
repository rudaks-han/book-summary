package rudaks.ch07;

import java.util.function.Function;
import java.util.stream.Stream;

public class Test {


    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i=0; i<10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            if (duration < fastest) fastest = duration;
        }

        return fastest;
    }

    public static void main(String[] args) {

        /*System.out.println("Sequential sum done in : " + measureSumPerf(ParallelStreams::sequentialSum, 10_000_0000) + " msecs");
        System.out.println("Iterative sum done in : " + measureSumPerf(ParallelStreams::iterativeSum, 10_000_0000) + " msecs");
        System.out.println("Parallel sum done in : " + measureSumPerf(ParallelStreams::parallelSum, 10_000_0000) + " msecs");*/

        /*System.out.println("Sequential sum done in : " + measureSumPerf(ParallelStreams::rangedSum, 10_000_0000) + " msecs");
        System.out.println("Iterative sum done in : " + measureSumPerf(ParallelStreams::iterativeSum, 10_000_0000) + " msecs");
        System.out.println("Parallel sum done in : " + measureSumPerf(ParallelStreams::parallelRangedSum, 10_000_0000) + " msecs");*/

        System.out.println("SideEffect parallel sum done in : " + measureSumPerf(ParallelStreams::sideEffectParallelSum, 10_000_0000) + " msecs");
        System.out.println(ParallelStreams.sideEffectParallelSum(50));

    }
}

