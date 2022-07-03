package com.example.designpattern.abstractfactory.aws;

import com.example.designpattern.abstractfactory.Storage;

// Amazon Web Service의 구체적인 product를 나타낸다.
public class S3Storage implements Storage {

    public S3Storage(int capacityInMib) {
        // aws s3 api 사용
        System.out.println("Allocated " + capacityInMib + " on S3");
    }

    @Override
    public String getId() {
        return "S31";
    }

    @Override
    public String toString() {
        return "S3 Storage";
    }
}
