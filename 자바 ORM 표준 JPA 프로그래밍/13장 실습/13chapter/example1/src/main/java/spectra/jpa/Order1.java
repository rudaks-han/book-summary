package spectra.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order1 {

    @Id @GeneratedValue
    private String id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member1 member1;

    public Order1(String id, Member1 member1)
    {
        this.id = id;
        this.member1 = member1;
    }

    public Order1(String id)
    {
        this.id = id;
    }

}


