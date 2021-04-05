package jpabook.start.ddl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "member22")
public class Member2 {

    @Id
    @Column
    private String id;

    @Column
    private String username;

    private int age;

    public Member2(String id, String username) {
        this.id = id;
        this.username = username;
        //this.age = age;
    }
}
