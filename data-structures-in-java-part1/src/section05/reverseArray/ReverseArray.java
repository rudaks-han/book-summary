package section05.reverseArray;

public class ReverseArray {

    public int[] reverseArray(int[] nums) {
        int startIndex = 0;
        int endIndex = nums.length - 1;

        while (endIndex > startIndex) {
            swap(nums, startIndex, endIndex);

            startIndex++;
            endIndex--;
        }

        return nums;
    }

    public void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }
}
