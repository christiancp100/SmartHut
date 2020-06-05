package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AutomationRepository extends CrudRepository<Automation, Long> {
    @EntityGraph(attributePaths = {"scenes", "triggers", "conditions"})
    List<Automation> findAllByUserId(@Param("userId") long userId);

    @EntityGraph(attributePaths = {"scenes", "triggers", "conditions"})
    Optional<Automation> findByIdAndUserId(@Param("id") long id, @Param("userId") long userId);
}
