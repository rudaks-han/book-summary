package jpabook.start.onetooneprimarytwoway;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneToOnePrimaryTwoWayExample {

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
        Locker6 locker1 = new Locker6("락커1");
        Locker6 locker2 = new Locker6("락커2");

        Member6 member1 = new Member6("회원1", locker1);
        Member6 member2 = new Member6("회원2", locker2);

        locker1.setMember(member1);
        locker2.setMember(member2);

        em.persist(locker1);
        em.persist(locker2);
        em.persist(member1);
        em.persist(member2);
/*
        em.flush();
        em.clear();

        Team4 findTeam = em.find(Team4.class, 3L);
        System.out.println("teamName: " + findTeam.getName());

        Member4 findMember = em.find(Member4.class, 1L);
        System.out.println("teamName: " + findMember.getTeam());*/
    }

}
