package jpabook.start.jpql.query;

import jpabook.start.Base;
import jpabook.start.Member;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TypeQueryExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Member member: results) {
            System.out.println("name: " + member.getName());
        }
    }

}
