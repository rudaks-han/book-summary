package study.ch_13_2;

import java.sql.SQLException;
import java.util.List;

public interface ICircleRepository
{
    void save(Circle circle) throws SQLException;
    List<Circle> find(ISpecification<Circle> specification) throws SQLException;

    List<Circle> findRecommended(long now);
    List<Circle> findAll();
}
