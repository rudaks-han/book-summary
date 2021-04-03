package study.layered;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class EFUserRepository implements IUserRepository {

    private final UserJpaRepository context;

    @Override
    public User find(UserId id) {
        User target = context.findById(id.getValue()).orElse(null);
        return target;
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(context.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public User find(UserName name) {
        User target = context.findById(name.getValue()).orElse(null);
        return target;
    }

    @Override
    public void save(User user) {
        context.save(user);
    }

    @Override
    public void delete(User user) {
        context.delete(user);
    }
}
