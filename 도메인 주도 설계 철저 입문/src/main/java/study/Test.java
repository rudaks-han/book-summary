package study;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class Test {
    public static void main(String[] args) {
        User user1 = new User("rudaks");
        User user2 = new User("rudaks");

        System.out.println(user1.equals(user2));

    }
}

@Getter
@AllArgsConstructor
@EqualsAndHashCode
class User {
    private String id;
}