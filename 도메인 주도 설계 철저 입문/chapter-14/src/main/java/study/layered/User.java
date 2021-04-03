package study.layered;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class User {
    private UserId id;
    private UserName name;
    private UserType type;

    public User(UserId id, UserName name, UserType type) {
        if (id == null)
            throw new IllegalArgumentException("id: " + id);
        if (name == null)
            throw new IllegalArgumentException("name: " + name);

        this.id = id;
        this.name = name;
        this.type = type;
    }

    public boolean isPremium() {
        return this.type == UserType.Premium;
    }

    public void changeName(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name: " + name);
        this.name = name;
    }

    public void downgrade() {
        this.type = UserType.Normal;
    }
}
