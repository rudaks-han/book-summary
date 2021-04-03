package study.ch_12_1;

import lombok.Getter;

@Getter
public class User
{
    private UserId id;
    private UserName name;

    // 사용자 객체를 복원할 때 실행되는 생성자 메서드
    public User(UserId id, UserName name) {
        if (id == null)
            throw new IllegalArgumentException("id : " + id);
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = id;
        this.name = name;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public void notify(IUserNotification note) {
        note.id(id);
        note.name(name);
    }

    public void changeName(UserName name) {
        this.name = name;
    }
}
