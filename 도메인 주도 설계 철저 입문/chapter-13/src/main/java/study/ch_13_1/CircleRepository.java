package study.ch_13_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CircleRepository implements ICircleRepository {

    private Connection connection;

    public void save(Circle circle) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "MERGE INTO circles USING (" +
                        "   SELECT :id AS id, :name AS name, :ownerId AS ownerId" +
                        ") AS data " +
                        "ON circles.id = data.id" +
                        "WHEN MATCHED THEN" +
                        "   UPDATE SET name = data.name, owenerId = data.ownerId " +
                        "WHEN NOT MATCHED THEN" +
                        "   INSERT (id, name, ownerId)" +
                        "   VALUES(data.id, data.name, data.ownerId)"
        );
        pstmt.setString(1, circle.getId().getValue());
        pstmt.setString(2, circle.getName().getValue());
        pstmt.setString(3, circle.getOwnerId().getValue());
        pstmt.executeUpdate();
        pstmt.close();

        PreparedStatement pstmt2 = connection.prepareStatement(
                "MERGE INTO userCircles USING (" +
                        "   SELECT :userId AS userId, :circleId AS circleId" +
                        ") AS data " +
                        "ON userCircles.userId = data.userId AND userCircles.circleId = data.circleId " +
                        "WHEN NOT MATCHED THEN" +
                        "   INSERT (userId, circleId)" +
                        "   VALUES(data.userId, data.circleId)"
        );
        pstmt2.setString(1, circle.getId().getValue());
        pstmt2.setString(2, circle.getMembers().get(0).getId().getValue());
        pstmt2.executeUpdate();
        pstmt2.close();
    }
}
