package study.ch_11_3;

public interface ICircleRepository {
    void save(Circle circle);
    Circle find(CircleId id);
    Circle find(CircleName name);
}
