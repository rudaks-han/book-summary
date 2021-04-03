package study.ch_2_3;

import lombok.Getter;

import java.util.Objects;

@Getter
public class FullName
{
    private final FirstName firstName;
    private final LastName lastName;

    public FullName(FirstName firstName, LastName lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
