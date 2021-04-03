package study.ch_3_2;

import java.util.Objects;

public class User {
    private UserId id;
    private String name;

    public User(UserId id, String name) {
        this.id = id;
        changeName(name);
    }

    public void changeName(String name) {
        if (id == null)
            throw new IllegalArgumentException("value : " + id);
        if (name == null)
            throw new IllegalArgumentException("value : " + name);

        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
