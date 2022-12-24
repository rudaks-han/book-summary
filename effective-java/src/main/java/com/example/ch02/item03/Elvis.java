package com.example.ch02.item03;

public class Elvis {

    public static final Elvis INSTANCE = new Elvis();

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue! Christmas without you!");
    }

    public static void main(String[] args) {
        Elvis elvis = Elvis.INSTANCE;
        elvis.sing();

    }

}
