package study.ch_7_4;

public class Client {

    public static void main(String[] args) {
        ServiceLocator.register(new InMemoryUserRepository());
        UserApplicationService userApplicationService = new UserApplicationService();
    }
}
