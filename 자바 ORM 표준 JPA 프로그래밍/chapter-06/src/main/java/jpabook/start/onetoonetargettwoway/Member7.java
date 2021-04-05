package jpabook.start.onetoonetargettwoway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member7 {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @OneToOne(mappedBy = "member")
    private Locker7 locker;

    public Member7(String username, Locker7 locker) {
        this.username = username;
        this.locker = locker;
    }
}
