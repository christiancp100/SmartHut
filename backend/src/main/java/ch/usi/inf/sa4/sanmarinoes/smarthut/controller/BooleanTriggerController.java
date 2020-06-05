package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.BooleanConditionOrTriggerSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTrigger;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanTriggerRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/booleanTrigger")
public class BooleanTriggerController {

    @Autowired BooleanTriggerRepository booleanTriggerRepository;

    @GetMapping("/{automationId}")
    public List<BooleanTrigger> getAll(@PathVariable long automationId) {
        return booleanTriggerRepository.findAllByAutomationId(automationId);
    }

    private BooleanTrigger save(BooleanTrigger newRL, BooleanConditionOrTriggerSaveRequest s) {
        newRL.setDeviceId(s.getDeviceId());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setOn(s.isOn());

        return booleanTriggerRepository.save(newRL);
    }

    @PostMapping
    public BooleanTrigger create(
            @Valid @RequestBody BooleanConditionOrTriggerSaveRequest booleanTriggerSaveRequest) {
        return save(new BooleanTrigger(), booleanTriggerSaveRequest);
    }

    @PutMapping
    public BooleanTrigger update(
            @Valid @RequestBody BooleanConditionOrTriggerSaveRequest booleanTriggerSaveRequest)
            throws NotFoundException {
        return save(
                booleanTriggerRepository
                        .findById(booleanTriggerSaveRequest.getId())
                        .orElseThrow(NotFoundException::new),
                booleanTriggerSaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        booleanTriggerRepository.deleteById(id);
    }
}
