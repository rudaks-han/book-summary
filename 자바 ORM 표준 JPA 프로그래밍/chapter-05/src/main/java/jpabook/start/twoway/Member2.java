package jpabook.start.twoway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member2 {
    @Id
    private String id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team2 team;

    public Member2(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setTeam(Team2 team) {
        this.team = team;

        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        team.getMembers().add(this);
    }

}
