package study.ch_5_2;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class InMemoryUserRepository implements IUserRepository {
    public HashMap<UserId, User> store = new HashMap<>();

    @Override
    public void save(User user) {
        // 저장시에도 깊은 복사를 수행
        this.store.put(user.getId(), clone(user));
    }

    @Override
    public User find(UserName userName) {
        User target = store.values().stream()
                .filter(user -> user.getName().equals(userName)).findAny()
                .orElseThrow(() -> new NoSuchElementException("userName not found: " + userName));

        if (target != null) {
            return clone(target);
        }
        return null;
    }

    @Override
    public boolean exists(UserName name) {
        return false;
    }

    // 깊은 복사를 담당하는 메서드
    private User clone(User user) {
        return new User(user.getId(), user.getName());
    }
}
