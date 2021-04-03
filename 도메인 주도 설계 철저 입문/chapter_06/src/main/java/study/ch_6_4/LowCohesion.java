package study.ch_6_4;

public class LowCohesion {
    private int value1;
    private int value2;
    private int value3;
    private int value4;

    public int methodA() {
        return value1 + value2;
    }

    public int methodB() {
        return value3 + value4;
    }
}
