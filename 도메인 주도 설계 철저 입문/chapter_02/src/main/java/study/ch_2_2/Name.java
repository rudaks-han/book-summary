package study.ch_2_2;

public class Name {
    private final String value;

    public Name(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);
        if (!value.matches("^[a-zA-Z]+$"))
            throw new IllegalArgumentException("허가되지 않은 문자가 사용됨: " + value);

        this.value = value;
    }
}
