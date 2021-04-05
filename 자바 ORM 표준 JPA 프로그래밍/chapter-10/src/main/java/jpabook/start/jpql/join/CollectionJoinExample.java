package jpabook.start.jpql.join;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Base;
import jpabook.start.Team;

public class CollectionJoinExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        Query query = em.createQuery("select t from Team t left join t.members where t.name = :teamName");
        query.setParameter("teamName", "개발팀");

        List<Team> results = query.getResultList();

        System.out.println("----------- result -----------");
        for (Team team: results) {
            System.out.println("name: " + team.getName());
        }
    }
}
