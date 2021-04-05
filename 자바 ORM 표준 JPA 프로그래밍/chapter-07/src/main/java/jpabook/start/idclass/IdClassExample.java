package jpabook.start.idclass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class IdClassExample {

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
        Parent parent = new Parent("id1", "id2", "부모이름");
        em.persist(parent);

        Child child = new Child("1", parent);
        em.persist(child);

        em.flush();
        em.clear();

        ParentId parentId = new ParentId("id1", "id2");
        Parent findParent = em.find(Parent.class, parentId);
        System.out.println(findParent.getName());

        Child findChild = em.find(Child.class, "1");
        System.out.println(findChild.getParent().getName());
    }
}
