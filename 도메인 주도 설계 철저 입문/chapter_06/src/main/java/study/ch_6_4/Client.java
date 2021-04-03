package study.ch_6_4;

public class Client {
    private IUserRegisterService userRegisterService;

    public void register(String name) {
        UserRegisterCommand command = new UserRegisterCommand(name);
        userRegisterService.handle(command);
    }
}
