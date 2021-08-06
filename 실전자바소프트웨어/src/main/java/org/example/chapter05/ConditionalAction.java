package org.example.chapter05;

public interface ConditionalAction {
    void perform(Facts facts);
    boolean evaluate(Facts facts);

}