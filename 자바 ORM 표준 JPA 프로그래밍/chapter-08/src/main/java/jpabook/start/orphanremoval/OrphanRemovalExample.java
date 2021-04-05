package jpabook.start.orphanremoval;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OrphanRemovalExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            remove(em);
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
        Child2 child1 = new Child2();
        Child2 child2 = new Child2();

        Parent2 parent2 = new Parent2(1L);
        child1.setParent(parent2);
        child2.setParent(parent2);

        parent2.getChildren().add(child1);
        parent2.getChildren().add(child2);

        em.persist(parent2);
    }

    public static void remove(EntityManager em) {
        Parent2 parent = em.find(Parent2.class, 1L);

        parent.getChildren().remove(0);
    }

}
