package sort;

import java.util.stream.IntStream;

public class SortTest {

    public static void main(String[] args) {

        int [] arrays = IntStream.range(0, 10000).toArray();

        BubbleSort bubbleSort = new BubbleSort();
        long start = System.currentTimeMillis();
        bubbleSort.sort(arrays);

        long end = System.currentTimeMillis();

        System.out.println("bubble sort: " + (end - start));

        SelectionSort selectionSort = new SelectionSort();
        start = System.currentTimeMillis();
        selectionSort.sort(arrays);

        end = System.currentTimeMillis();

        System.out.println("selection sort: " + (end - start));
    }
}
