package jpabook.start.nonidentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Parent5 {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Parent5(String name) {
        this.name = name;
    }
}
