package study.ch_10_4.unitofwork;

import org.springframework.transaction.annotation.Transactional;
import study.ch_10_4.User;
import study.ch_10_4.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserApplicationService {
    private final UnitOfWork unitOfWork;
    private final IUserFactory userFactory;
    private final IUserRepository userRepository;
    private final UserService userService;

    public UserApplicationService(
            UnitOfWork unitOfWork,
            UserService userService,
            IUserFactory userFactory,
            IUserRepository userRepository
    ) {
        this.unitOfWork = unitOfWork;
        this.userService = userService;
        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(UserRegisterCommand command) throws SQLException {
        UserName userName = new UserName(command.getName());
        User user = userFactory.create(userName);

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("user: " + user);
        }

        userRepository.save(user);

        // 변경 결과가 반영되었음을 유닛오브워크에 알림
        unitOfWork.commit(user);
    }
}
