package jpabook.start.exception.repository;

import jpabook.start.exception.jpo.member.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class NoResultExceptionTestRepository {

    @PersistenceContext
    EntityManager em;

    public Member findAllMember() throws NoResultException {
        return em.createQuery("select m from Member m", Member.class)
                .getSingleResult();
    }
}
