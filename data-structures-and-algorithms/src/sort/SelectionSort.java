package sort;

public class SelectionSort {

    public static void main(String[] args) {
        int[] array = new int[]{5, 3, 1, 4, 2};

        sort(array);
    }

    public static int[] sort(int[] array) {

        for (int i=array.length-1; i>0; i--) {
            int largestIndex = 0;
            for (int k=1; k<i; k++) {
                if (array[k] > array[largestIndex]) {
                    largestIndex = k;
                }
            }

            swap(array, largestIndex, i);
        }

        return array;
    }

    private static void swap(int[] array, int i, int k) {
        int temp = array[i];
        array[i] = array[k];
        array[k] = temp;
    }
}
