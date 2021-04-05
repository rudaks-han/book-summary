package jpabook.start.key;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BoardSequenceExample {

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
        BoardSequence board1 = new BoardSequence("한경만");
        BoardSequence board2 = new BoardSequence("김지훈");
        BoardSequence board3 = new BoardSequence("전소희");
        BoardSequence board4 = new BoardSequence("허소정");
        BoardSequence board5 = new BoardSequence("정은영");

        em.persist(board1);
        em.persist(board2);
        em.persist(board3);
        em.persist(board4);
        em.persist(board5);

        System.out.println("board.id : " + board5.getId());

    }
}
