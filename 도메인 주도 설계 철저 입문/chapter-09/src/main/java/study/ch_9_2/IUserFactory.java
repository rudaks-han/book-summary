package study.ch_9_2;

import java.sql.SQLException;

public interface IUserFactory {
    User create(UserName name) throws SQLException;
}
