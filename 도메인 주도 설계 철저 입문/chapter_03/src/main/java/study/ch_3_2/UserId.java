package study.ch_3_2;

public class UserId {
    private String value;

    public UserId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}
