package com.example.design.abstractfactory;

public interface ResourceFactory {

    Instance createInstance(Instance.Capacity capacity);

    Storage createStorage(int capMib);
}
