package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SmartPlugRepository extends SwitchableRepository<SmartPlug> {
    @Transactional
    Collection<SmartPlug> findByOn(boolean on);

    /**
     * Updates total consumption of all activated smart plugs by considering a load of
     * fakeConsumption W. This query must be executed every second
     *
     * @see ch.usi.inf.sa4.sanmarinoes.smarthut.scheduled.UpdateTasks
     * @param fakeConsumption the fake consumption in watts
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            "UPDATE SmartPlug s SET totalConsumption = s.totalConsumption + ?1 / 3600.0 WHERE s.on = true")
    void updateTotalConsumption(Double fakeConsumption);
}
