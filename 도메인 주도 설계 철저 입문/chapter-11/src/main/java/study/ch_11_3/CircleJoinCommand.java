package study.ch_11_3;

import lombok.Getter;

@Getter
public class CircleJoinCommand {
    private String userId;
    private String circleId;

    public CircleJoinCommand(String userId, String circleId) {
        this.userId = userId;
        this.circleId = circleId;
    }
}
