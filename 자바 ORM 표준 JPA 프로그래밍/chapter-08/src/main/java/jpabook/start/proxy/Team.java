package jpabook.start.proxy;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Team(String name) {
        this.name = name;
    }

}
