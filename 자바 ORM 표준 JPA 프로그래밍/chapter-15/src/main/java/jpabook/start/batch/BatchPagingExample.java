package jpabook.start.batch;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BatchPagingExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void testSave(EntityManager em) {

        int pageSize = 100;

        for (int i=0; i<10; i++) {
            List<Product> resultList = em.createQuery("select p from Product p", Product.class)
                .setFirstResult(i * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

            for (Product product: resultList) {
                product.setPrice(product.getPrice() + 100);
            }

            em.flush();
            em.clear();
        }

    }

}
