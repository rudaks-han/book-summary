package study.ch_10_4;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

public class UserApplicationService {
    // 리포지토리가 가진 것과 같은 커넥션 객체
    private final Connection connection;
    private IUserFactory userFactory;
    private IUserRepository userRepository;
    private UserService userService;

    public UserApplicationService(
            Connection connection,
            UserService userService,
            IUserFactory userFactory,
            IUserRepository userRepository
    ) {
        this.connection = connection;
        this.userService = userService;
        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(UserRegisterCommand command) throws SQLException {
        connection.setAutoCommit(false);

        UserName userName = new UserName(command.getName());
        User user = userFactory.create(userName);

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("user: " + user);
        }

        userRepository.save(user);
        //처리가 완료되면 커밋
        connection.commit();
    }
}
