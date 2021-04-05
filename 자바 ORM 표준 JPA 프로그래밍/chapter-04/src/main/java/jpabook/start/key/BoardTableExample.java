package jpabook.start.key;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BoardTableExample {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void logic(EntityManager em) {
        BoardTable board1 = new BoardTable("한경만");
        BoardTable board2 = new BoardTable("김지훈");
        BoardTable board3 = new BoardTable("전소희");
        BoardTable board4 = new BoardTable("허소정");
        BoardTable board5 = new BoardTable("정은영");

        em.persist(board1);
        em.persist(board2);
        em.persist(board3);
        em.persist(board4);
        em.persist(board5);

        //System.out.println("board.id : " + board1.getId());

    }
}
