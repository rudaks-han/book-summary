package jpabook.start.batchsize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member3 {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @org.hibernate.annotations.BatchSize(size = 2)
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Order2> orders = new ArrayList<>();

    public Member3(String username) {
        this.username = username;
    }
}
