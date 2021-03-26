package ch09;

import java.util.Arrays;

public class Anagram {

    public static String sort(String str) {
        String [] temp = str.split("");
        Arrays.sort(temp);

        return String.join("", temp);
    }
    public static boolean isAnagram(String str1, String str2) {
        if (sort(str1).equals(sort(str2))) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String str1 = "2restful";
        String str2 = "fluster2";

        System.out.println(isAnagram(str1, str2));
    }
}
