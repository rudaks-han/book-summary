package study.ch_4_2;

public class UserService {
    public boolean exists(User user) {
        // 사용자명 중복을 확인
        return false;
    }

    public void changeName(User user, UserName userName) {
        if (user == null)
            throw new IllegalArgumentException("user: " + user);
        if (userName == null)
            throw new IllegalArgumentException("userName: " + userName);

        user.setName(userName);
    }

    public static void main(String[] args) {
        UserService userService = new UserService();

        UserId userId = new UserId("id");
        UserName userName = new UserName("john");
        User user = new User(userId, userName);

        // 도메인 서비스에 요청하기
        boolean duplicateCheckResult = userService.exists(user);
        System.out.println(duplicateCheckResult);
    }
}
