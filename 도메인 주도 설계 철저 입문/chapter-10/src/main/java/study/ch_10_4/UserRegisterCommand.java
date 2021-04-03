package study.ch_10_4;

import lombok.Getter;

@Getter
public class UserRegisterCommand {
    private String name;

    public UserRegisterCommand(String name) {
        this.name = name;
    }
}
