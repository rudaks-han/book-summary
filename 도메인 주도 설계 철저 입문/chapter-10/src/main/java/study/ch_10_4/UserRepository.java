package study.ch_10_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {
    // 인자로 전달받은 데이터베이스 커넥션
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement pstmt = connection.prepareStatement(
                "MERGE INTO users USING (" +
                        "   SELECT :id AS id, :name AS name" +
                        ") AS data " +
                        "ON users.id = data.id" +
                        "WHEN MATCHED THEN" +
                        "   UPDATE SET name = data.name " +
                        "WHEN NOT MATCHED THEN" +
                        "   INSERT (id, name)" +
                        "   VALUES(data.id, data.name)"
        );
        pstmt.setString(1, user.getId().getValue());
        pstmt.setString(2, user.getName().getValue());
        pstmt.executeUpdate();
        connection.commit();
    }
}
