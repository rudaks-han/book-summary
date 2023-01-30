package com.example.solid.lsp.after;

import lombok.Setter;

@Setter
public class Square implements Shape {

    private int side;

    public Square(int side) {
        this.side = side;
    }

    @Override
    public int computeArea() {
        return side * side;
    }
}
