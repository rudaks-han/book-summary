package com.example.designpattern.singleton;

/**
 * 이 클래스는 Double Checked Locking 사용하는, 전통적인 싱글톤 방법을 나타낸다.
 * 또한 lazy 초기화 싱글톤이다.
 * 이 구현방법이 volatile과 double check locking을 사용한 lazy 초기화로 멀티쓰레드 이슈를 해결할 수는 있지만, volatile 키워드는 Java 1.5 이상에서만 동작한다.
 */
public class LazyRegistryWithDCL {

    /**
     * private 생성자로 하여금 외부에서 객체 생성을 못하게 하고 서브클래싱을 불가능하게 한다.
     */
    private LazyRegistryWithDCL() {
    }

    // volatile 키워드는 CPU 캐쉬값을 사용하지 않고 메모리를 접근하도록 한다.
    private static volatile LazyRegistryWithDCL INSTANCE;

    /**
     * double checking locking을 사용한다.
     * @return
     */
    public static LazyRegistryWithDCL getInstance() {
        if (INSTANCE == null) {
            synchronized (LazyRegistryWithDCL.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LazyRegistryWithDCL();
                    // 기본적으로 멀티 쓰레드 환경에서는 CPU 레지스터에 변수 값을 저장한다.
                }
            }
        }

        return INSTANCE;
    }
}
