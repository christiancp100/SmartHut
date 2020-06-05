package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Automation;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.AutomationRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Trigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.TriggerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutomationService {
    private final AutomationRepository automationRepository;
    private final TriggerRepository<Trigger<?>> triggerRepository;

    @Autowired
    public AutomationService(
            AutomationRepository automationRepository,
            TriggerRepository<Trigger<?>> triggerRepository) {
        this.automationRepository = automationRepository;
        this.triggerRepository = triggerRepository;
    }

    public List<Trigger<?>> findTriggersByDeviceId(Long deviceId) {
        return triggerRepository.findAllByDeviceId(deviceId);
    }

    public Automation findByVerifiedId(Long automationId) {
        return automationRepository.findById(automationId).orElseThrow();
    }
}
