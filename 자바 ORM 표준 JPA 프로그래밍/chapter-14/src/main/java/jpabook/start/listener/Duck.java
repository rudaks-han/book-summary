package jpabook.start.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(DuckListener.class)
public class Duck {
    @Id
    private Long id;

    private String name;

    /*@PrePersist
    private void prePersist() {
        System.out.println("### Duck.prePersist id : " + id);
    }

    @PostPersist
    private void postPersist() {
        System.out.println("### Duck.postPersist id : " + id);
    }

    @PostLoad
    private void postLoad() {
        System.out.println("### Duck.postLoad id : " + id);
    }

    @PreRemove
    private void preRemove() {
        System.out.println("### Duck.preRemove id : " + id);
    }

    @PostRemove
    private void postRemove() {
        System.out.println("### Duck.postRemove id : " + id);
    }*/
}
