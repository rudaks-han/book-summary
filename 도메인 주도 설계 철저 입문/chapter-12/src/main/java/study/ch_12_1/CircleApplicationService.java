package study.ch_12_1;

public class CircleApplicationService {

   /* private final ICircleRepository circleRepository;

    public void update(CircleUpdatecommand command) {
        CircleId id = new CircleId(command.getId());
        // 이 지점에서 User 객체가 복원되지만,
        Circle circle = circleRepository.find(id);
        if (circle == null)
            throw new CircleNotFoundException(id);

        if (command.getName() != null) {
            CircleName name = new CircleName(command.getName());
            circle.changeName(name);

            if (circleService.exists(circle)) {
                throw new CanNotRegisterCircleException("circle: " + circle);
            }
        }

        circleRepository.save(circle);
        // User 객체를 사용하지 않고 처리가 끝남
    }*/
}
