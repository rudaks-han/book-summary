package study.ch_10_4.unitofwork;

import study.ch_10_4.UserName;

public class User extends Entity {
    private UserName name;

    public User(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);
        this.name = name;

        markNew();
    }

    public void changeName(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);
        this.name = name;
        markDirty();
    }
}
