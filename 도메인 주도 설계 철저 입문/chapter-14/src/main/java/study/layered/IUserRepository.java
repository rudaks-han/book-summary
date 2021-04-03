package study.layered;

import java.util.List;

public interface IUserRepository {
    User find(UserId id);
    List<User> findAll();
    User find(UserName name);
    void save(User user);
    void delete(User user);
}
