package study.layered;

import lombok.Getter;

@Getter
public class UserGetCommand {
    private String id;

    public UserGetCommand(String id) {
        this.id = id;
    }
}
