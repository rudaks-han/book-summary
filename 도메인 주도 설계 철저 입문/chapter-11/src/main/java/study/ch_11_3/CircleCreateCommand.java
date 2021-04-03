package study.ch_11_3;

import lombok.Getter;

@Getter
public class CircleCreateCommand {
    private String userId;
    private String name;

    public CircleCreateCommand(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
