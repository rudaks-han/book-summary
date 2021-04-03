package study.ch_11_3;

public class CircleService {
    private final ICircleRepository circleRepository;

    public CircleService(ICircleRepository circleRepository) {
        this.circleRepository = circleRepository;
    }

    public boolean exists(Circle circle) {
        Circle duplicated = circleRepository.find(circle.getName());
        return duplicated != null;
    }
}
