package jpabook.start.jpql.join;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jpabook.start.Base;

public class ThetaJoinExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        TypedQuery query = em.createQuery("select count(m) from Member m, Team t where m.name = t.name", Long.class);

        Long count = (Long) query.getSingleResult();

        System.out.println("----------- result -----------");
        System.out.println(count);
    }
}
