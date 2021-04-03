package study.ch_13_2;

import lombok.Getter;
import study.ch_13_1.*;

import java.util.List;

@Getter
public class Circle
{
    private final CircleId id;
    private CircleName name;
    private OwnerId ownerId;
    private User owner;
    // 소속된 사용자 중 프리미엄 사용자 수를 확인해야 하는데
    // 가진 정보는 사용자의 식별자뿐이다.
    private List<UserId> members;
    private long created;

    public Circle(CircleId id) {
        this.id = id;
    }

    public int countMembers() {
        return this.members.size();
    }

    // 엔티티가 사용자 리포지토리를 갖는다?
    public boolean isFull(IUserRepository userRepository) {
        List<User> users = userRepository.find(members);
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        return countMembers() >= circleUpperLimit;
    }
}
