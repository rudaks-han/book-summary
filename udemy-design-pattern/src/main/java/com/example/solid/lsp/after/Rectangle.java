package com.example.solid.lsp.after;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rectangle implements Shape {

    private int width;

    private int height;

    public int computeArea() {
        return width * height;
    }
}
