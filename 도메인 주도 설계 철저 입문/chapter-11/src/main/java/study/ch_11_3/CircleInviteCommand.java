package study.ch_11_3;

import lombok.Getter;

@Getter
public class CircleInviteCommand {
    private String circleId;
    private String fromUserId;
    private String invitedUserId;

    public CircleInviteCommand(String fromUserId, String invitedUserId) {
        this.fromUserId = fromUserId;
        this.invitedUserId = invitedUserId;
    }
}
