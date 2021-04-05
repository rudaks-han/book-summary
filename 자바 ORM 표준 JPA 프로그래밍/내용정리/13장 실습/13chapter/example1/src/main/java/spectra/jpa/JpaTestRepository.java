package spectra.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestRepository extends JpaRepository<Order1, String> {

    @Query(value="select o from Order1 o join fetch o.member1")
    List<Order1> findOrders();

}
