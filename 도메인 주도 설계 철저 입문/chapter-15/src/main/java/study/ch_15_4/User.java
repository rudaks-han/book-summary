package study.ch_15_4;

public class User {
    private UserId id;
    private UserName name;
    private Password password;

    public User(UserId id, UserName name, Password password) {
        if (id == null)
            throw new IllegalArgumentException("id: " + id);
        if (name == null)
            throw new IllegalArgumentException("name: " + name);
        if (password == null)
            throw new IllegalArgumentException("password: " + password);

        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void changeName(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name: " + name);
        this.name = name;
    }

    public boolean isSamePassword(Password password) {
        return this.password.equals(password);
    }
}
