package jpabook.start.jpql.udf;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Base;
import jpabook.start.Member;

public class UserDefinedFuncExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select group_concat(m.name) from Member m");
        List<String> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (String str: results) {
            System.out.println(str);
        }
    }

}
