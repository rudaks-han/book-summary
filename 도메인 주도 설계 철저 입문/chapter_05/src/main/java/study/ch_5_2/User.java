package study.ch_5_2;

import lombok.Getter;

@Getter
public class User implements Cloneable {
    private UserId id;
    private UserName name;

    public User(UserId id, UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = id;
        this.name = name;
    }

    public void changeUserName(UserName name) {
        this.name = name;
    }
}
