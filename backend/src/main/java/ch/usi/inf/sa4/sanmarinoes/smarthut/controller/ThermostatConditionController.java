package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ThermostatConditionSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatConditionRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/thermostatCondition")
public class ThermostatConditionController {

    @Autowired ThermostatConditionRepository thermostatConditionRepository;

    @GetMapping("/{automationId}")
    public List<ThermostatCondition> getAll(@PathVariable long automationId) {
        return thermostatConditionRepository.findAllByAutomationId(automationId);
    }

    private ThermostatCondition save(ThermostatCondition newRL, ThermostatConditionSaveRequest s) {
        newRL.setDeviceId(s.getDeviceId());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setOperator(s.getOperator());
        newRL.setMode(s.getMode());

        return thermostatConditionRepository.save(newRL);
    }

    @PostMapping
    public ThermostatCondition create(
            @Valid @RequestBody ThermostatConditionSaveRequest booleanTriggerSaveRequest) {
        return save(new ThermostatCondition(), booleanTriggerSaveRequest);
    }

    @PutMapping
    public ThermostatCondition update(
            @Valid @RequestBody ThermostatConditionSaveRequest booleanTriggerSaveRequest)
            throws NotFoundException {
        return save(
                thermostatConditionRepository
                        .findById(booleanTriggerSaveRequest.getId())
                        .orElseThrow(NotFoundException::new),
                booleanTriggerSaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        thermostatConditionRepository.deleteById(id);
    }
}
