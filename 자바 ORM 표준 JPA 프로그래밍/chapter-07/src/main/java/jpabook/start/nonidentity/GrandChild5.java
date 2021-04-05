package jpabook.start.nonidentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class GrandChild5 {

    @Id
    @GeneratedValue
    @Column(name = "grandchild_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child5 child;

    public GrandChild5(String name, Child5 child) {
        this.name = name;
        this.child = child;
    }
}
