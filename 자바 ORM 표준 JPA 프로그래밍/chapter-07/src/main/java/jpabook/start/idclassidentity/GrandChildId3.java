package jpabook.start.idclassidentity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GrandChildId3 implements Serializable {

    private ChildId3 child;

    private String id;
}
