package jpabook.start.batchsize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "orders2")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order2 {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Member3 member;
}
