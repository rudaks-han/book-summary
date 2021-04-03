package study.ch_7_4;

public class ServiceLocator {
    private static IUserRepository userRepository;

    public static IUserRepository resolve() {
        return userRepository;
    }

    public static void register(IUserRepository userRepository) {
        userRepository = userRepository;
    }
}
