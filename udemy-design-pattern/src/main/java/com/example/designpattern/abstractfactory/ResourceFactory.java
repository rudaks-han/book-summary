package com.example.designpattern.abstractfactory;

// 각 유형에 정의된 메소드의 추상 팩토리
public interface ResourceFactory {

    Instance createInstance(Instance.Capacity capacity);

    Storage createStorage(int capMib);
}
