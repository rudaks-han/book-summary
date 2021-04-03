package study.ch_5_2;

import lombok.Getter;

@Getter
public class UserId {
    private String value;

    public UserId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}
