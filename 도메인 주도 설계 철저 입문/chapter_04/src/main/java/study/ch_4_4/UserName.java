package study.ch_4_4;

import lombok.Getter;

@Getter
public class UserName {
    private String value;

    public UserName(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);
        if (value.length() < 3)
            throw new IllegalArgumentException("사용자명은 3글자 이상어야 함");

        this.value = value;
    }
}
