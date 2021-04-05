package jpabook.start.singletable;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item2 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;
}