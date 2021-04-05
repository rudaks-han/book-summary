package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Base {
    protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static void init() {
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

       // emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void testSave(EntityManager em) {
        Member member = new Member();
        member.setName("한경만");
        member.setAge(10);

        Address address = new Address("seoul", "seocho", "123-123");
        Set<String> favoriteFoods = new HashSet<>();
        favoriteFoods.add("라면");
        favoriteFoods.add("삼겹살");

        member.setAddress(address);

        em.persist(member);

        Member member2 = new Member();
        member2.setName("김지훈");
        member2.setAge(12);
        member2.setAddress(new Address("incheon", "namgu", "333-111"));

        em.persist(member2);

        Team team = new Team("개발팀");
        team.getMembers().add(member);
        team.setCreatedDate(new Date());

        em.persist(team);

        Team team2 = new Team("연구소");
        team2.getMembers().add(member2);
        team2.setCreatedDate(new Date());

        em.persist(team2);

        member.setTeam(team);
        member2.setTeam(team2);

    }
}
