package jpabook.start.jointable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Child6 {
    @Id
    @GeneratedValue
    @Column(name = "child_id")
    private Long id;

    private String name;

    public Child6(String name) {
        this.name = name;
    }
}
