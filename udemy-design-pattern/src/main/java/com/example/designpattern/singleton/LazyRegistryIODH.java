package com.example.designpattern.singleton;

/**
 * lazy 초기화 홀더 클래스를 사용한 싱글톤 패턴
 * 이 방법은 동기화를 신경쓸 필요없는 lazy 초기화 방법이다.
 */
public class LazyRegistryIODH {

    /**
     * 생성자를 private으로 만듦으로써 클래스 외부에서 객체 초기화를 못하게 하고 또한 상속을 제한할 수 있다.
     */
    private LazyRegistryIODH() {
        System.out.println("In LazyRegistryIODH singleton");
    }

    /**
     * 이 클래스는 holder 패턴의 초기화를 제공한다.
     */
    private static class RegistryHolder {
        // INSTANCE는 getInstance()를 사용할 때까지는 생성이 되지 않는다.
        static LazyRegistryIODH INSTANCE = new LazyRegistryIODH();
    }

    /**
     * 이 메소드는 싱글톤 인스턴스를 제공한다.
     */
    public static LazyRegistryIODH getInstance() {
        return RegistryHolder.INSTANCE;
    }
}
