package com.example.ch05._5_7;

/**
 * 스트리밍 서비스의 유저에 대한 자세한 사항을 갖는다.
 *
 * 이 클래스는 데이터베이스에 직접 연결하지 않는다. 대신 메모리에 저장된 값으로
 * 생성된다. 따라서 이 클래스가 생성된 이후에 데이터베이스에서 이뤄진 변경 사항을
 * 반영하지 않을 수 있다.
 */
public class User {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final Version signupVersion;

    public User(String username, String firstName, String lastName, Version signupVersion) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signupVersion = signupVersion;
    }

    private String getUserId() {
        if (signupVersion.isOlderThan("2.0")) {
            // (v2.0 이전에 등록한) 레거시 유저라는 이름으로 ID가 부여된다.
            // 자세한 내용은 #4218 이슈를 보라.
            return firstName.toLowerCase() + "." + lastName.toLowerCase();
        }
        // (v2.0 이후로 등록한) 새 유저는 username으로 ID가 부여된다.
        return username;
    }
}
