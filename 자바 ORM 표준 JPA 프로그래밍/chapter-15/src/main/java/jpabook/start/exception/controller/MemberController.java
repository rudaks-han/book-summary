package jpabook.start.exception.controller;

import jpabook.start.exception.jpo.member.Member;
import jpabook.start.exception.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public Member findAll() {
        return memberService.findAll();
    }

    public void register(Member member) {
        memberService.register(member);
    }

    @GetMapping("{id}")
    public void find(@PathVariable Long id) {

        Member member = memberService.find(id);

        /*System.out.println("--- find Member ---");
        System.out.println("member : " + member.getName());*/
    }
}
