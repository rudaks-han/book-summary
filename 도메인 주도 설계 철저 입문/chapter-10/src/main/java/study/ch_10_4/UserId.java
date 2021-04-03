package study.ch_10_4;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class UserId
{
    private String value;

    public UserId(String value) {
        if (StringUtils.isEmpty(value))
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}
