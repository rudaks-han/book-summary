package jpabook.start.jpql.join;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Base;
import jpabook.start.Member;

public class FetchJoinExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        System.out.println("----------- query -----------");
        Query query = em.createQuery("select m from Member m join fetch m.team");

        List<Member> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Member member: results) {
            System.out.println("name: " + member.getName());
        }
    }
}
