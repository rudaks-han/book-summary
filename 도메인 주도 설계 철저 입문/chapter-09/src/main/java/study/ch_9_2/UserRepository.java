package study.ch_9_2;

import java.sql.*;

public class UserRepository implements IUserRepository {
    private NumberingApi numberingApi;

    // 객체 저장에 관계형 데이터베이스를 사용하지만
    @Override
    public User find(UserId id) throws SQLException {
        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        pstmt.setString(1, id.getValue());
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            return new User(
                    id,
                    new UserName(name)
            );
        } else {
            throw null;
        }
    }

    // 자동 번호 매기기는 다른 기술을 사용함
    @Override
    public UserId nextIdentity()
    {
        Response response = numberingApi.request();
        return new UserId(response.getNextId());
    }


    @Override
    public void save(User user) {

    }
}
