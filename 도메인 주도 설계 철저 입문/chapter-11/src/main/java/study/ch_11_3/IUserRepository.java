package study.ch_11_3;

public interface IUserRepository {
    User find(UserId id);
    void save(User user);
    UserId nextIdentity();
}
