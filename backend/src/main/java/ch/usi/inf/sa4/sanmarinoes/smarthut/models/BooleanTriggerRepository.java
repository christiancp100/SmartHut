package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface BooleanTriggerRepository extends TriggerRepository<BooleanTrigger> {

    List<BooleanTrigger> findAllByAutomationId(@Param("automationId") long automationId);
}
