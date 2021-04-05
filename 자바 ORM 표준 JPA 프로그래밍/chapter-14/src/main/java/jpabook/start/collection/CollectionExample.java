package jpabook.start.collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CollectionExample {

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
        Parent parent = new Parent();

        CollectionChild collectionChild1 = new CollectionChild(1L, "child1");
        CollectionChild collectionChild2 = new CollectionChild(2L, "child2");

        em.persist(collectionChild1);
        em.persist(collectionChild2);

        parent.getCollection().add(collectionChild1);
        parent.getCollection().add(collectionChild2);

        em.persist(parent);

        em.clear();

        System.out.println("::: exists : " + parent.getCollection().contains(collectionChild1));
    }

}
