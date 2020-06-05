package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.DimmableSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.DuplicateStateException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/curtains")
public class CurtainsController {
    @Autowired private DeviceService deviceService;
    @Autowired private CurtainsRepository curtainsService;
    @Autowired private SceneRepository sceneRepository;
    @Autowired private StateRepository<State> stateRepository;

    private Curtains save(Curtains newRL, DimmableSaveRequest s, final Principal principal) {
        newRL.setName(s.getName());
        newRL.setRoomId(s.getRoomId());
        newRL.setIntensity(s.getIntensity());

        return deviceService.saveAsOwner(newRL, principal.getName());
    }

    @PostMapping
    public Curtains create(
            @Valid @RequestBody DimmableSaveRequest curtain, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(curtain.getRoomId(), principal.getName());
        return save(new Curtains(), curtain, principal);
    }

    @PutMapping
    public Curtains update(
            @Valid @RequestBody DimmableSaveRequest curtain, final Principal principal)
            throws NotFoundException {
        return save(
                curtainsService.findById(curtain.getId()).orElseThrow(NotFoundException::new),
                curtain,
                principal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }

    @PostMapping("/{id}/state")
    public State sceneBinding(
            @PathVariable("id") long deviceId,
            @RequestParam long sceneId,
            final Principal principal)
            throws NotFoundException, DuplicateStateException {

        Curtains c =
                curtainsService
                        .findByIdAndUsername(deviceId, principal.getName())
                        .orElseThrow(NotFoundException::new);
        State s = c.cloneState();
        final Scene sc = sceneRepository.findById(sceneId).orElseThrow(NotFoundException::new);
        s.setSceneId(sc.getId());
        if (stateRepository.countByDeviceIdAndSceneId(deviceId, sceneId) > 0)
            throw new DuplicateStateException();
        return stateRepository.save(s);
    }
}
