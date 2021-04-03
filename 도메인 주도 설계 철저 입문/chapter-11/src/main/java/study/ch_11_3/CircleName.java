package study.ch_11_3;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CircleName {
    private String value;

    public CircleName(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);
        if (value.length() < 3)
            throw new IllegalArgumentException("서클명은 3글자 이상어야야 함: " + value);
        if (value.length() > 20)
            throw new IllegalArgumentException("서클명은 20글자 이하어야야 함: " + value);

        this.value = value;
    }
}
