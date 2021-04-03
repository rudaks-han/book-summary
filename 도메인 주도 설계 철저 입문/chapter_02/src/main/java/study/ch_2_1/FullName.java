package study.ch_2_1;

import lombok.Getter;

@Getter
public class FullName {
    private String firstName;
    private String lastName;

    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static void main(String[] args) {
        FullName fullName = new FullName("masanobu", "naruse");
        System.out.println(fullName.getLastName()); // naruse가 출력됨
    }
}
