package study.layered;

import java.sql.SQLException;

public interface IUserFactory {
    User create(UserName name);
}
