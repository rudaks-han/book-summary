package jpabook.start.embedded;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

public class EmbeddedExample {

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
        Address homeAddress = new Address("경기도", "미사대로", "123-123");
        Address companyAddress = new Address("서울", "반포대로", "456-456");
        Period period = new Period(new Date(), new Date());

        Member member = new Member("kmhan", "한경만", period, homeAddress, companyAddress);
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, "kmhan");
        findMember.setCompanyAddress(null);
    }
}
