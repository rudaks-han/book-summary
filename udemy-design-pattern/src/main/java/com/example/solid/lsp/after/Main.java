package com.example.solid.lsp.after;

public class Main {

    public static void main(String[] args) {
        Shape rectangle = new Rectangle(10, 20);
        System.out.println(rectangle.computeArea());

        Shape square = new Square(10);
        System.out.println(square.computeArea());
    }
}
