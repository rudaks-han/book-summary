package jpabook.start.jpql.groupby;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Base;

public class GroupByExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select t.name, count(m.age), sum(m.age), avg(m.age), max(m.age), min(m.age) from Member m left join m.team t group by t.name");

        List<Object[]> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Object[] o: results) {
            System.out.println("name: " + o[0]);
            System.out.println("count age: " + o[1]);
            System.out.println("sum age: " + o[2]);
            System.out.println("avg age: " + o[3]);
            System.out.println("max age: " + o[4]);
            System.out.println("min age: " + o[5]);
        }
    }
}
