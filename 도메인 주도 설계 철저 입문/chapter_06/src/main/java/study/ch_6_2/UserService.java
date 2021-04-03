package study.ch_6_2;

public class UserService
{
    private IUserRepository userRepository;
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean exists(User user) {
        User duplicatedUser = userRepository.find(user.getName());

        return duplicatedUser != null;
    }
}
