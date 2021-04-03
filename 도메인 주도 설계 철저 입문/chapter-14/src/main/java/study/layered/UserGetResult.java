package study.layered;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGetResult {
    private User user;

    public UserGetResult(UserData userData) {
        this.user = userData.getUser();
    }
}
