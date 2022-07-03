package com.example.designpattern.abstractfactory.aws;

import com.example.designpattern.abstractfactory.Instance;
import com.example.designpattern.abstractfactory.Storage;

// Amazon Web Service 제품군을 나타낸다.
public class Ec2Instance implements Instance {

    public Ec2Instance(Capacity capacity) {
        // ec2 instance 유형. aws API를 사용
        System.out.println("Created Ec2Instance");
    }

    @Override
    public void start() {
        System.out.println("Ec2Instance started");
    }

    @Override
    public void attachStorage(Storage storage) {
        System.out.println("Attched " + storage + " to Ec2Instance");
    }

    @Override
    public void stop() {
        System.out.println("Ec2Instance stopped");
    }

    @Override
    public String toString() {
        return "Ec2Instance";
    }
}
