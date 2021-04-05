package jpabook.start.study;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Example {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin();
            save(em);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    public static void save(EntityManager em) {

        Team10 team = new Team10(1L, "개발팀");
        em.persist(team);

        Member10 member1 = new Member10(1L, "한경만", team);
        Member10 member2 = new Member10(2L, "정은영", team);

        team.getMembers().add(member1);
        team.getMembers().add(member2);

        /*em.persist(member1);
        em.persist(member2);*/

        em.flush();
        em.clear();

        Team10 findTeam = em.find(Team10.class, 1L);
        findTeam.getMembers().clear();

    }


}
