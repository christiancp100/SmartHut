package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SceneSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.SceneService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/scene")
public class SceneController {

    @Autowired private SceneRepository sceneRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SceneService sceneService;
    @Autowired private StateRepository<State> stateRepository;

    @GetMapping
    public List<Scene> findAll(
            Principal principal, @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {
        if (hostId == null) {
            return toList(sceneRepository.findByUsername(principal.getName()));
        } else {
            Utils.returnIfGuest(userRepository, null, hostId, principal);
            return sceneRepository.findByHostId(hostId);
        }
    }

    @PostMapping
    public @ResponseBody Scene create(
            @Valid @RequestBody SceneSaveRequest s, final Principal principal) {

        final String username = principal.getName();
        final Long userId = userRepository.findByUsername(username).getId();

        final Scene newScene = new Scene();

        newScene.setUserId(userId);
        newScene.setName(s.getName());
        newScene.setGuestAccessEnabled(s.isGuestAccessEnabled());
        newScene.setIcon(s.getIcon());

        return sceneRepository.save(newScene);
    }

    @PostMapping("/{id}/apply")
    public @ResponseBody List<Device> apply(
            @PathVariable("id") long id,
            final Principal principal,
            @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {
        if (hostId == null) {
            return sceneService.apply(
                    sceneRepository
                            .findByIdAndUsername(id, principal.getName())
                            .orElseThrow(NotFoundException::new),
                    principal.getName(),
                    false);
        } else {
            Utils.returnIfGuest(userRepository, null, hostId, principal);
            return sceneService.applyAsGuest(
                    sceneRepository
                            .findByIdAndUserIdAndGuestAccessEnabled(id, hostId, true)
                            .orElseThrow(NotFoundException::new),
                    principal.getName(),
                    hostId);
        }
    }

    @PostMapping("/{id}/copyFrom/{copyId}")
    public @ResponseBody List<State> copy(
            @PathVariable("id") long id,
            @PathVariable("copyId") long copyId,
            final Principal principal)
            throws NotFoundException {
        final Scene scene =
                sceneRepository
                        .findByIdAndUsername(id, principal.getName())
                        .orElseThrow(NotFoundException::new);
        final Scene copyFrom =
                sceneRepository
                        .findByIdAndUsername(copyId, principal.getName())
                        .orElseThrow(NotFoundException::new);

        return sceneService.copyStates(scene, copyFrom);
    }

    @PutMapping("/{id}")
    public @ResponseBody Scene update(
            @PathVariable("id") long id, @RequestBody SceneSaveRequest s, final Principal principal)
            throws NotFoundException {
        final Scene newScene =
                sceneRepository
                        .findByIdAndUsername(id, principal.getName())
                        .orElseThrow(NotFoundException::new);

        if (s.getName() != null) {
            newScene.setName(s.getName());
        }

        newScene.setIcon(s.getIcon());

        newScene.setGuestAccessEnabled(s.isGuestAccessEnabled());

        return sceneRepository.save(newScene);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id) {
        stateRepository.deleteAllBySceneId(id);
        sceneRepository.deleteById(id);
    }

    /**
     * Returns a List<State> of all Devices that are associated to a given scene (identified by its
     * id).
     */
    @GetMapping(path = "/{sceneId}/states")
    public List<State> getStates(@PathVariable("sceneId") long sceneId) {
        Iterable<State> states = stateRepository.findBySceneId(sceneId);
        return toList(states);
    }
}
