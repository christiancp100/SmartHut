package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ConditionRepository<T extends Condition<?>> extends CrudRepository<T, Long> {

    List<T> findAllByDeviceId(@Param("deviceId") long deviceId);

    List<T> findAllByAutomationId(@Param("automationId") long automationId);

    @Transactional
    void deleteAllByAutomationId(@Param("automationId") long automationId);
}
