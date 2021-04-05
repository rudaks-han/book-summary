package jpabook.start.idclassidentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@IdClass(ChildId3.class)
public class Child3 {
    @Id
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent3 parent;

    @Id
    @Column(name = "child_id")
    private String childId;

    private String name;
}
