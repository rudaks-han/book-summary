package jpabook.start.study;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Team10 {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Member10> members = new ArrayList<>();

    public Team10(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
