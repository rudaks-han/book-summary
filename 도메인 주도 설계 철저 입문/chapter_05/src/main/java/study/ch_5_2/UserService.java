package study.ch_5_2;

public class UserService {
    private IUserRepository userRepository;
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean exists(User user) {
        // 중복 확인이 사용자명 기준이라는 지식이 도메인 객체에서 누락된다.
        return userRepository.exists(user.getName());
    }
}
