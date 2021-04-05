package jpabook.start.subselect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "orders3")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order3 {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Member4 member;
}
