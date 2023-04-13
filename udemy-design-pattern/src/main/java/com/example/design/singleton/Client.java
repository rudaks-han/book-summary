package com.example.design.singleton;

public class Client {

    public static void main(String[] args) {
        EagerRegistry registry1 = EagerRegistry.getInstance();
        EagerRegistry registry2 = EagerRegistry.getInstance();
        System.out.println(registry1 == registry2); // true

        LazyRegistryWithDCL lazySingleton1 = LazyRegistryWithDCL.getInstance();
        LazyRegistryWithDCL lazySingleton2 = LazyRegistryWithDCL.getInstance();
        System.out.println(lazySingleton1 == lazySingleton2); // true

        LazyRegistryIODH lazyRegistryIODH1 = LazyRegistryIODH.getInstance();
        LazyRegistryIODH lazyRegistryIODH2 = LazyRegistryIODH.getInstance();
        System.out.println(lazyRegistryIODH1 == lazyRegistryIODH2); // true

        RegistryEnum registryEnum1 = RegistryEnum.INSTANCE;
        RegistryEnum registryEnum2 = RegistryEnum.INSTANCE;
        System.out.println(registryEnum1 == registryEnum2); // true
    }
}
