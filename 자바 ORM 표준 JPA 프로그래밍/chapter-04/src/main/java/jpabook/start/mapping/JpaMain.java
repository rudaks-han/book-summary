package jpabook.start.mapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void logic(EntityManager em) {
        String id = "rudaks";

        Member member = new Member();
        member.setId(id);
        member.setUsername("한경만");
        member.setAge(null);

        em.persist(member);

        Member findMember = em.find(Member.class, id);
        System.out.println("findMember : " + findMember.toString());

        List<Member> members = em.createQuery("select m from Member2 m", Member.class)
                .getResultList();

        System.out.println("members.size: " + members.size());
    }
}
