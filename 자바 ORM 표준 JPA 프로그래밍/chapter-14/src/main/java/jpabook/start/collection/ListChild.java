package jpabook.start.collection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ListChild {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
