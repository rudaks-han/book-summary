package jpabook.start;

import jpabook.start.jpo.Member;
import jpabook.start.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Override
    public void run(String... strings) throws Exception {
        Member member1 = new Member(1L, "한경만");
        Member member2 = new Member(2L, "김지훈");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Optional<Member> member = memberRepository.findById(1L);
        System.out.println("name: " + member.get().getName());
    }

}