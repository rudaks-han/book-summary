package jpabook.start.embeddedididentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GrandChild4 {

    @EmbeddedId
    private GrandChildId4 id;

    @MapsId("childId")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "parent_id"),
        @JoinColumn(name = "child_id")
    })
    private Child4 child;

    private String name;

}
