package org.example.chapter05;

@FunctionalInterface
public interface Condition {

    boolean evaluate(Facts facts);
}
