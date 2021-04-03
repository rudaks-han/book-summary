package study.ch_13_1;

import java.sql.SQLException;

public interface ICircleRepository {
    void save(Circle circle) throws SQLException;
    Circle find(CircleId id);
}
