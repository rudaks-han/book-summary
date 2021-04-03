package study.ch_13_2;

public interface ISpecification<T> {
    boolean isSatisfiedBy(T value);
}
