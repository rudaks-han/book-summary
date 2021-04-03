package study.ch_2_2;

import lombok.Getter;

@Getter
public class FullName2
{
    private final Name firstName;
    private final Name lastName;

    public FullName2(Name firstName, Name lastName) {
        if (firstName == null)
            throw new IllegalArgumentException("firstName: " + firstName);
        if (lastName == null)
            throw new IllegalArgumentException("lastName: " + lastName);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean validateName(String value) {
        return value.matches("^[a-zA-Z]+$");
    }
}
