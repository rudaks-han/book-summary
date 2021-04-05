package jpabook.start.manytooneoneway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member1 {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @ManyToOne
    private Team1 team;

    public Member1(String username, Team1 team) {
        this.username = username;
        this.team = team;
    }
}
