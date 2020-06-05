package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ScenePrioritySaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ScenePriority;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ScenePriorityRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/scenePriority")
public class ScenePriorityController {

    @Autowired ScenePriorityRepository scenePriorityRepository;

    @GetMapping("/{automationId}")
    public List<ScenePriority> getByAutomationId(@PathVariable long automationId) {
        return scenePriorityRepository.findAllByAutomationId(automationId);
    }

    private ScenePriority save(ScenePriority newRL, ScenePrioritySaveRequest s) {
        newRL.setPriority(s.getPriority());
        newRL.setAutomationId(s.getAutomationId());
        newRL.setSceneId(s.getSceneId());

        return scenePriorityRepository.save(newRL);
    }

    @PostMapping
    public ScenePriority create(
            @Valid @RequestBody ScenePrioritySaveRequest scenePrioritySaveRequest) {
        return save(new ScenePriority(), scenePrioritySaveRequest);
    }

    @PutMapping
    public ScenePriority update(
            @Valid @RequestBody ScenePrioritySaveRequest scenePrioritySaveRequest)
            throws NotFoundException {
        return save(
                scenePriorityRepository
                        .findById(scenePrioritySaveRequest.getSceneId())
                        .orElseThrow(NotFoundException::new),
                scenePrioritySaveRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        scenePriorityRepository.deleteBySceneId(id);
    }
}
