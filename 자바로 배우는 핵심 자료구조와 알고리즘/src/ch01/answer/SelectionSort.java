package ch01.answer;

public class SelectionSort {

    // i와 j의 위치에 있는 값을 바꾼다.
    public static void swapElements(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // start로 부터 시작하는 최소값의 위치를 찾고 배열의 마지막 위치로 갑니다.
    public static int indexLowest(int[] array, int start) {
        int lowIndex = start;
        for (int i = start; i<array.length; i++) {
            if (array[i] < array[lowIndex]) {
                lowIndex = i;
            }
        }

        return lowIndex;
    }

    // 선택 정렬을 사용하여 요소를 정렬한다.
    public static void selectSort(int[] array) {
        for (int i=0; i<array.length; i++) {
            int j = indexLowest(array, i);
            swapElements(array, i, j);
        }
    }

    public static void main(String[] args) {
        int[] samples = new int[]{8,3,1,5,7,4,6,2,9};
        selectSort(samples);

        for (int i=0; i<samples.length; i++) {
            System.out.println(samples[i]);
        }
    }
}
