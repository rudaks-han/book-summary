package com.example.designpattern.prototype;

import javafx.geometry.Point3D;
import lombok.Getter;

/**
 * 이 클래스는 추상 프로토타입을 나타내고 clone 메소드를 정의한다.
 */
@Getter
public abstract class GameUnit implements Cloneable {

    private Point3D position;

    public GameUnit() {
        this.position = Point3D.ZERO;
    }

    @Override
    protected GameUnit clone() throws CloneNotSupportedException {
        GameUnit unit = (GameUnit) super.clone();
        unit.initialize();
        return unit;
    }

    protected void initialize() {
        this.position = Point3D.ZERO;
        reset();
    }

    protected abstract void reset();

    public GameUnit(float x, float y, float z) {
        this.position = new Point3D(x, y, z);
    }

    public void move(Point3D direction, float distance) {
        Point3D finalMove = direction.normalize();
        finalMove = finalMove.multiply(distance);
        this.position = this.position.add(finalMove);
    }
}
