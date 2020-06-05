package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchableSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.DuplicateStateException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/regularLight")
public class RegularLightController extends GuestEnabledController<RegularLight> {

    private RegularLightRepository regularLightRepository;
    private SceneRepository sceneRepository;
    private StateRepository<State> stateRepository;
    private DeviceService deviceService;

    @Autowired
    public RegularLightController(
            UserRepository userRepository,
            RegularLightRepository regularLightRepository,
            SceneRepository sceneRepository,
            StateRepository<State> stateRepository,
            DeviceService deviceService) {
        super(userRepository, regularLightRepository);
        this.regularLightRepository = regularLightRepository;
        this.sceneRepository = sceneRepository;
        this.stateRepository = stateRepository;
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<RegularLight> findAll() {
        return toList(regularLightRepository.findAll());
    }

    @GetMapping("/{id}")
    public RegularLight findById(@PathVariable("id") long id) throws NotFoundException {
        return regularLightRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private RegularLight save(
            RegularLight initial, SwitchableSaveRequest rl, String username, Long hostId)
            throws NotFoundException {
        initial.setName(rl.getName());
        initial.setRoomId(rl.getRoomId());
        initial.setOn(rl.isOn());

        if (hostId == null) {
            return deviceService.saveAsOwner(initial, username);
        } else {
            return deviceService.saveAsGuest(initial, username, hostId);
        }
    }

    @PostMapping
    public RegularLight create(
            @Valid @RequestBody SwitchableSaveRequest rl, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(rl.getRoomId(), principal.getName());
        return save(new RegularLight(), rl, principal.getName(), null);
    }

    @PutMapping
    public RegularLight update(
            @Valid @RequestBody SwitchableSaveRequest rl,
            final Principal principal,
            @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {
        return save(
                fetchIfOwnerOrGuest(principal, rl.getId(), hostId),
                rl,
                principal.getName(),
                hostId);
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
        RegularLight d =
                regularLightRepository
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
