package jpabook.start.collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parent {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @JoinColumn
    private Collection<CollectionChild> collection = new ArrayList<>();

    @OneToMany
    @JoinColumn
    private List<ListChild> list = new ArrayList<>();
}
