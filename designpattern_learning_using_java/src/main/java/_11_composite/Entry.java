package _11_composite;

public abstract class Entry {

    public abstract String getName();

    public abstract int getSize();

    public void printList() {
        printList("");
    }

    protected abstract void printList(String prefix);

    @Override
    public String toString() {
        return getName() + " (" + getSize()+ ")";
    }
}
