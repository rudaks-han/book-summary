package study.ch_9_2;

import lombok.Getter;

@Getter
public class UserRegisterCommand {
    private String name;

    public UserRegisterCommand(String name) {
        this.name = name;
    }
}
