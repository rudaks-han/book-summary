package jpabook.start.study;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member10 {
    @Id
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private Team10 team;

    public Member10(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
