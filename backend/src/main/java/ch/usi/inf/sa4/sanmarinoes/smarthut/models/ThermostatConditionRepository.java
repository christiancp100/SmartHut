package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ThermostatConditionRepository extends ConditionRepository<ThermostatCondition> {

    List<ThermostatCondition> findAllByAutomationId(@Param("automationId") long automationId);
}
