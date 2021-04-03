package study.ch_6_4;

import study.ch_6_2.*;

public class UserApplicationService {
    private final IUserRepository userRepository;
    private final UserService userService;

    public UserApplicationService(IUserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void register(String name, String mailAddress) {
        User user = new User(
                new UserName(name)
        );

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("이미 등록된 사용자명임: " + user.getName());
        }

        userRepository.save(user);
    }

    public void delete(UserDeleteCommand command) {
        UserId targetId = new UserId(command.getId());
        User user = userRepository.find(targetId);

        if (user == null) {
            throw new UserNotFoundException(user.getId().getValue());
        }

        userRepository.delete(user);
    }
}
