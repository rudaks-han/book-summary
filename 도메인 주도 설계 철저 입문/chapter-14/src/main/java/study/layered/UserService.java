package study.layered;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public boolean exists(User user) {
        User duplicatedUser = userRepository.find(user.getName());
        return duplicatedUser != null;
    }
}
