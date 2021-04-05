package jpabook.start.manytooneoneway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team1 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Team1(String name) {
        this.name = name;
    }
}
