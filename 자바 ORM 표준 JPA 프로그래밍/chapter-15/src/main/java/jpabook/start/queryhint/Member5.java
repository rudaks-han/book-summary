package jpabook.start.queryhint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member5 {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    public Member5(String username) {
        this.username = username;
    }
}
