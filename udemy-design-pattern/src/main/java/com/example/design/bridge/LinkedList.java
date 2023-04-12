package com.example.design.bridge;

// 여기가 구현부이다.
// 구현부는 추상 구조와 관련없이 자신만의 구조를 정의한다.
public interface LinkedList<T> {

    void addFirst(T element);

    T removeFirst();

    void addLast(T element);

    T removeLast();

    int getSize();
}
