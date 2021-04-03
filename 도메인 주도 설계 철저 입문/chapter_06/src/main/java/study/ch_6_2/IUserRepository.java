package study.ch_6_2;

public interface IUserRepository {
    User find(UserId id);
    User find(UserName name);
    void save(User user);
    void delete(User user);
}
