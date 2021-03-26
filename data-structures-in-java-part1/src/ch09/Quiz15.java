package ch09;

public class Quiz15 {

    public static void solve(int[] array) {
        for (int i=0; i<array.length; ++i) {
            if (array[Math.abs(array[i])] > 0) {
                array[Math.abs(array[i])] = -array[Math.abs(array[i])];
            } else {
                System.out.println(Math.abs(array[i]) + "is a repetition");
            }
        }
    }

    public static void main(String[] args) {
        int [] nums = new int[]{2,3,1,2,4,3};

        solve(nums);
    }
}
