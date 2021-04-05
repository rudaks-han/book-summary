package jpabook.start.version;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import jpabook.start.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class
)
public class OptimisticLockTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private BoardService boardService;

    @Test(expected = OptimisticLockException.class)
    @Transactional
    public void 낙관적락_예외발생() {

        Board board = em.find(Board.class, 1L);
        board.setTitle("제목2");
        boardService.save(board);

        try {
            board.setTitle("제목3");
            boardService.save(board);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
