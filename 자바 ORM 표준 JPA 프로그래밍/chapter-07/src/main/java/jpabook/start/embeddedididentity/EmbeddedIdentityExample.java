package jpabook.start.embeddedididentity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EmbeddedIdentityExample {

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
        Parent4 parent = new Parent4("parent_id", "부모");
        em.persist(parent);

        ChildId4 childId = new ChildId4("parent_id", "child_id1");
        Child4 child = new Child4(childId, parent, "자식1");
        em.persist(child);

        ChildId4 childId2 = new ChildId4("parent_id", "child_id2");
        Child4 child2 = new Child4(childId2, parent, "자식2");
        em.persist(child2);

        GrandChildId4 grandChildId = new GrandChildId4(childId, "grandchild_id");
        GrandChild4 grandChild = new GrandChild4(grandChildId, child, "손자");
        em.persist(grandChild);
    }
}
