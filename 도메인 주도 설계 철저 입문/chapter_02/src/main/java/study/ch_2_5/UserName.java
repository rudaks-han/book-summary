package study.ch_2_5;

public class UserName {
    private final String value;

    public UserName(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);
        if (value.length() < 3)
            throw new IllegalArgumentException("사용자명은 3글자 이상어야함. value: " + value);

        this.value = value;
    }
}
