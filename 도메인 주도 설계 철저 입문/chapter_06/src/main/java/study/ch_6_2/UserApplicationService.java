package study.ch_6_2;

public class UserApplicationService {
    private final IUserRepository userRepository;
    private final UserService userService;

    public UserApplicationService(IUserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void register(String name, String mailAddress) {
        User user = new User(
                new UserName(name),
                new MailAddress(mailAddress)
        );

        if (userService.exists(user)) {
            throw new CanNotRegisterUserException("이미 등록된 사용자명임: " + user.getName());
        }

        userRepository.save(user);
    }

    public UserData get(String userId) {
        UserId targetId = new UserId(userId);
        User user = userRepository.find(targetId);

        if (user == null) {
            return null;
        }

        return new UserData(user);
    }

    public void update(UserUpdateCommand command) {
        UserId targetId = new UserId(command.getId());
        User user = userRepository.find(targetId);

        if (user == null) {
            throw new UserNotFoundException(targetId.getValue());
        }

        String name = command.getName();
        if (name != null) {
            // 사용자명 중복 여부를 확인
            UserName newUserName = new UserName(name);
            User duplicateUser = userRepository.find(newUserName);
            if (duplicateUser != null) {
                throw new CanNotRegisterUserException("이미 존재하는 사용자명임: " + duplicateUser.getName());
            }

            user.changeName(newUserName);
        }

        String mailAddress = command.getMailAddress();
        if (mailAddress != null) {
            MailAddress newMailAddress = new MailAddress(mailAddress);
            user.changeMailAddress(newMailAddress);
        }

        userRepository.save(user);
    }

    public void update(String userId, String name, String mailAddress) {
        UserId targetId = new UserId(userId);
        User user = userRepository.find(targetId);

        if (user == null) {
            throw new UserNotFoundException(targetId.getValue());
        }

        if (name != null) {
            UserName newUserName = new UserName(name);
            user.changeName(newUserName);
            if (userService.exists(user)) {
                throw new CanNotRegisterUserException("이미 존재하는 사용자명임");
            }
        }

        // 이메일 주소를 수정하는 경우
        if (mailAddress != null) {
            MailAddress newMailAddress = new MailAddress(mailAddress);
            user.changeMailAddress(newMailAddress);
        }

        userRepository.save(user);
    }

    public void delete(UserDeleteCommand command) {
        UserId targetId = new UserId(command.getId());
        User user = userRepository.find(targetId);

        if (user == null) {
            // 탈퇴 대상 사용자가 발견되지 않았다면 탈퇴 처리 성공으로 간주한다.
            return;
        }

        userRepository.delete(user);
    }
}
