package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.RangeConditionOrTriggerSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RangeConditionRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/rangeCondition")
public class RangeConditionController {

    @Autowired RangeConditionRepository rangeConditionRepository;

    @GetMapping("/{automationId}")
    public List<RangeCondition> getAll(@PathVariable long automationId) {
        return rangeConditionRepository.findAllByAutomationId(automationId);
    }

    private RangeCondition save(RangeCondition newRL, RangeConditionOrTriggerSaveRequest s) {
        newRL.setDeviceId(s.getDeviceId());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setOperator(s.getOperator());
        newRL.setRange(s.getRange());

        return rangeConditionRepository.save(newRL);
    }

    @PostMapping
    public RangeCondition create(
            @Valid @RequestBody RangeConditionOrTriggerSaveRequest booleanTriggerSaveRequest) {
        return save(new RangeCondition(), booleanTriggerSaveRequest);
    }

    @PutMapping
    public RangeCondition update(
            @Valid @RequestBody RangeConditionOrTriggerSaveRequest booleanTriggerSaveRequest)
            throws NotFoundException {
        return save(
                rangeConditionRepository
                        .findById(booleanTriggerSaveRequest.getId())
                        .orElseThrow(NotFoundException::new),
                booleanTriggerSaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        rangeConditionRepository.deleteById(id);
    }
}
