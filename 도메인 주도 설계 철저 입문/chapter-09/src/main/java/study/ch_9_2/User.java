package study.ch_9_2;

import lombok.Getter;

import java.sql.*;
import java.util.UUID;

@Getter
public class User
{
    private UserId id;
    private UserName name;

    /*// 사용자를 최초 생성할 때 실행되는 생성자 메서드
    public User(UserName name) throws SQLException {
        String seqId;

        Connection conn = DriverManager.getConnection("url", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT seq = (NEXT VALUE FOR useSeq)");
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            seqId = resultSet.getString("seq");
        } else {
            throw new SQLException();
        }

        id = new UserId(seqId);
        this.name = name;
    }*/

    // 사용자 객체를 복원할 때 실행되는 생성자 메서드
    public User(UserId id, UserName name) {
        if (id == null)
            throw new IllegalArgumentException("id : " + id);
        if (name == null)
            throw new IllegalArgumentException("name : " + name);

        this.id = id;
        this.name = name;
    }

    public void setId(UserId id) {
        this.id = id;
    }
}
