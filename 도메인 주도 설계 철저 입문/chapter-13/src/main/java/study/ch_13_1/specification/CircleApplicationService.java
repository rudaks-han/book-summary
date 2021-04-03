package study.ch_13_1.specification;

import study.ch_13_1.*;

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

        CircleFullSpecification circleFullSpecification = new CircleFullSpecification(userRepository);
        if (circleFullSpecification.isSatisfiedBy(circle)) {
            throw new CircleFullException(circleId.getValue());
        }
    }
}
