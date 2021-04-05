package jpabook.start.version;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Getter
    @PersistenceContext
    private EntityManager em;

    public Board find(Long id) {
        return em.find(Board.class, id);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void save(Board board) {
        em.merge(board);
        em.flush();
    }

}
