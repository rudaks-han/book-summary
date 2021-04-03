package study.ch_15_4;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Password
{
    private String value;

    public Password(String value) {
        if (StringUtils.isEmpty(value))
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}
