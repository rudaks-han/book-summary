package study.ch_4_4;

import java.sql.*;

public class UserService {
    public boolean exists(User user) throws SQLException {
        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        pstmt.setString(1, user.getName().getValue());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            return true;
        }

        return false;
    }
}
