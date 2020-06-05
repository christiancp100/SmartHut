package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.CameraConfigurationService;
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
@RequestMapping("/securityCamera")
public class SecurityCameraController {
    private final DeviceService deviceService;
    private final SecurityCameraRepository securityCameraService;
    private final SceneRepository sceneRepository;
    private final StateRepository<State> stateRepository;
    private final CameraConfigurationService cameraConfigurationService;

    @Autowired
    public SecurityCameraController(
            DeviceService deviceService,
            SecurityCameraRepository securityCameraService,
            SceneRepository sceneRepository,
            StateRepository<State> stateRepository,
            CameraConfigurationService cameraConfigurationService) {
        this.deviceService = deviceService;
        this.securityCameraService = securityCameraService;
        this.sceneRepository = sceneRepository;
        this.stateRepository = stateRepository;
        this.cameraConfigurationService = cameraConfigurationService;
    }

    private SecurityCamera save(
            SecurityCamera newSC, SwitchableSaveRequest sc, final Principal principal) {
        newSC.setName(sc.getName());
        newSC.setRoomId(sc.getRoomId());
        newSC.setOn(sc.isOn());
        newSC.setPath(cameraConfigurationService.getVideoUrl());

        return deviceService.saveAsOwner(newSC, principal.getName());
    }

    @PostMapping
    public SecurityCamera create(
            @Valid @RequestBody SwitchableSaveRequest sc, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(sc.getRoomId(), principal.getName());
        return save(new SecurityCamera(), sc, principal);
    }

    @PutMapping
    public SecurityCamera update(
            @Valid @RequestBody SwitchableSaveRequest sc, final Principal principal)
            throws NotFoundException {
        return save(
                securityCameraService
                        .findByIdAndUsername(sc.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new),
                sc,
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

        SecurityCamera d =
                securityCameraService
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
