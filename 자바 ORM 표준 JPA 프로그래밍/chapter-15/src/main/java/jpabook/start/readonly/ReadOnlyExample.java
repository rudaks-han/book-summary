package jpabook.start.readonly;

import javax.persistence.*;
import java.util.List;

public class ReadOnlyExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            testSave(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    public static void testSave(EntityManager em) {

        Order4 order1 = new Order4("라면", 1000);
        em.persist(order1);

        Order4 order2 = new Order4("과자", 500);
        em.persist(order2);

        /*System.out.println("------- jpql -------");
        List<Order4> orders = em.createQuery("select o from orders4 o", Order4.class)
                .getResultList();

        for (Order4 order: orders) {
            order.setName("초기화");
        }*/

        Query query = em.createQuery("select o.id, o.name, o.price from orders4 o");

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList){
            Long id = (Long)row[0];
            String name = (String)row[1];
            int price = (Integer) row[2];

            System.out.println("id : " + id + ", name : " + name + ", price : " + price);
        }
    }

}
