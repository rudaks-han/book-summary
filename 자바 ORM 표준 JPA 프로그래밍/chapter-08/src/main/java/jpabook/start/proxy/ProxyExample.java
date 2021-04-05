package jpabook.start.proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProxyExample {

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

        Team team = new Team("개발팀");
        em.persist(team);

        Member member = new Member("kmhan", "한경만", team);
        em.persist(member);

        em.flush();
        em.clear();

        System.out.println("# execute findMember");
        Member findMember = em.find(Member.class, "kmhan");
        System.out.println("# findMember: " + findMember.getName());

        System.out.println("# getTeam");
        findMember.getTeam().getName();

        System.err.println("findMember: " + findMember.getClass()); // Member
        System.err.println("findMember.getTeam(): " + findMember.getTeam().getClass()); // Team$HibernateProxy$8ROLyCGi

        em.clear();

        System.out.println("# execute getReferenceMember");
        Member findMember2 = em.getReference(Member.class, "kmhan");
        System.err.println("findMember2: " + findMember2.getClass()); // Member$HibernateProxy$8ROLyCGi

        System.out.println("# getName");
        System.out.println("member name : " + findMember2.getName()); // getReference는 실제 사용시점에 조회

        boolean isLoad = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(findMember2);
        System.out.println("isLoad : " + isLoad);

    }

}
