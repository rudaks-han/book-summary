package spectra.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member1 {
    @Id
    private String id;
    private String username;

    // 연관관계 매핑
    @OneToMany(mappedBy = "member1")
    private List<Order1> order1 = new ArrayList<Order1>();

    public Member1(String id, String username)
    {
        this.id = id;
        this.username = username;
    }

}
