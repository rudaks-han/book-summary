package jpabook.start.querydsl;

import com.querydsl.jpa.impl.JPAUpdateClause;
import jpabook.start.Base;

import javax.persistence.EntityManager;

import static jpabook.start.QMember.member;

public class UpdateClauseExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        update(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void update(EntityManager em) {

        JPAUpdateClause updateClause = new JPAUpdateClause(em, member);

        // 실행안됨
        long count = updateClause
                .where(member.name.eq("한경만"))
                .set(member.age, 1)
                .execute();

        System.out.println("----------- result -----------");
        System.out.println("count : " + count);
    }

}
