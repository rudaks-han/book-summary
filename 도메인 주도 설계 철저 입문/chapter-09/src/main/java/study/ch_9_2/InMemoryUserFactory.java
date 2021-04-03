package study.ch_9_2;

import java.sql.SQLException;

public class InMemoryUserFactory implements IUserFactory {
    private int currentId;

    @Override
    public User create(UserName name) throws SQLException {
        currentId++;

        return new User(
                new UserId(currentId+""),
                name
        );
    }
}
