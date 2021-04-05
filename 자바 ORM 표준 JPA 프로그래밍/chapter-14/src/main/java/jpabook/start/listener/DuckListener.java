package jpabook.start.listener;

import javax.persistence.*;

public class DuckListener {
    @PrePersist
    private void prePersist(Object obj) {
        System.out.println("### Duck.prePersist obj : " + obj);
    }

    @PostPersist
    private void postPersist(Object obj) {
        System.out.println("### Duck.postPersist obj : " + obj);
    }

    @PostLoad
    private void postLoad(Object obj) {
        System.out.println("### Duck.postLoad obj : " + obj);
    }

    @PreRemove
    private void preRemove(Object obj) {
        System.out.println("### Duck.preRemove obj : " + obj);
    }

    @PostRemove
    private void postRemove(Object obj) {
        System.out.println("### Duck.postRemove obj : " + obj);
    }
}
