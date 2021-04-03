package study.ch_2_3;

import org.apache.commons.lang3.StringUtils;

public class FirstName
{
    private String value;

    public FirstName(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("최소 1글자 이상이어야 함" + value);
        }

        this.value = value;
    }

}
