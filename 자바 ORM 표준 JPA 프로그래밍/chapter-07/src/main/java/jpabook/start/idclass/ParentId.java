package jpabook.start.idclass;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ParentId implements Serializable {

    private String id1;

    private String id2;
}
