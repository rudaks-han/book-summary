package study.ch_4_1;

public class User {
    private UserId id;
    private UserName name;

    public User(UserId id, UserName name) {
        this.id = id;
        this.name = name;
    }

    public boolean exists(User user) {
        // 사용자명 중복을 확인하는 코드
        return false;
    }

    public static void main(String[] args) {
        UserId userId = new UserId("id");
        UserName userName = new UserName("smith");
        User user = new User(userId, userName);

        // 새로 만든 객체에 중복 여부를 묻는 상황이 됨
        boolean duplicateCheckResult = user.exists(user);
        System.out.println(duplicateCheckResult);
    }
}
