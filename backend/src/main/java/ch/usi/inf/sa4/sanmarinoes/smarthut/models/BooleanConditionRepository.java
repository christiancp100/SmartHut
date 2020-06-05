package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface BooleanConditionRepository extends ConditionRepository<BooleanCondition> {

    List<BooleanCondition> findAllByAutomationId(@Param("automationId") long automationId);
}
