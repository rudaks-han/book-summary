package study.ch_2_5;

public class Example_2_5 {
    public static void ex_2_33() {
        String modelNumber = "a20421-100-1";
    }

    public static void ex_2_36() {
        String username = "me";
    }

    public static void ex_2_37() {
        String userName = "me";

        if (userName.length() >= 3) {
            // 유효한 값으로 처리를 계속한다.
        } else {
            throw new IllegalArgumentException("유효하지 않은 값");
        }
    }

    void createUser(String name) {
        if (name == null)
            throw new IllegalArgumentException("value : " + name);
        if (name.length() < 3)
            throw new IllegalArgumentException("사용자명은 3글자 이상어야함. value: " + name);


    }

    void updateUser(String id, String name) {
        if (name == null)
            throw new IllegalArgumentException("value : " + name);
        if (name.length() < 3)
            throw new IllegalArgumentException("사용자명은 3글자 이상어야함. value: " + name);

    }

}
