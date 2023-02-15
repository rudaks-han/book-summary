package com.example.design.bridge;

// 정제된 추상부이다.
public class Queue<T> implements FifoCollection<T> {

    private LinkedList<T> list;

    public Queue(LinkedList<T> list) {
        this.list = list;
    }

    @Override
    public void offer(T element) {
        list.addLast(element);
    }

    @Override
    public T poll() {
        return list.removeFirst();
    }
}
