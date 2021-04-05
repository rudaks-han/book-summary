package jpabook.start.jpql.query;

import jpabook.start.Base;
import jpabook.start.Member;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class QueryExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select m from Member m");
        List<Member> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Member member: results) {
            System.out.println(member.getName());
        }

        query = em.createQuery("select m.name, m.age from Member m where m.name = :name")
                .setParameter("name", "한경만");
        results = query.getResultList();

        for (Object o: results) {
            Object[] result = (Object[]) o;
            System.out.println("name: " + result[0]);
            System.out.println("age: " + result[1]);
        }
    }

}
