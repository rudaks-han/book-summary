package ch09;

public class App {

    public static void main(String[] args) {

        int[] nums = new int[10];

        for (int i=0; i<10; i++) {
            nums[i] = i;
        }

        // linear search
        for (int i=0; i<10; i++) {
            if (nums[i] == 6) {
                System.out.println("We have found the item at index: " + i);
            }

        }


    }
}
