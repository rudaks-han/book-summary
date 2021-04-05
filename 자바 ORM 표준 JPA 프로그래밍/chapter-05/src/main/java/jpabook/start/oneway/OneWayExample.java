package jpabook.start.oneway;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class OneWayExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            query(em);
            update(em);
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
        Team1 team = new Team1("team1", "팀1");
        em.persist(team);

        Member1 member1 = new Member1("member1", "회원1");
        member1.setTeam(team);
        em.persist(member1);

        Member1 member2 = new Member1("member2", "회원2", team);
        member2.setTeam(team);
        em.persist(member2);
    }

    public static void query(EntityManager em) {
        String jpql = "select m from Member1 m join m.team t where t.name = :teamName";

        List<Member1> resultList = em.createQuery(jpql, Member1.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member1 member : resultList) {
            System.out.println("name: " + member.getUsername());
        }

        resultList.stream()
            .map(member1 -> "name : " + member1.getUsername())
            .forEach(System.out::println);
    }

    public static void update(EntityManager em) {
        Team1 team = new Team1("team2", "팀2");
        em.persist(team);

        Member1 member = em.find(Member1.class, "member1");
        member.setTeam(team);
    }

    public static void delete(EntityManager em) {
        Member1 member = em.find(Member1.class, "member1");
        member.setTeam(null);
    }
}
