package study.ch_10_4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
    private static Connection connection;
    public SqlConnection() {
        Connection conn = null;
        try {
            connection = DriverManager.getConnection("url", "user", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
