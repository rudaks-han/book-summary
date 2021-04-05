package jpabook.start.batch;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

public class ScrollExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득
        Session session = em.unwrap(Session.class);

        try {
            tx.begin(); //트랜잭션 시작

            ScrollableResults scroll = session.createQuery("select p from Product p")
                .setCacheMode(CacheMode.IGNORE)
                .scroll(ScrollMode.FORWARD_ONLY);

            int count = 0;

            while (scroll.next()) {
                Product p = (Product) scroll.get(0);
                p.setPrice(p.getPrice() + 100);

                count ++;

                if (count % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }

            tx.commit();//트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }


}
