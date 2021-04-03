package study.ch_6_2;

import lombok.Getter;

@Getter
public class UserDeleteCommand
{
    private String id;

    public UserDeleteCommand(String id) {
        this.id = id;
    }
}
