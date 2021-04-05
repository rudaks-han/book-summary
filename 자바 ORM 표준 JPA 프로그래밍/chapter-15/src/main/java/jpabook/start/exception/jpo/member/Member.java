package jpabook.start.exception.jpo.member;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Member {

    @Id
    private Long id;

    private String name;
}
