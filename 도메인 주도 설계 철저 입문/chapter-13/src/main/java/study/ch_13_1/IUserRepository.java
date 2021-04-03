package study.ch_13_1;

import java.util.List;

public interface IUserRepository {
    User find(UserId id);
    User find(User user);
    List<User> find(List<UserId> members);
    void save(User user);
}
