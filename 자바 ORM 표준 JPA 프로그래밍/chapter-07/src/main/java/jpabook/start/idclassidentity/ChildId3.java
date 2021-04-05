package jpabook.start.idclassidentity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChildId3 implements Serializable {

    private String parent;

    private String childId;
}
