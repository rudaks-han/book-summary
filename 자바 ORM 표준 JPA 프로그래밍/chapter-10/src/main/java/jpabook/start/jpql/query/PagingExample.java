package jpabook.start.jpql.query;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jpabook.start.Base;
import jpabook.start.Member;

public class PagingExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("select m from Member m order by m.name desc", Member.class);
        query.setFirstResult(10);
        query.setMaxResults(20);

        List<Member> results = query.getResultList();

        for (Member member: results) {
            System.out.println("name: " + member.getName());
        }
    }

}
