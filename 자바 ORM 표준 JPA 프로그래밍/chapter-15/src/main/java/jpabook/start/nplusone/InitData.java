package jpabook.start.nplusone;

import javax.persistence.EntityManager;

public class InitData {

    public static void init(EntityManager em) {
        Member2 member1 = new Member2("한경만");
        em.persist(member1);

        Member2 member2 = new Member2("김지훈");
        em.persist(member2);

        Order order1 = new Order();
        order1.setMember(member1);
        em.persist(order1);

        Order order2 = new Order();
        order2.setMember(member1);
        em.persist(order2);

        member1.getOrders().add(order1);
        member1.getOrders().add(order2);

        em.flush();
        em.clear();
    }
}
