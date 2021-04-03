package study.ch_13_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CircleQueryService {
    private Connection connection;

    public CircleGetSummariesResult getSummaries(CircleGetSummariesCommand command) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT " +
                        "circles.id as circleId, " +
                        "users.name as ownerName " +
                        "FROM circles " +
                        "LEFT OUTER JOIN users " +
                        "ON circles.ownerId = users.id " +
                        "ORDER BY circles.id " +
                        "OFFSET :skip ROW " +
                        "FETCH NEXT :size ROWS ONLY"
        );
        pstmt.setInt(1, (command.getPage() - 1));
        pstmt.setInt(2, command.getSize());

        ResultSet resultSet = pstmt.executeQuery();

        List<CircleSummaryData> summaries = new ArrayList<>();

        while(resultSet.next()) {
            String circleId = resultSet.getString("circleId");
            String ownerName = resultSet.getString("ownerName");
            CircleSummaryData summary = new CircleSummaryData(circleId, ownerName);
            summaries.add(summary);
        }

        return new CircleGetSummariesResult(summaries);
    }
}
