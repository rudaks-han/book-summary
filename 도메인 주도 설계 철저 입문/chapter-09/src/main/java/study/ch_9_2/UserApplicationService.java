package study.ch_9_2;

import java.sql.SQLException;

public class UserApplicationService {
    private IUserFactory userFactory;
    private IUserRepository userRepository;
    private UserService userService;

    /*public void register(UserRegisterCommand command) throws SQLException {
        UserName userName = new UserName(command.getName());
        // 팩토리를 이용한 인스턴스 생성
        User user = userFactory.create(userName);

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("user: " + user);
        }

        userRepository.save(user);
    }*/
    public void register(UserRegisterCommand command) throws SQLException {
        UserName userName = new UserName(command.getName());
        User user = new User(
                userRepository.nextIdentity(),
                userName
        );
    }
}
