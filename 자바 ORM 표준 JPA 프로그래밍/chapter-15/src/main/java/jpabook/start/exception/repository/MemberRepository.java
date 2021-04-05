package jpabook.start.exception.repository;

import jpabook.start.exception.jpo.member.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) throws NoResultException {
        return em.createQuery("select m from Member m", Member.class)
                .getSingleResult();
    }
}
