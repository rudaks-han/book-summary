package study.ch_12_1;

import lombok.Getter;

@Getter
public class OwnerId {
    private String value;

    public OwnerId(String value) {
        if (value == null)
            throw new IllegalArgumentException("value : " + value);

        this.value = value;
    }
}
