package section05.duplicates;

public class RepeatedIntegersProblem {
    public void solve(int[] array) {
        for (int i=0; i<array.length; ++i) {
            if (array[Math.abs(array[i])] > 0) {
                array[Math.abs(array[i])] = -array[Math.abs(array[i])];
            } else {
                System.out.println(Math.abs(array[i]) + " is a repetition!");
            }
        }
    }
}
