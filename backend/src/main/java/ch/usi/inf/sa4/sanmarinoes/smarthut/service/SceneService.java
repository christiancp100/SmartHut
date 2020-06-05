package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SceneService {

    private final DevicePopulationService devicePopulationService;
    private final DevicePropagationService devicePropagationService;
    private final StateRepository<State> stateRepository;
    private final SceneRepository sceneRepository;

    public Scene findByValidatedId(Long id) {
        return sceneRepository.findById(id).orElseThrow();
    }

    @Autowired
    public SceneService(
            DevicePopulationService devicePopulationService,
            DevicePropagationService devicePropagationService,
            StateRepository<State> stateRepository,
            SceneRepository sceneRepository) {
        this.devicePopulationService = devicePopulationService;
        this.devicePropagationService = devicePropagationService;
        this.stateRepository = stateRepository;
        this.sceneRepository = sceneRepository;
    }

    private List<Device> copyStatesToDevices(Scene fromScene) {
        final List<Device> updated = new ArrayList<>(fromScene.getStates().size());

        for (final State s : fromScene.getStates()) {
            s.apply();
            updated.add(s.getDevice());
        }

        devicePopulationService.populateComputedFields(updated);
        return updated;
    }

    public List<Device> apply(Scene newScene, String username, boolean fromTrigger) {
        List<Device> updated = copyStatesToDevices(newScene);
        devicePropagationService.saveAllAsOwner(updated, username, true, fromTrigger);
        return updated;
    }

    public List<Device> applyAsGuest(Scene newScene, String username, Long hostId) {
        List<Device> updated = copyStatesToDevices(newScene);
        devicePropagationService.saveAllAsGuestSceneApplication(updated, username, hostId);
        return updated;
    }

    public List<State> copyStates(Scene to, Scene from) {
        final ArrayList<State> states = new ArrayList<>();
        for (final State s : from.getStates()) {
            states.add(stateRepository.save(s.copyToSceneId(to.getId())));
        }
        return states;
    }
}
