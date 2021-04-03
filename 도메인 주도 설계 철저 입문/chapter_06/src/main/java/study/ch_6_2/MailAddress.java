package study.ch_6_2;

import lombok.Getter;

@Getter
public class MailAddress
{
    private String value;

    public MailAddress(String value) {
        if (value == null)
            throw new IllegalArgumentException("value: " + value);

        this.value = value;
    }
}
