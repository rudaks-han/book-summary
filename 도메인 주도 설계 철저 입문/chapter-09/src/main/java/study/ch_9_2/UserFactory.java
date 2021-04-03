package study.ch_9_2;

import java.sql.*;

public class UserFactory implements IUserFactory {
    @Override
    public User create(UserName name) throws SQLException {
        String seqId;

        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT seq = (NEXT VALUE FOR useSeq)");
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            seqId = resultSet.getString("seq");
        } else {
            throw new SQLException();
        }

        UserId id = new UserId(seqId);
        return new User(id, name);
    }
}
