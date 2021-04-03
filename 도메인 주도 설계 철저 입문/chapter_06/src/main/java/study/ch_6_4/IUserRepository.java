package study.ch_6_4;

import study.ch_6_2.User;
import study.ch_6_2.UserId;
import study.ch_6_2.UserName;

public interface IUserRepository {
    User find(UserId id);
    User find(UserName name);
    void save(User user);
    void delete(User user);
}
