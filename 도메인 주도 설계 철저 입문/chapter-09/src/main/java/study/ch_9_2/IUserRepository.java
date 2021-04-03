package study.ch_9_2;

import java.sql.SQLException;

public interface IUserRepository {
    User find(UserId id) throws SQLException;
    void save(User user);
    UserId nextIdentity();
}
