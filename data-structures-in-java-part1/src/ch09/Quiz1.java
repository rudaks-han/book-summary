package ch09;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Quiz1 {

    // 1,2,3,4,5 ==> 5,4,3,2,1

    public static void swap(int[] nums, int x, int y) {
        int temp = nums[x];
        nums[x] = nums[y];
        nums[y] = temp;
    }

    public static int [] reverseArray(int [] nums) {
        int startIndex = 0;
        int endIndex = nums.length - 1;

        while (endIndex > startIndex) {
            swap(nums, startIndex, endIndex);
            startIndex++;
            endIndex--;
        }

        return nums;
    }

    public static void main(String[] args) {
        int [] nums = new int[]{1,2,3,4,5};
        int [] results = reverseArray(nums);

        for (int i=0; i<results.length; i++) {
            System.out.println(results[i]);
        }
    }
}
