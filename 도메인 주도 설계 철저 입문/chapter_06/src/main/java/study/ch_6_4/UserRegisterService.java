package study.ch_6_4;

import study.ch_6_2.*;

public class UserRegisterService
{
    private final IUserRepository userRepository;
    private final UserService userService;

    public UserRegisterService(IUserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void handle(UserRegisterCommand command) {
        UserName userName = new UserName(command.getName());

        User user = new User(userName);

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("이미 등록된 사용자명임: " + user.getName());
        }

        userRepository.save(user);
    }
}
