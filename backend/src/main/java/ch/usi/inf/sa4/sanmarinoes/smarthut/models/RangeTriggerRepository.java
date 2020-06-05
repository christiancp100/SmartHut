package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface RangeTriggerRepository extends TriggerRepository<RangeTrigger> {

    List<RangeTrigger> findAllByAutomationId(@Param("automationId") long automationId);
}
