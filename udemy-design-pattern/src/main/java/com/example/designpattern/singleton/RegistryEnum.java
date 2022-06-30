package com.example.designpattern.singleton;

/**
 * Ref. Google I/O 2008 Joshua Bloch
 * Java 1.5부터 사용 가능.
 * Java 내부 메커니즘을 사용하여 직렬화를 구현하고 싱글톤을 보장한다.
 */
public enum RegistryEnum {

    INSTANCE;

    public void getConfiguration() {

    }
}
