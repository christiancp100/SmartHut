package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.BooleanConditionOrTriggerSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanCondition;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanConditionRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/booleanCondition")
public class BooleanConditionController {

    @Autowired BooleanConditionRepository booleanConditionRepository;

    @GetMapping("/{automationId}")
    public List<BooleanCondition> getAll(@PathVariable long automationId) {
        return booleanConditionRepository.findAllByAutomationId(automationId);
    }

    private BooleanCondition save(BooleanCondition newRL, BooleanConditionOrTriggerSaveRequest s) {
        newRL.setDeviceId(s.getDeviceId());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setOn(s.isOn());

        return booleanConditionRepository.save(newRL);
    }

    @PostMapping
    public BooleanCondition create(
            @Valid @RequestBody BooleanConditionOrTriggerSaveRequest booleanTriggerSaveRequest) {
        return save(new BooleanCondition(), booleanTriggerSaveRequest);
    }

    @PutMapping
    public BooleanCondition update(
            @Valid @RequestBody BooleanConditionOrTriggerSaveRequest booleanTriggerSaveRequest)
            throws NotFoundException {
        return save(
                booleanConditionRepository
                        .findById(booleanTriggerSaveRequest.getId())
                        .orElseThrow(NotFoundException::new),
                booleanTriggerSaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        booleanConditionRepository.deleteById(id);
    }
}
