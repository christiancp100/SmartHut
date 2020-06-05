package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchableStateSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchableState;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchableStateRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/switchableState")
public class SwitchableStateController {

    @Autowired private SwitchableStateRepository switchableStateRepository;

    @PutMapping
    public SwitchableState update(@Valid @RequestBody SwitchableStateSaveRequest ss)
            throws NotFoundException {
        final SwitchableState initial =
                switchableStateRepository.findById(ss.getId()).orElseThrow(NotFoundException::new);
        initial.setOn(ss.isOn());
        switchableStateRepository.save(initial);
        return initial;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        switchableStateRepository.deleteById(id);
    }
}
