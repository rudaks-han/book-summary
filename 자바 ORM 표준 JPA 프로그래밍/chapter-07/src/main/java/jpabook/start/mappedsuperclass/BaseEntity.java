package jpabook.start.mappedsuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public BaseEntity(String name) {
        this.name = name;
    }
}
