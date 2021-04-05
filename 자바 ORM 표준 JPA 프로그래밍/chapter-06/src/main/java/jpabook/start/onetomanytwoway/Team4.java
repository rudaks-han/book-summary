package jpabook.start.onetomanytwoway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team4 {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "team_id"/*, insertable = false, updatable = false*/)
    private List<Member4> members = new ArrayList<>();

    public Team4(String name) {
        this.name = name;
    }
}
