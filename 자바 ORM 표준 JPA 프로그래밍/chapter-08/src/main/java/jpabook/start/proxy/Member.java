package jpabook.start.proxy;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public Member(String id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }
}
