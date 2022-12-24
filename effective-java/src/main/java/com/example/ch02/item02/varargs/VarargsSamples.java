package com.example.ch02.item02.varargs;

import java.util.Arrays;

public class VarargsSamples {

    public void printNumbers(int... numbers) {
        Arrays.stream(numbers).forEach(System.out::println);
    }

    public static void main(String[] args) {
        VarargsSamples samples = new VarargsSamples();
        samples.printNumbers(5, 10);
    }
}
