package jpabook.start.onetooneprimarytwoway;

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
public class Member6 {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker6 locker;

    public Member6(String username, Locker6 locker) {
        this.username = username;
        this.locker = locker;
    }
}
