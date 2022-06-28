package com.example.designpattern.singleton;

/**
 * 싱글톤 인스턴스로 eager 초기화를 사용한다.
 */
public class EagerRegistry {

    private EagerRegistry() {}

    private static final EagerRegistry INSTANCE = new EagerRegistry(); // 미리 생성해 놓는다.

    public static EagerRegistry getInstance() {
        return INSTANCE;
    }
}
