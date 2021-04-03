package study.ch_13_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CircleRepository implements ICircleRepository {
    private final Connection connection;

    public CircleRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Circle circle) throws SQLException {

    }

    @Override
    public List<Circle> find(ISpecification<Circle> specification) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM circles");
        ResultSet resultSet = pstmt.executeQuery();
        List<Circle> circles = new ArrayList<>();

        while (resultSet.next()) {
            // 인스턴스를 생성해 조건에 부합하는지 확인(조건을 만족하지 않으면 버림)

            Circle circle = createInstance(resultSet);
            if (specification.isSatisfiedBy(circle)) {
                circles.add(circle);
            }
        }

        return circles;
    }

    @Override
    public List<Circle> findRecommended(long now) {
        return null;
    }

    @Override
    public List<Circle> findAll() {
        return null;
    }

    private Circle createInstance(ResultSet resultSet) {
        return null;
    }
}
