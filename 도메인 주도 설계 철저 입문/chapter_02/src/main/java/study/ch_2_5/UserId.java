package study.ch_2_5;

public class UserId {
    private String value;

    public UserId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);
        this.value = value;
    }
}
