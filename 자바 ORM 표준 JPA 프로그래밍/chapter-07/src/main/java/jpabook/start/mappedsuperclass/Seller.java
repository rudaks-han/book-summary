package jpabook.start.mappedsuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends BaseEntity {
    private String shopName;

    public Seller(String name, String shopName) {
        super(name);
        this.shopName = shopName;
    }
}
