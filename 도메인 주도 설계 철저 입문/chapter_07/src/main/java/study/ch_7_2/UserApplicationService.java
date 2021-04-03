package study.ch_7_2;

public class UserApplicationService {
    private final IUserRepository userRepository;

    public UserApplicationService() {
        this.userRepository = new InMemoryUserRepository();
    }
}
