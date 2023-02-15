package com.example.design.bridge;

// 여기가 추상부이다.
// FIFO 컬렉션을 나타낸다.
public interface FifoCollection<T> {

    // 엘리먼트를 추가
    void offer(T element);

    // 컬렉션에서 첫 엘리먼트를 삭제하고 리턴한다.
    T poll();
}
