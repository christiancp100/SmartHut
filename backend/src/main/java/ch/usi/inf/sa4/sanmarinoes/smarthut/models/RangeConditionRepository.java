package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface RangeConditionRepository extends ConditionRepository<RangeCondition> {
    List<RangeCondition> findAllByAutomationId(@Param("automationId") long automationId);
}
