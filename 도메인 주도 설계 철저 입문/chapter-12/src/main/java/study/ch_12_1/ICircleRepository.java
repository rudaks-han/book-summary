package study.ch_12_1;

import java.sql.SQLException;

public interface ICircleRepository {
    void save(Circle circle) throws SQLException;
}
