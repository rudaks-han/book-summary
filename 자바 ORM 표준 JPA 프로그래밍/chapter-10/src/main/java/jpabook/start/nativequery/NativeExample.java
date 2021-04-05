package jpabook.start.nativequery;

import jpabook.start.Base;
import jpabook.start.Member;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class NativeExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {

        String sql = "select id, name, age, city, street, zipCode, team_id from member where id = ?";

        Query nativeQuery = em.createNativeQuery(sql, Member.class)
                .setParameter(1, 1);
        List<Member> results = nativeQuery.getResultList();

        System.out.println("----------- result -----------");
        for (Member member: results) {
            System.out.println("name: " + member.getName());
        }
    }

}
