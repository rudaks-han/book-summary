package study.ch_13_1;

import java.util.List;

public class CircleApplicationService {
    private final ICircleRepository circleRepository;
    private final IUserRepository userRepository;

    public CircleApplicationService(ICircleRepository circleRepository, IUserRepository userRepository) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
    }

    public void join(CircleJoinComamnd command) {
        CircleId circleId = new CircleId(command.getCircleId());
        Circle circle = circleRepository.find(circleId);

        List<User> users = userRepository.find(circle.getMembers());
        // 서클에 소속된 프리미엄 사용자의 수에 따라 최대 인원이 결정됨
        long premiumUserNumber = users.stream().filter(User::isPremium).count();
        long circleUpperLimit = premiumUserNumber < 10 ? 30 : 50;
        if (circle.countMembers() >= circleUpperLimit) {
            throw new CircleFullException(circleId.getValue());
        }
    }

}
