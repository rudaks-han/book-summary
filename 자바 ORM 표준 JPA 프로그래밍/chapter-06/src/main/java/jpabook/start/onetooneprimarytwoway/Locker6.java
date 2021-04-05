package jpabook.start.onetooneprimarytwoway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Locker6 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Locker6(String name) {
        this.name = name;
    }

    @OneToOne
    private Member6 member;
}
