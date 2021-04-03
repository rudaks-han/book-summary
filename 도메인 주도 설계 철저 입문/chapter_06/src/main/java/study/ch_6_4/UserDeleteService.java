package study.ch_6_4;

import study.ch_6_2.User;
import study.ch_6_2.UserId;
import study.ch_6_2.UserNotFoundException;

public class UserDeleteService {
    private final IUserRepository userRepository;

    public UserDeleteService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handle(UserDeleteCommand command) {
        UserId userId = new UserId(command.getId());
        User user = userRepository.find(userId);

        if (user == null) {
            throw new UserNotFoundException(userId.getValue());
        }

        userRepository.delete(user);
    }
}
