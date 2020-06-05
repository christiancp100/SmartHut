package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TriggerRepository<T extends Trigger<?>> extends CrudRepository<T, Long> {

    List<T> findAllByDeviceId(@Param("deviceId") long deviceId);

    @Transactional
    void deleteAllByAutomationId(Long automationId);
}
