package jpabook.start;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@Getter
@Setter
@NamedQuery(
    name = "Member.findByName",
    query = "select m from Member m where m.name = :name"
)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int age;

    @Embedded
    private Address address;

    @ManyToOne
    private Team team;
}
