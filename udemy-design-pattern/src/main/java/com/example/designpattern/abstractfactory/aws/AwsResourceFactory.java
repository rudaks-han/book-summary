package com.example.designpattern.abstractfactory.aws;

import com.example.designpattern.abstractfactory.Instance;
import com.example.designpattern.abstractfactory.ResourceFactory;
import com.example.designpattern.abstractfactory.Storage;

public class AwsResourceFactory implements ResourceFactory {

    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new Ec2Instance(capacity);
    }

    @Override
    public Storage createStorage(int capMib) {
        return new S3Storage(capMib);
    }
}
