package jpabook.start.embeddedid;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
//@Embeddable
public class ParentId2 implements Serializable {

    private String id1;

    private String id2;
}
