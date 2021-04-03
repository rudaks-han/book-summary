package study.ch_13_1.specification;

import study.ch_13_1.Circle;
import study.ch_13_1.IUserRepository;
import study.ch_13_1.User;

import java.util.List;

public class CircleFullSpecification {
    private final IUserRepository userRepository;

    public CircleFullSpecification(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isSatisfiedBy(Circle circle) {
        List<User> users = userRepository.find(circle.getMembers());
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        return circle.countMembers() >= circleUpperLimit;
    }
}
