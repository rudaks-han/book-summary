package jpabook.start.delete;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team3 {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private List<Member3> members = new ArrayList<>();

    public Team3(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addMember(Member3 member) {
        this.members.add(member);
        if (member.getTeam() != this) {
            member.setTeam(this);
        }
    }
}
