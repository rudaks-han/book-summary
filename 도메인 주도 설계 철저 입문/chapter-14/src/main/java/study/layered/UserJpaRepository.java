package study.layered;

import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<User, String>{
}
