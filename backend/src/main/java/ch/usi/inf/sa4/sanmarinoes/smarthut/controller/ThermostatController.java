package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ThermostatSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.DuplicateStateException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.ThermostatPopulationService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/thermostat")
public class ThermostatController {

    @Autowired private DeviceService deviceService;
    @Autowired private ThermostatRepository thermostatRepository;
    @Autowired private ThermostatPopulationService thermostatService;
    @Autowired private SceneRepository sceneRepository;
    @Autowired private StateRepository<State> stateRepository;

    private Thermostat save(Thermostat newT, ThermostatSaveRequest t, final Principal principal) {
        newT.setTargetTemperature(t.getTargetTemperature());
        newT.setId(t.getId());
        newT.setName(t.getName());
        newT.setRoomId(t.getRoomId());
        newT.setUseExternalSensors(t.isUseExternalSensors());
        newT.setOn(false);

        thermostatService.populateMeasuredTemperature(newT);
        newT = thermostatRepository.save(newT);

        newT.setOn(t.isTurnOn());
        return deviceService.saveAsOwner(newT, principal.getName());
    }

    @PostMapping
    public Thermostat create(@Valid @RequestBody ThermostatSaveRequest t, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(t.getRoomId(), principal.getName());
        return save(new Thermostat(), t, principal);
    }

    @PutMapping
    public Thermostat update(@Valid @RequestBody ThermostatSaveRequest t, final Principal principal)
            throws NotFoundException {
        return save(
                thermostatRepository
                        .findByIdAndUsername(t.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new),
                t,
                principal);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }

    @PostMapping("/{id}/state")
    public State sceneBinding(
            @PathVariable("id") long deviceId,
            @RequestParam long sceneId,
            final Principal principal)
            throws NotFoundException, DuplicateStateException {

        Thermostat d =
                thermostatRepository
                        .findByIdAndUsername(deviceId, principal.getName())
                        .orElseThrow(NotFoundException::new);
        State s = d.cloneState();
        final Scene sc = sceneRepository.findById(sceneId).orElseThrow(NotFoundException::new);
        s.setSceneId(sc.getId());
        if (stateRepository.countByDeviceIdAndSceneId(deviceId, sceneId) > 0)
            throw new DuplicateStateException();
        return stateRepository.save(s);
    }
}
