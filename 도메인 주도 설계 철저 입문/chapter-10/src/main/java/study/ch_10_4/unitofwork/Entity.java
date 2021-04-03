package study.ch_10_4.unitofwork;

public abstract class Entity {
    private UnitOfWork unitOfWork;

    protected void markNew() {
        unitOfWork.getCurrent().registerNew(this);
    }

    protected void markClean() {
        unitOfWork.getCurrent().registerClean(this);
    }

    protected void markDirty() {
        unitOfWork.getCurrent().registerDirty(this);
    }

    protected void markDeleted() {
        unitOfWork.getCurrent().registerDeleted(this);
    }
}
