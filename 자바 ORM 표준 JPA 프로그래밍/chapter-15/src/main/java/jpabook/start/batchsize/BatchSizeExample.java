package jpabook.start.batchsize;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BatchSizeExample {

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

        Member3 member1 = new Member3("한경만");
        em.persist(member1);

        Member3 member2 = new Member3("김지훈");
        em.persist(member2);

        Member3 member3 = new Member3("전소희");
        em.persist(member3);

        Order2 order1 = new Order2();
        order1.setMember(member1);
        em.persist(order1);

        Order2 order2 = new Order2();
        order2.setMember(member1);
        em.persist(order2);

        Order2 order3 = new Order2();
        order3.setMember(member1);
        em.persist(order3);

        em.flush();
        em.clear();

        /*
        drop table orders2;
        drop table Member3 if exists
         */
        System.out.println("------- jpql -------");
        List<Member3> members = em.createQuery("select m from Member3 m", Member3.class)
                .getResultList();

    }

}
