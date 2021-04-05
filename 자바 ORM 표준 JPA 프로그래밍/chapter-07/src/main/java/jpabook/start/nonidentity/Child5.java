package jpabook.start.nonidentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Child5 {

    @Id
    @GeneratedValue
    @Column(name = "child_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent5 parent;

    public Child5(String name, Parent5 parent) {
        this.name = name;
        this.parent = parent;
    }
}
