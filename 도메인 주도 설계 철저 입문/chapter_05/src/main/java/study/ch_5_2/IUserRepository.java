package study.ch_5_2;

public interface IUserRepository {
    void save(User user);
    User find(UserName userName);
    boolean exists(UserName name);
}
