package com.example.designpattern.abstractfactory.gcp;

import com.example.designpattern.abstractfactory.*;

// Google cloud platform 의 팩토리 구현체
public class GoogleResourceFactory implements ResourceFactory {

    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new GoogleComputeEngineInstance(capacity);
    }

    @Override
    public Storage createStorage(int capMib) {
        return new GoogleCloudStorage(capMib);
    }
}
