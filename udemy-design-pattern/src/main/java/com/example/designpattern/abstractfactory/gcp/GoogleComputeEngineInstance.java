package com.example.designpattern.abstractfactory.gcp;

import com.example.designpattern.abstractfactory.Instance;
import com.example.designpattern.abstractfactory.Storage;

public class GoogleComputeEngineInstance implements Instance {

    public GoogleComputeEngineInstance(Capacity capacity) {
        // GCP 인스턴스 유형. GCP API를 사용
        System.out.println("Created Google Compute Engine instance");
    }

    @Override
    public void start() {
        System.out.println("Compute Engine instance started");
    }

    @Override
    public void attachStorage(Storage storage) {
        System.out.println("Attched " + storage + " to Compute engine instance");
    }

    @Override
    public void stop() {
        System.out.println("Compute engine instance stopped");
    }
}
