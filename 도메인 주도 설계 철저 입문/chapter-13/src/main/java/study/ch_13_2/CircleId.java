package study.ch_13_2;

import lombok.Getter;

@Getter
public class CircleId
{
    private String value;

    public CircleId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);

        this.value = value;
    }
}
