package spectra.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @PersistenceContext
    EntityManager em;

    public Order1 findOne(String orderId){
        return em.find(Order1.class, orderId);
    }

}
