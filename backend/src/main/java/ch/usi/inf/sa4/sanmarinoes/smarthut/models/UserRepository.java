package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findById(Long userId);

    User findByEmailIgnoreCase(String email);
}
