package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchableSaveRequest;
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
@RequestMapping("/smartPlug")
public class SmartPlugController {

    @Autowired private DeviceService deviceService;
    @Autowired private SmartPlugRepository smartPlugRepository;
    @Autowired private SceneRepository sceneRepository;
    @Autowired private StateRepository<State> stateRepository;

    private SmartPlug save(SmartPlug newSP, SwitchableSaveRequest sp, final Principal principal) {
        newSP.setOn(sp.isOn());
        newSP.setName(sp.getName());
        newSP.setRoomId(sp.getRoomId());

        return deviceService.saveAsOwner(newSP, principal.getName());
    }

    @PostMapping
    public SmartPlug create(@Valid @RequestBody SwitchableSaveRequest sp, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(sp.getRoomId(), principal.getName());
        return save(new SmartPlug(), sp, principal);
    }

    @PutMapping
    public SmartPlug update(@Valid @RequestBody SwitchableSaveRequest sp, final Principal principal)
            throws NotFoundException {
        return save(
                smartPlugRepository
                        .findByIdAndUsername(sp.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new),
                sp,
                principal);
    }

    @DeleteMapping("/{id}/meter")
    public SmartPlug resetMeter(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        final SmartPlug s =
                smartPlugRepository
                        .findByIdAndUsername(id, principal.getName())
                        .orElseThrow(NotFoundException::new);

        s.resetTotalConsumption();
        return smartPlugRepository.save(s);
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

        SmartPlug d =
                smartPlugRepository
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
