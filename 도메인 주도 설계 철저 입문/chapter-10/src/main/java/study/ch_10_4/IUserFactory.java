package study.ch_10_4;

import java.sql.SQLException;

public interface IUserFactory {
    User create(UserName name) throws SQLException;
}
