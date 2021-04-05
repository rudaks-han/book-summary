package jpabook.start.twoway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team2 {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member2> members = new ArrayList<>();

    public Team2(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addMember(Member2 member) {
        this.members.add(member);
        if (member.getTeam() != this) {
            member.setTeam(this);
        }
    }
}
