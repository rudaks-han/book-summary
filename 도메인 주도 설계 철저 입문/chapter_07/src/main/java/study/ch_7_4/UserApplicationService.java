package study.ch_7_4;

public class UserApplicationService {
    private IUserRepository userRepository;

    public UserApplicationService() {
        // IUserRepository의 의존 관계 해소 대상이 설정되어 있지 않으므로 예외 발생
        this.userRepository = ServiceLocator.resolve();
    }

    public void register(UserRegisterCommand command) {

    }


}
