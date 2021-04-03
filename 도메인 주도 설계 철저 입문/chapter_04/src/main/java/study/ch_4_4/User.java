package study.ch_4_4;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private UserId id;
    private UserName name;

    public User(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        id = new UserId(UUID.randomUUID().toString());
        this.name = name;
    }
}
