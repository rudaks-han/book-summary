package jpabook.start.delete;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member3 {
    @Id
    private String id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team3 team;

    public Member3(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setTeam(Team3 team) {
        this.team = team;

        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        team.getMembers().add(this);
    }
}
