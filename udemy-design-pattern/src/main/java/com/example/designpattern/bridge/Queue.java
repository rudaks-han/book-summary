package com.example.designpattern.bridge;

// refined abstraction
public class Queue<T> implements FifoCollection<T> {

    private LinkedList<T> list;

    public Queue(LinkedList<T> list) {
        this.list = list;
    }

    @Override
    public void offer(T element) {
        this.list.addLast(element);
    }

    @Override
    public T poll() {
        return this.list.removeFirst();
    }
}
