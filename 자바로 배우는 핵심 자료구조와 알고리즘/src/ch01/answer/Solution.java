package ch01.answer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

class Solution {
    public String solution(String[] participant, String[] completion) {
        String answer = "";

        for (int i=0; i<participant.length; i++) {
            boolean exists = false;
            for (int k=0; k<completion.length; k++) {
                if (participant[i] == completion[k]) {
                    exists = true;
                }
            }

            if (!exists) {
                return participant[i];

            }
        }

        return answer;
    }

    public static void main(String[] args) {
        String [] participatn = new String[]{"marina", "josipa", "nikola", "vinko", "filipa"};
        String [] completion = new String[]{"josipa", "filipa", "marina", "nikola"};

        Solution solution = new Solution();
        String result = solution.solution(participatn, completion);
        System.out.println(result);

        HashMap map = new HashMap();
    }
}
