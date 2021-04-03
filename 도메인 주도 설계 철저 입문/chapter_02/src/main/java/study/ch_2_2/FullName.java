package study.ch_2_2;

import lombok.Getter;

import java.util.Objects;

@Getter
public class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        if (firstName == null)
            throw new IllegalArgumentException("firstName: " + firstName);
        if (lastName == null)
            throw new IllegalArgumentException("lastName: " + lastName);
        if (!validateName(firstName))
            throw new IllegalArgumentException("lastName: " + lastName);
        if (!validateName(lastName))
            throw new IllegalArgumentException("lastName: " + lastName);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean validateName(String value) {
        return value.matches("^[a-zA-Z]+$");
    }

    public static void main(String[] args) {
        FullName fullName = new FullName("masanobu", "naruse");
        System.out.println(fullName.getLastName()); // naruse가 출력됨
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstName, fullName.firstName) &&
                Objects.equals(lastName, fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
