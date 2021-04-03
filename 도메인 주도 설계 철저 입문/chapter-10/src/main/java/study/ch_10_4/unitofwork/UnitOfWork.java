package study.ch_10_4.unitofwork;

public class UnitOfWork {
    void registerNew(Object value) {}
    void registerDirty(Object value) {}
    void registerClean(Object value) {}
    void registerDeleted(Object value) {}
    void commit(Object value) {}

    public UnitOfWork getCurrent() {
        return this;
    }
}
