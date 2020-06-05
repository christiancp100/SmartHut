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
@RequestMapping("/dimmableLight")
public class DimmableLightController extends GuestEnabledController<DimmableLight> {

    private final DimmableLightRepository dimmableLightRepository;
    private final SceneRepository sceneRepository;
    private final StateRepository<State> stateRepository;
    private final DeviceService deviceService;

    @Autowired
    public DimmableLightController(
            UserRepository userRepository,
            DimmableLightRepository dimmableLightRepository,
            SceneRepository sceneRepository,
            StateRepository<State> stateRepository,
            DeviceService deviceService) {
        super(userRepository, dimmableLightRepository);
        this.dimmableLightRepository = dimmableLightRepository;
        this.sceneRepository = sceneRepository;
        this.stateRepository = stateRepository;
        this.deviceService = deviceService;
    }

    private DimmableLight save(
            DimmableLight initial, DimmableSaveRequest dl, String username, Long hostId)
            throws NotFoundException {
        initial.setIntensity(dl.getIntensity());
        initial.setName(dl.getName());
        initial.setRoomId(dl.getRoomId());

        if (hostId == null) {
            return deviceService.saveAsOwner(initial, username);
        } else {
            return deviceService.saveAsGuest(initial, username, hostId);
        }
    }

    /**
     * Assume that only the host can create a device Here save always as host, but remember to
     * propagate change to guests (DeviceService.saveAsOwner())
     */
    @PostMapping
    public DimmableLight create(
            @Valid @RequestBody DimmableSaveRequest dl, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(dl.getRoomId(), principal.getName());
        return save(new DimmableLight(), dl, principal.getName(), null);
    }

    /** Logic for saving either as owner or guest is handled in method save of this controller */
    @PutMapping
    public DimmableLight update(
            @Valid @RequestBody DimmableSaveRequest sp,
            final Principal principal,
            @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {

        return save(
                fetchIfOwnerOrGuest(principal, sp.getId(), hostId),
                sp,
                principal.getName(),
                hostId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }

    /**
     * the full url should be: "/dimmableLight/{id}/state?sceneId={sceneId} however it is not
     * necessary to specify the query in the mapping
     */
    @PostMapping("/{id}/state")
    public State sceneBinding(
            @PathVariable("id") long deviceId,
            @RequestParam long sceneId,
            final Principal principal)
            throws NotFoundException, DuplicateStateException {

        DimmableLight d =
                dimmableLightRepository
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
