package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.AutomationFastUpdateRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.AutomationSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.ConditionDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.ScenePriorityDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.TriggerDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/automation")
public class AutomationController {

    @Autowired private AutomationRepository automationRepository;
    @Autowired private ScenePriorityRepository sceneRepository;
    @Autowired private TriggerRepository<Trigger<?>> triggerRepository;
    @Autowired private ConditionRepository<Condition<?>> conditionRepository;
    @Autowired private UserRepository userService;

    @GetMapping
    public List<Automation> getAll(
            @RequestParam(value = "hostId", required = false) Long hostId,
            final Principal principal) {
        final Long userId = userService.findByUsername(principal.getName()).getId();
        return automationRepository.findAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public Automation get(@PathVariable long id) throws NotFoundException {
        return automationRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private Automation save(Automation newRL, AutomationSaveRequest s, Principal principal) {

        final Long userId = userService.findByUsername(principal.getName()).getId();
        newRL.setName(s.getName());
        newRL.setUserId(userId);

        return automationRepository.save(newRL);
    }

    @PostMapping
    public Automation create(
            @Valid @RequestBody AutomationSaveRequest automationSaveRequest, Principal principal) {
        return save(new Automation(), automationSaveRequest, principal);
    }

    @PutMapping
    public Automation update(
            @Valid @RequestBody AutomationSaveRequest automation, Principal principal)
            throws NotFoundException {
        return save(
                automationRepository
                        .findById(automation.getId())
                        .orElseThrow(NotFoundException::new),
                automation,
                principal);
    }

    @PutMapping("/fast")
    public Automation fastUpdate(
            @Valid @RequestBody AutomationFastUpdateRequest req, Principal principal)
            throws NotFoundException {
        final Automation a =
                automationRepository
                        .findByIdAndUserId(
                                req.getId(),
                                userService.findByUsername(principal.getName()).getId())
                        .orElseThrow(NotFoundException::new);

        a.setName(req.getName());
        automationRepository.save(a);

        triggerRepository.deleteAllByAutomationId(a.getId());
        sceneRepository.deleteAllByAutomationId(a.getId());
        conditionRepository.deleteAllByAutomationId(a.getId());

        Iterable<Trigger<?>> tt =
                triggerRepository.saveAll(
                        req.getTriggers()
                                .stream()
                                .map(TriggerDTO::toModel)
                                .map(t -> t.setAutomationId(a.getId()))
                                .collect(Collectors.toList()));
        Iterable<ScenePriority> ss =
                sceneRepository.saveAll(
                        req.getScenes()
                                .stream()
                                .map(ScenePriorityDTO::toModel)
                                .collect(Collectors.toList()));

        Iterable<Condition<?>> cc =
                conditionRepository.saveAll(
                        req.getConditions()
                                .stream()
                                .map(ConditionDTO::toModel)
                                .map(t -> t.setAutomationId(a.getId()))
                                .collect(Collectors.toList()));

        for (final ScenePriority s : ss) {
            s.setAutomationId(a.getId());

            // this is here just to pass the quality gate,
            // please do not replicate unless the quality gate sees
            // it as a bug
            s.setAutomation(a);
        }

        a.getScenes().clear();
        a.getTriggers().clear();
        a.getConditions().clear();
        ss.forEach(t -> a.getScenes().add(t));
        tt.forEach(t -> a.getTriggers().add(t));
        cc.forEach(t -> a.getConditions().add(t));

        return a;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        automationRepository.deleteById(id);
    }
}
