package com.example.design.prototype;

public class Client {

    public static void main(String[] args) throws CloneNotSupportedException {
        Swordman s1 = new Swordman();
        s1.move(new Point3D(-10, 0, 0), 20);
        s1.attack();

        System.out.println(s1);

        Swordman s2 = (Swordman) s1.clone();
        System.out.println(s1 == s2);
        System.out.println(s2);
    }
}
