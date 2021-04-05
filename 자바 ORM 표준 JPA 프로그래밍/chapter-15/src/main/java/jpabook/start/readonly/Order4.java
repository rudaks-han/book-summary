package jpabook.start.readonly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "orders4")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order4 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    public Order4(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
