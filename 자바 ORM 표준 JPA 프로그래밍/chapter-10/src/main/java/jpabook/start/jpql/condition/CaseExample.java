package jpabook.start.jpql.condition;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Base;

public class CaseExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select " +
                                         "case " +
                                         "  when m.age <= 10 then '학생' " +
                                         "  when m.age > 10 and m.age <= 20 then '경로' " +
                                         "  else '일반' " +
                                         "end " +
                                         "from Member m", String.class);

        List<String> results = query.getResultList();
        
        System.out.println("----------- result -----------");
        for (String str: results) {
            System.out.println("str: " + str);
        }
    }

}
