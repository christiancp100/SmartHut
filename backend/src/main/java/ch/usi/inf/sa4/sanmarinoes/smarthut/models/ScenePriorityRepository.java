package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ScenePriorityRepository extends CrudRepository<ScenePriority, Long> {

    List<ScenePriority> findAllBySceneId(@Param("sceneId") long sceneId);

    @Transactional
    void deleteBySceneId(@Param("sceneId") long sceneId);

    List<ScenePriority> findAllByAutomationId(@Param("automationId") long automationId);

    @Transactional
    void deleteAllByAutomationId(Long automationId);
}
