package com.example.designpattern.singleton;

public class Client {

    public static void main(String[] args) {
        EagerRegistry registry = EagerRegistry.getInstance(); // 인스턴스 생성
        EagerRegistry registry2 = EagerRegistry.getInstance(); // 또 다른 인스턴스 생성

        System.out.println(registry == registry2); // true <= 항상 같은 인스턴스

        LazyRegistryWithDCL lazySignleton1 = LazyRegistryWithDCL.getInstance();
        LazyRegistryWithDCL lazySignleton2 = LazyRegistryWithDCL.getInstance();

        System.out.println(lazySignleton1 == lazySignleton2); // true

        LazyRegistryIODH singleton = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton2 = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton3 = LazyRegistryIODH.getInstance();
        LazyRegistryIODH singleton4 = LazyRegistryIODH.getInstance();
        // In LazyRegistryIODH singleton <== 한번만 호출된다.
    }
}
