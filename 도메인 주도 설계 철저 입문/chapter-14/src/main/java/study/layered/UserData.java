package study.layered;

import lombok.Getter;

@Getter
public class UserData {
    private User user;

    public UserData(User user) {
        this.user = user;
    }
}
