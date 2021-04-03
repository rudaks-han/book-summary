package study.ch_4_4;

import java.sql.*;

public class Program {
    public void createUser(String userName) throws Exception {
        User user = new User(new UserName(userName));
        UserService userService = new UserService();
        if (userService.exists(user)) {
            throw new IllegalArgumentException(user.getName().getValue() + "은 이미 존재하는 사용자명임");
        }

        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
        pstmt.setString(1, user.getId().getValue());
        pstmt.setString(2, user.getName().getValue());
        pstmt.executeUpdate();
    }
}
