package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.DimmableStateSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableState;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DimmableStateRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/dimmableState")
public class DimmableStateController {

    @Autowired private DimmableStateRepository dimmableStateRepository;

    @PutMapping
    public DimmableState update(@Valid @RequestBody DimmableStateSaveRequest ss)
            throws NotFoundException {
        final DimmableState initial =
                dimmableStateRepository.findById(ss.getId()).orElseThrow(NotFoundException::new);
        initial.setIntensity(ss.getIntensity());
        dimmableStateRepository.save(initial);
        return initial;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        dimmableStateRepository.deleteById(id);
    }
}
