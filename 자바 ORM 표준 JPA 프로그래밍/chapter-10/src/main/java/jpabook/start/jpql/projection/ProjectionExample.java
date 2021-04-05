package jpabook.start.jpql.projection;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpabook.start.Address;
import jpabook.start.Base;
import jpabook.start.Member;

public class ProjectionExample extends Base {

    public static void main(String[] args) {
        init();

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
        query(em);
        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void query(EntityManager em) {
        List<Address> addresses = em.createQuery("select m.address from Member m", Address.class)
            .getResultList();

        System.out.println("----------- result -----------");
        for (Address address: addresses) {
            System.out.println(address.getStreet());
        }
    }

}
