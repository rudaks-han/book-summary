package jpabook.start.embeddedididentity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GrandChildId4 implements Serializable {

    private ChildId4 childId;

    @Column(name = "grandchild_id")
    private String id;
}
