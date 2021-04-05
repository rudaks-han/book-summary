package jpabook.start.onetooneprimaryoneway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker5 locker;

    public Member5(String username, Locker5 locker) {
        this.username = username;
        this.locker = locker;
    }
}
