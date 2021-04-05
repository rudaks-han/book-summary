package jpabook.start.idclassidentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GrandChildId3.class)
@Entity
public class GrandChild3 {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "parent_id"),
            @JoinColumn(name = "child_id")
    })
    private Child3 child;

    @Id
    @Column(name = "grandchild_id")
    private String id;

    private String name;
}
