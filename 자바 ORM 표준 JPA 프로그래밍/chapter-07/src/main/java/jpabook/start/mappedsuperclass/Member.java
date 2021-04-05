package jpabook.start.mappedsuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    private String email;

    public Member(String name, String email) {
        super(name);
        this.email = email;
    }
}
