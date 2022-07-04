package com.example.designpattern.bridge;

// 구현부
// node를 사용하는 전통적인 LinkedList이다.
// thread-safe하지 않다.
public class SinglyLinkedList<T> implements LinkedList<T> {

    private class Node {
        private Object data;
        private Node next;
        private Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private int size;
    private Node top;
    private Node last;

    @Override
    public void addFirst(T element) {
        if (this.top == null) {
            this.last = this.top = new Node(element, null);
        } else {
            this.top = new Node(element, this.top);
        }
    }

    @Override
    public T removeFirst() {
        if (this.top == null) {
            return null;
        }
        T temp = (T) this.top.data;
        if (this.top.next != null) {
            this.top = this.top.next;
        } else {
            this.top = null;
            this.last = null;
        }
        size--;
        return temp;
    }

    @Override
    public void addLast(T element) {
        //
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
