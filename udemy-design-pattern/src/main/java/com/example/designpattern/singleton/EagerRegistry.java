package com.example.designpattern.singleton;

/**
 * 싱글톤 인스턴스로 eager 초기화를 사용한다.
 */
public class EagerRegistry {

    /**
     * 생성자를 private으로 만듦으로써 클래스 외부에서 객체 초기화를 못하게 하고 또한 상속을 제한할 수 있다.
     */
    private EagerRegistry() {
        // 초기화 코드
    }

    /**
     * 싱글톤 인스턴스. eager 초기화 싱글톤
     */
    private static final EagerRegistry INSTANCE = new EagerRegistry(); // 미리 생성해 놓는다.

    /**
     * 이 메소드는 외부 세계에 싱글톤 인스턴스를 리턴한다.
     */
    public static EagerRegistry getInstance() {
        return INSTANCE;
    }
}
