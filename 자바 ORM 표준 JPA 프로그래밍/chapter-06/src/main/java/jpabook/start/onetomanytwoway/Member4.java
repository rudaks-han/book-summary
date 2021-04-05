package jpabook.start.onetomanytwoway;

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
public class Member4 {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    public Member4(String username) {
        this.username = username;
    }

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team4 team;
}
