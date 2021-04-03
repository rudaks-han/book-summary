package study.ch_13_1.specification;

import study.ch_13_1.CircleId;
import study.ch_13_1.User;

import java.util.List;

public class CircleMembers {
    private final CircleId id;
    private final User owner;
    private final List<User> members;

    public CircleMembers(CircleId id, User owner, List<User> members) {
        this.id = id;
        this.owner = owner;
        this.members = members;
    }

    public int countMembers() {
        return members.size() + 1;
    }

    public long countPremiumMembers(boolean containsOwner) {
        long premiumUserNumber = members.stream().filter(User::isPremium).count();
        if (containsOwner) {
            return premiumUserNumber + (owner.isPremium() ? 1 : 0);
        } else {
            return premiumUserNumber;
        }
    }
}
