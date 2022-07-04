package com.example.designpattern.bridge;

/**
 * 구현부
 * 구현부는 추상 구조와 전혀 관련이 없는 자신만의 구조를 정의하고 있다.
 */
public interface LinkedList<T> {

    void addFirst(T element);

    T removeFirst();

    void addLast(T element);

    T removeLast();

    int getSize();
}
