package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.RangeConditionOrTriggerSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeTriggerRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/rangeTrigger")
public class RangeTriggerController {

    @Autowired RangeTriggerRepository rangeTriggerRepository;

    @GetMapping("/{automationId}")
    public List<RangeTrigger> getAll(@PathVariable long automationId) {
        return rangeTriggerRepository.findAllByAutomationId(automationId);
    }

    private RangeTrigger save(RangeTrigger newRL, RangeConditionOrTriggerSaveRequest s) {
        newRL.setDeviceId(s.getDeviceId());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setOperator(s.getOperator());
        newRL.setRange(s.getRange());

        return rangeTriggerRepository.save(newRL);
    }

    @PostMapping
    public RangeTrigger create(
            @Valid @RequestBody RangeConditionOrTriggerSaveRequest booleanTriggerSaveRequest) {
        return save(new RangeTrigger(), booleanTriggerSaveRequest);
    }

    @PutMapping
    public RangeTrigger update(
            @Valid @RequestBody RangeConditionOrTriggerSaveRequest booleanTriggerSaveRequest)
            throws NotFoundException {
        return save(
                rangeTriggerRepository
                        .findById(booleanTriggerSaveRequest.getId())
                        .orElseThrow(NotFoundException::new),
                booleanTriggerSaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        rangeTriggerRepository.deleteById(id);
    }
}
