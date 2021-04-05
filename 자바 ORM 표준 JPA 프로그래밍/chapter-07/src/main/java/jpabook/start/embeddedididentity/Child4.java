package jpabook.start.embeddedididentity;

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
public class Child4 {

    @EmbeddedId
    private ChildId4 id;

    @MapsId("parentId")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Parent4 parent;

    private String name;
}
