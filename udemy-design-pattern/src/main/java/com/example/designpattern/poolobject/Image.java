package com.example.designpattern.poolobject;

import javafx.geometry.Point2D;

// 재사용 가능한 추상 클래스를 나타낸다.
public interface Image extends Poolable {

    void draw();

    Point2D getLocation();

    void setLocation(Point2D location);
}
