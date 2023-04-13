package com.example.solid.lsp.before;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rectangle {

    private int width;

    private int height;

    public int computeArea() {
        return width * height;
    }
}
