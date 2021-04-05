package jpabook.start.batch;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

public class StatelessSessionExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        Transaction tx = session.beginTransaction();
        ScrollableResults scroll = session.createQuery("select p from Product p").scroll();

        while (scroll.next()) {
            Product p = (Product) scroll.get(0);
            p.setPrice(p.getPrice() + 100);
            session.update(p);
        }

        tx.commit();
        session.close();

        emf.close(); //엔티티 매니저 팩토리 종료
    }


}
