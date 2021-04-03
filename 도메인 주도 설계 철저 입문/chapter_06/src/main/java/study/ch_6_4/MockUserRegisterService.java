package study.ch_6_4;

public class MockUserRegisterService implements IUserRegisterService {
    @Override
    public void handle(UserRegisterCommand command) {
        throw new ComplexException();
    }
}
