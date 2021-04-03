package study.ch_10_4;

import java.sql.SQLException;

public interface IUserRepository {
    void save(User user) throws SQLException;
}
