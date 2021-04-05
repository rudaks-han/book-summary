package jpabook.start.jpql.join;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import jpabook.start.Base;
import jpabook.start.Member;
import jpabook.start.Team;

public class JoinOnExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select m.name, t.name from Member m left join m.team t on t.name = '개발팀' and m.name is null");

        List<Object[]> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Object[] o: results) {
            System.out.println("member name: " + o[0] + ", team name: " + o[1]);
        }
    }
}
