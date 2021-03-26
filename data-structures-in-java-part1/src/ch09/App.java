package ch09;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        // ArrayList
        List<String> names = new ArrayList<>();
        names.add("Kevin");
        names.add("Daniel");
        names.add("Adam");
        names.add("Ana");

        names.remove(0); // O(N)
        System.out.println(names.get(0));


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
