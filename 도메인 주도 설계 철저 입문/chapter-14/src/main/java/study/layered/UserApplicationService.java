package study.layered;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserApplicationService {
    private final IUserFactory userFactory;
    private final IUserRepository userRepository;
    private final UserService userService;

    public UserGetResult get(UserGetCommand command) {
        UserId id = new UserId(command.getId());
        User user = userRepository.find(id);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾지 못했음: " + id);
        }

        UserData data = new UserData(user);

        return new UserGetResult(data);
    }

    public UserGetAllResult getAll() {
        List<User> users = userRepository.findAll();
        List<UserData> userModels = users.stream().map(UserData::new).collect(Collectors.toList());
        return new UserGetAllResult(userModels);
    }

    public UserRegisterResult register(UserRegisterCommand command) {
        UserName name = new UserName(command.getName());
        User user = userFactory.create(name);
        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("이미 등록된 사용자임" + user.getName());
        }

        userRepository.save(user);

        return new UserRegisterResult(user.getId().getValue());
    }

    public void update(UserUpdateCommand command) {
        UserId id = new UserId(command.getId());
        User user = userRepository.find(id);
        if (user == null) {
            throw new UserNotFoundException(id+"");
        }

        if (command.getName() != null) {
            UserName name = new UserName(command.getName());
            user.changeName(name);

            if (userService.exists(user)) {
                throw new CanNotRegisterUserException("이미 등록된 사용자임: " + user.getName());
            }
        }

        userRepository.save(user);
    }

    public void delete(UserDeleteCommand command) {
        UserId id = new UserId(command.getId());
        User user = userRepository.find(id);
        if (user == null) {
            return;
        }

        userRepository.delete(user);
    }
}
