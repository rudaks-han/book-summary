package com.example.designpattern.bridge;

/**
 * 추상부
 * First In First Out 컬렉션을 나타낸다.
 */
public interface FifoCollection<T> {

    void offer(T element);

    T poll();
}
