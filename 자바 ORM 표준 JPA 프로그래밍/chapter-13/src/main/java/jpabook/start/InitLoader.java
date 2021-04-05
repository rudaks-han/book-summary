package jpabook.start;

import jpabook.start.controller.MemberController;
import jpabook.start.controller.TeamController;
import jpabook.start.jpo.Member;
import jpabook.start.jpo.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitLoader implements CommandLineRunner {

    @Autowired
    private MemberController memberController;

    @Autowired
    private TeamController teamController;

    @Override
    public void run(String... strings) throws Exception {
        Team team1 = new Team("개발팀");
        Team team2 = new Team("연구소");

        Member member1 = new Member(1L, "한경만", team1);
        Member member2 = new Member(2L, "김지훈", team2);

        teamController.register(team1);
        teamController.register(team2);

        memberController.register(member1);
        memberController.register(member2);


    }

}