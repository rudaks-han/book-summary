package com.example.design.proxy;

import javafx.geometry.Point2D;

public class BitmapImage implements Image {

    private Point2D location;

    private String name;

    public BitmapImage(String filename) {
        System.out.println("Loaded from disk: " + filename);
        this.name = filename;
    }

    @Override
    public void setLocation(Point2D point2D) {
        this.location = point2D;
    }

    @Override
    public Point2D getLocation() {
        return this.location;
    }

    @Override
    public void render() {
        System.out.println("Rendered " + this.name);
    }
}
