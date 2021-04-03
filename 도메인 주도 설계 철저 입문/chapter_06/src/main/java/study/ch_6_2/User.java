package study.ch_6_2;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User implements Cloneable {
    private UserId id;
    private UserName name;
    private MailAddress mailAddress;

    public User(UserName name) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = new UserId(UUID.randomUUID().toString());
        this.name = name;
    }

    public User(UserId id, UserName name) {
        if (id == null)
            throw new IllegalArgumentException("id : " + id);
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = id;
        this.name = name;
    }

    public User(UserName name, MailAddress mailAddress) {
        if (name == null)
            throw new IllegalArgumentException("name : " + name);
        if (mailAddress == null)
            throw new IllegalArgumentException("mailAddress : " + mailAddress);

        this.name = name;
        this.mailAddress = mailAddress;
    }

    public void changeName(UserName name) {
        this.name = name;
    }

    public void changeMailAddress(MailAddress mailAddress) {
        this.mailAddress = mailAddress;
    }
}
