package study.ch_12_1;

import lombok.Getter;

import java.util.List;

@Getter
public class Circle {
    private final CircleId id;
    private CircleName name;
    private OwnerId ownerId;
    // 멤버 목록을 비공개로 함
    private List<User> members;

    public Circle(CircleId id) {
        this.id = id;
    }

    public boolean isFull() {
        return this.members.size() >= 29;
    }

    public void join(User user) {
        if (user == null) {
            throw new IllegalArgumentException(user.getId().getValue());
        }

        if (isFull()) {
            throw new CircleFullException(id.getValue());
        }

        this.members.add(user);
    }
    public void changeMemberName(UserId id, UserName name) {
        User target = members.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
        if (target != null) {
            target.changeName(name);
        }
    }
}
