package spectra.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Test1 /*implements ApplicationRunner*/ {

    public void run(ApplicationArguments args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-jpa-application");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Member1 member = new Member1("shjeon", "전소희");

        em.persist(member);

        Member1 findMember = em.find(Member1.class, "shjeon");
        Order1 order = new Order1("test", findMember);
        order.setMember1(findMember);

        em.persist(order);
        transaction.commit();
    }
}
