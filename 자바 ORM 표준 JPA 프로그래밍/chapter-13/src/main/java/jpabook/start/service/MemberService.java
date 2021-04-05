package jpabook.start.service;

import jpabook.start.jpo.Member;
import jpabook.start.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void register(Member member) {
        memberRepository.save(member);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member find(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("member id not exists: " + id));

       // member.getTeam();

        return member;
    }
}
