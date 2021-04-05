package jpabook.start.entitygraph;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class EntityGraphExample {

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
        Company company = new Company(1L, "스펙트라");
        em.persist(company);

        Team team = new Team();
        team.setCompany(company);
        em.persist(team);

        em.flush();
        em.clear();

        EntityGraph graph = em.getEntityGraph("Team.withCompany");

        Map hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);

        Team findTeam = em.find(Team.class, 1L, hints);

    }

}
