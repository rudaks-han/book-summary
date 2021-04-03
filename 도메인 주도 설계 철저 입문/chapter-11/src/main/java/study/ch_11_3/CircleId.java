package study.ch_11_3;

import lombok.Getter;

@Getter
public class CircleId {
    private String value;

    public CircleId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);

        this.value = value;
    }
}
