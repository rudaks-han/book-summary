package jpabook.start.delete;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneWayExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            delete(em);
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
        Team3 team = new Team3("team1", "팀1");
        em.persist(team);

        Member3 member1 = new Member3("member1", "회원1");
        member1.setTeam(team);
        em.persist(member1);

        Member3 member2 = new Member3("member2", "회원2");
        member2.setTeam(team);
        em.persist(member2);
    }



    public static void delete(EntityManager em) {
        Team3 team = em.find(Team3.class, "team1");
        em.remove(team);

        /*em.createQuery("delete from Member3 m where m.team.id = :teamId")
            .setParameter("teamId", "team1").executeUpdate();*/
    }
}
