package jpabook.start.onetomanyoneway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member3 {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    public Member3(String username) {
        this.username = username;
    }
}
