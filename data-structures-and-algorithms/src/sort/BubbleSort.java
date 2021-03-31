package sort;

public class BubbleSort {

    public static void main(String[] args) {
        int[] array = new int[]{5, 3, 1, 4, 2};

        sort(array);
    }

    public static int[] sort(int[] array) {

        for (int i=0; i<array.length; i++) {
            for (int k=0; k<array.length-1-i; k++) {
                if (array[k] > array[k+1]) {
                    swap(array, k, k+1);
                }
            }
        }

        return array;
    }

    private static void swap(int[] array, int i, int k) {
        int temp = array[i];
        array[i] = array[k];
        array[k] = temp;
    }
}
