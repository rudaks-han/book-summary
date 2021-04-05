package jpabook.start.exception.service;

import jpabook.start.exception.jpo.member.Member;
import jpabook.start.exception.repository.MemberRepository;
import jpabook.start.exception.repository.NoResultExceptionTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Transactional
public class MemberService {
    @Autowired
    private NoResultExceptionTestRepository noResultExceptionTestRepository;

    @Autowired
    private MemberRepository memberRepository;

    public void register(Member member) {
        memberRepository.save(member);
    }

    public Member find(Long id) {
        return memberRepository.findOne(id);
    }

    public Member findAll() {
        return noResultExceptionTestRepository.findAllMember();
    }

}
