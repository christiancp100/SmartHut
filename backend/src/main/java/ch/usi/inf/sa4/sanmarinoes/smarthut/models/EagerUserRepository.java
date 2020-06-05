package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface EagerUserRepository extends CrudRepository<User, Long> {
    @EntityGraph(attributePaths = {"guests"})
    User findByUsername(String username);

    @EntityGraph(attributePaths = {"guests"})
    Optional<User> findById(Long userId);
}
