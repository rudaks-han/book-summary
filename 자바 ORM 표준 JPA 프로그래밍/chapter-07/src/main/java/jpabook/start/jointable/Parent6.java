package jpabook.start.jointable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Parent6 {
    @Id
    @GeneratedValue
    @Column(name = "parent_id")
    private Long id;

    private String name;

    @OneToOne
    @JoinTable(
            name = "parent_child",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private Child6 child;

    public Parent6(String name, Child6 child) {
        this.name = name;
        this.child = child;
    }
}
