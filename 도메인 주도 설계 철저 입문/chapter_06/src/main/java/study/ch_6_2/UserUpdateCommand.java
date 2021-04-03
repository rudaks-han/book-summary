package study.ch_6_2;

import lombok.Getter;

@Getter
public class UserUpdateCommand {
    private String id;
    private String name;
    private String mailAddress;

    public UserUpdateCommand(String id) {
        this.id = id;
    }

    public UserUpdateCommand(String id, String name, String mailAddress) {
        this.id = id;
        this.name = name;
        this.mailAddress = mailAddress;
    }
}
