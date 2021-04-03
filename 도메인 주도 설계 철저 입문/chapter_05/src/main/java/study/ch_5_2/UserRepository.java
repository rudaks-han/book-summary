package study.ch_5_2;

import java.sql.*;

public class UserRepository implements IUserRepository {
    @Override
    public void save(User user) {
        try {
            Connection conn = new SqlConnection().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
            pstmt.setString(1, user.getId().getValue());
            pstmt.setString(2, user.getName().getValue());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User find(UserName userName) {
        try {
            Connection conn = new SqlConnection().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
            pstmt.setString(1, userName.getValue());
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("user_name");

                return new User(
                        new UserId(userId),
                        new UserName(name)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(UserName name)
    {
        return false;
    }
}

class SqlConnection {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("xxx");
    }
}