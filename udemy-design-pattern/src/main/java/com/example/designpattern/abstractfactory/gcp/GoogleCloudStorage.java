package com.example.designpattern.abstractfactory.gcp;

import com.example.designpattern.abstractfactory.Storage;

// Google Cloud Platform의 구체적인 product를 나타낸다.
public class GoogleCloudStorage implements Storage {

    public GoogleCloudStorage(int capacityInMib) {
        // gcp api를 사용
        System.out.println("Allocated " + capacityInMib + " on Google Cloud Storage");
    }

    @Override
    public String getId() {
        return "gcpcs1";
    }

    @Override
    public String toString() {
        return "Google cloud storage";
    }
}
