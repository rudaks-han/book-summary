package study.ch_9_3;

import study.ch_9_2.UserId;

public class User {
    // 외부로 공개하지 않아도 된다
    private UserId id;

    public Circle createCircle(CircleName circleName) {
        return new Circle(
                id,
                circleName
        );
    }
}
