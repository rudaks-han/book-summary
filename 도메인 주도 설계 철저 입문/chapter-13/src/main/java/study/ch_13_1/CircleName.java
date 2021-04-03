package study.ch_13_1;

import lombok.Getter;

@Getter
public class CircleName {
    private String value;

    public CircleName(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);

        this.value = value;
    }
}
