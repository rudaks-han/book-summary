package com.example.designpattern.singleton;

/**
 * lazy 초기화 홀더 클래스를 사용한 싱글톤 패턴
 * 이 방법은 동기화를 신경쓸 필요없는 lazy 초기화 방법이다.
 */
public class LazyRegistryIODH {

    private LazyRegistryIODH() {
        System.out.println("In LazyRegistryIODH singleton");
    }

    private static class RegistryHolder {
        // INSTANCE는 getInstance()를 사용할 때까지는 생성이 되지 않는다.
        static LazyRegistryIODH INSTANCE = new LazyRegistryIODH();
    }

    public static LazyRegistryIODH getInstance() {
        return RegistryHolder.INSTANCE;
    }
}
