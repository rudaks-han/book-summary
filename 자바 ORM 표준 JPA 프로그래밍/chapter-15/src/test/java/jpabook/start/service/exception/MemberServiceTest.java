package jpabook.start.service.exception;

import jpabook.start.Application;
import jpabook.start.exception.jpo.member.Member;
import jpabook.start.exception.repository.MemberRepository;
import jpabook.start.exception.service.MemberService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void test() throws Exception {
        Member member = new Member(1L, "한경만");
        memberService.register(member);

        Member findMember = memberRepository.findOne(1L);

        Assert.assertTrue(member == findMember);
        //Assert.assertTrue(member.getId().equals(findMember.getId()));
    }
}
