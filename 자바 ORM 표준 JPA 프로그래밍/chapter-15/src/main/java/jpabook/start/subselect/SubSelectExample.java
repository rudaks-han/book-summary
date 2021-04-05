package jpabook.start.subselect;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubSelectExample {

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

        Member4 member1 = new Member4("한경만");
        em.persist(member1);

        Member4 member2 = new Member4("김지훈");
        em.persist(member2);

        Member4 member3 = new Member4("전소희");
        em.persist(member3);

        Order3 order1 = new Order3();
        order1.setMember(member1);
        em.persist(order1);

        Order3 order2 = new Order3();
        order2.setMember(member1);
        em.persist(order2);

        Order3 order3 = new Order3();
        order3.setMember(member1);
        em.persist(order3);

        em.flush();
        em.clear();

        /*
        drop table orders3;
        drop table Member4 if exists
         */
        System.out.println("------- jpql -------");
        List<Member4> members = em.createQuery("select m from Member4 m", Member4.class)
                .getResultList();

    }

}
