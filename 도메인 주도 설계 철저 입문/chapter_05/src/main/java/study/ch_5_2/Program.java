package study.ch_5_2;

import org.junit.Assert;

public class Program {
    private IUserRepository userRepository;

    public Program(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String userName) {
        User user = new User(new UserId("123"), new UserName(userName));
        UserService userService = new UserService(userRepository);
        if (userService.exists(user)) {
            throw new IllegalArgumentException(user.getName().getValue() + "은 이미 존재하는 사용자명임");
        }

        userRepository.save(user);
    }

    public static void executeRepository() {
        IUserRepository userRepository = new UserRepository();
        Program program = new Program(userRepository);
        program.createUser("john");
    }

    public static void executeInMemoryRepository() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        Program program = new Program(userRepository);
        program.createUser("john");

        User user = userRepository.store.get(new UserId("john"));
        Assert.assertEquals("john", user.getName().getValue());
    }

    public static void main(String[] args) {
        //executeRepository();

        InMemoryUserRepository userRepository = new InMemoryUserRepository();

        // 리스트 5-15
        // 객체를 복원할 때 깊은 복사를 하지 않으면
        User user = userRepository.find(new UserName("John"));
        // 복원된 객체에 대한 조작이 리포지토리에 저장된 객체에도 영향을 미친다.
        user.changeUserName(new UserName("John"));

        // 리스트 5-16
        // 여기서 인스턴스를 바로 리포지토리에 저장하면
        userRepository.save(user);
        // 인스턴스에 대한 조작이 리포지토리에 저장된 객체에도 영향을 미친다.
        user.changeUserName(new UserName("john"));


    }
}
